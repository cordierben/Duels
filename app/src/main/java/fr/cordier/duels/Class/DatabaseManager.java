package fr.cordier.duels.Class;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.deezer.sdk.model.Artist;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Duels.db";
    private static final int DATABASE_VERSION=73;
    DeezerConnect deezerConnect;
    String applicationID = "423942";

    public DatabaseManager(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.i ("DATABASE","invoked");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String strSql2="CREATE TABLE IF NOT EXISTS User ("
                +"state Integer not null,"
                +"email VARCHAR(500) not null,"
                +"mdp VARCHAR(500) not null);";
        db.execSQL(strSql2);

        Log.i("DATABASE","onCreate invoked");
        //insertion(db,1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String delete="DROP TABLE IF EXISTS Artist;";
        db.execSQL(delete);
        String delete2="DROP TABLE IF EXISTS User;";
        db.execSQL(delete2);
        String delete3="DROP TABLE IF EXISTS Ranking;";
        db.execSQL(delete3);
        String delete4="DROP TABLE IF EXISTS Friend;";
        db.execSQL(delete4);
        Log.i("DATABASE","Drop Table invoked");
        this.onCreate(db);
        Log.i("DATABASE","onUpgrade invoked");
    }

    public void insertArtist(String artist,int idA,String image){
        artist=artist.replace("'","");
        String strSql="INSERT INTO Artist(Name,IdArtist,ImageArtist) VALUES('"+artist+"','"+idA+"','"+image+"');";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","inserArtist invoked");
    }

    public List<String> selectArtist(){
        String strSql="SELECT Name,IdArtist FROM Artist ORDER BY Name asc;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            int idA=cursor.getInt(1);
            info.add(String.valueOf(idA));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectArtist invoked");
        return info;
    }

    public List<String> selectArtistImg(){
        String strSql="SELECT Name,IdArtist,ImageArtist FROM Artist ORDER BY Name asc;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            int idA=cursor.getInt(1);
            info.add(String.valueOf(idA));
            info.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectArtist invoked");
        return info;
    }

    public List<String> countArtist(){
        String strSql="SELECT count(Id) FROM Artist;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","countArtist invoked");
        return info;
    }

    public List<String> searchArtist(String name){
        String strSql="SELECT Name,IdArtist,ImageArtist FROM Artist WHERE UPPER(Name) LIKE UPPER('"+name+"') ORDER BY Name asc;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            int idA=cursor.getInt(1);
            info.add(String.valueOf(idA));
            info.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","searchArtist invoked");
        return info;
    }

    public int selectRankingScore(int IdArtist,String song, int IdUser){
        song=song.replace("'","");
        String strSql="SELECT count(*) FROM Ranking WHERE IdArtist='"+IdArtist+"' AND musicName='"+song+"' AND IdUser='"+IdUser+"';";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        int i=0;
        while(!cursor.isAfterLast()) {
            i+=cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectRankingScore invoked");
        return i;
    }

    public List<String> selectRanking(int IdArtist){
        String strSql="SELECT musicName,SUM(score) AS total FROM Ranking WHERE IdArtist='"+IdArtist+"' GROUP BY musicName ORDER BY total desc limit 20;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            info.add(String.valueOf(cursor.getInt(1)));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectRanking invoked");
        return info;
    }

    public List<String> selectPersonalRanking(int IdArtist,int IdUser){
        String strSql="SELECT musicName,score FROM Ranking WHERE IdArtist='"+IdArtist+"' AND IdUser='"+IdUser+"' ORDER BY score desc limit 20;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            info.add(String.valueOf(cursor.getInt(1)));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectPersonalRanking invoked");
        return info;
    }

    public List<String> selectFriendRanking(int IdArtist,int IdUser){
        String strSql="SELECT U.username, R.musicname FROM Ranking as R, Friend as F, User as U WHERE F.Id1='"+IdUser+"' AND F.Id2='"+IdUser+"' AND F.Id2=U.Id AND F.Id1=U.Id AND U.Id=R.IdUser AND R.IdArtist='"+IdArtist+"' GROUP BY R.musicname ORDER BY SUM(R.score) desc LIMIT 3;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            info.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectFriendRanking invoked");
        return info;
    }

    public void insertRanking(int idA,String artist,String song,int scoreSup,int IdUser){
        artist=artist.replace("'","");
        song=song.replace("'","");
        String strSql="INSERT INTO Ranking (IdUser,IdArtist, Name, musicName,score) VALUES('"+IdUser+"','"+idA+"', '"+artist+"','"+song+"','"+scoreSup+"');";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","insertRanking invoked");
    }

    public void updateUser(int state,String email){
        String strSql="Update User Set state='"+state+"' where email='"+email+"';";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","UpdateRanking invoked");
    }

    public void insertUser(int state,String email,String mdp){

        String strSql="INSERT INTO User (state,email,mdp) " +
                "VALUES('"+state+"', '"+email+"','"+mdp+"');";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","insertUser invoked");
    }

    public List<String> selectUser(){
        String strSql="SELECT * FROM User;";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            int state=(cursor.getInt(0));
            info.add(String.valueOf(state));
            info.add(cursor.getString(1));
            info.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectUser invoked");
        return info;
    }

    public List<String> selectUserId(String email){
        String strSql="SELECT email FROM User WHERE email='"+email+"';";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectUser invoked");
        return info;
    }

    public void insertFriend(int id1,int id2){
        String strSql="INSERT INTO Friend (Id1, Id2, type, state) VALUES('"+id1+"','"+id2+"','Newbie','Pending');";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","insertFriend invoked");
    }

    public List<String> selectFriends(int id){
        String strSql="SELECT Id1,Id2 FROM Friend WHERE (Id1='"+id+"' OR Id2='"+id+"') AND State='Accepted';";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            int state=(cursor.getInt(0));
            info.add(String.valueOf(state));
            info.add(cursor.getString(1));
            info.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectFriends invoked");
        return info;
    }

    public void DeleteUser(String Email){
        String strSql="DELETE FROM User WHERE email='"+Email+"';";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","DeleteFriend invoked");
    }

    public List<String> CheckExistingFriend(int id, int id2){
        String strSql="SELECT State FROM Friend WHERE (Id1='"+id+"' AND Id2='"+id2+"') OR (Id2='"+id+"' AND Id1='"+id2+"')";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","CheckExistingFriend invoked");
        return info;
    }

    public List<String> selectPendingFriend(int id){
        String strSql="SELECT Id1 FROM Friend WHERE Id2='"+id+"' AND State='Pending';";
        Cursor cursor= this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        List<String> info=new ArrayList<>();
        while(!cursor.isAfterLast()) {
            info.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("Database","selectPendingFriend invoked");
        return info;
    }

    public void updateFriend(int id1,int id2,String etat){
        String strSql="UPDATE Friend SET State='"+etat+"' WHERE Id2='"+id1+"' AND Id1='"+id2+"';";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","UpdateFriend invoked");
    }

    public void DeleteFriend(int id1,int id2){
        String strSql="DELETE FROM Friend WHERE Id2='"+id1+"' AND Id1='"+id2+"';";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","DeleteFriend invoked");
    }

    public void insertion(SQLiteDatabase db,int id) {
        deezerConnect = new DeezerConnect(applicationID);
        String[] permissions = new String[]{Permissions.BASIC_ACCESS, Permissions.MANAGE_LIBRARY, Permissions.LISTENING_HISTORY};

        //Requete Deezer pour insertion en boucle
        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                Artist artist = (Artist) result;
                //Filtre
                //Version Firestore

                if (artist.getNbFans() > 200000) {
                    Log.i("*****","Adding "+artist.getName());
                    FirebaseFirestore db= FirebaseFirestore.getInstance();
                    Map<String,Object> user=new HashMap<>();
                    user.put("IdA",artist.getId());
                    user.put("Name",artist.getName());
                    user.put("Image",artist.getMediumImageUrl());
                    // Add a new document with a generated ID
                    db.collection("Artists")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("******", "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("******", "Error adding document", e);
                                }
                            });
                }
                if(id<10000) insertion(db,id+1);
                /*Version SQLITE
                if (artist.getNbFans() > 200000) {
                    insertArtist(artist.getName(),(int) artist.getId(),artist.getMediumImageUrl());
                    Log.i("******",artist.getName());
                }
                if(id<10000) insertion(db,id+1);*/
            }
            public void onUnparsedResult(String requestResponse, Object requestId) {
                insertion(db,id+1);
            }
            public void onException(Exception e, Object requestId) {
                insertion(db,id+1);
            }
        };
        // create the request
        DeezerRequest request = DeezerRequestFactory.requestArtist(id);
        // set a requestId, that will be passed on the listener's callback methods
        request.setId("myRequest");
        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);
    }
}
