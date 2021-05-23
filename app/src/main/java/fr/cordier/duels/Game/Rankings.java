package fr.cordier.duels.Game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

import fr.cordier.duels.Class.Song;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;

public class Rankings extends AppCompatActivity {

    String Email;
    int idA;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.container);

        //Récupération data
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");
        Log.i("*****",Email);
        idA=Integer.parseInt(intent.getStringExtra("IdArtiste"));
        name=intent.getStringExtra("NomArtiste");


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public String getUserEmail() { return Email; }

    public String getArtist() { return name; }

    public void PersonalResult(LinearLayout PersonalLayout){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Ranking")
                .whereEqualTo("Artist",name)
                .whereEqualTo("User",Email)
                .orderBy("Score", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete Ranking invoked");
                        if (task.isSuccessful()) {
                            int i=1;
                            for (DocumentSnapshot doc : task.getResult()) {
                                Song track=new Song(doc.getString("Music"),doc.getLong("Score").intValue(),doc.getString("Image"));
                                CreateRanking(i,PersonalLayout,track);
                                i=i+1;
                            }
                        } else {
                            Log.d("******","ERROR QUERY RANKING");
                        }
                    }
                });
    }

    public void GlobalResult(LinearLayout GlobalLayout, String country){
        if(country.equals("World")){
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            db.collection("Ranking")
                    .whereEqualTo("Artist",name)
                    .orderBy("Score", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.i("*******","onComplete invoked");
                            if (task.isSuccessful()) {
                                List<Song> song=new ArrayList<>();
                                int songCount=0;
                                for (DocumentSnapshot doc : task.getResult()) {
                                    int count=0;
                                    for(int i=0;i<song.size();i=i+1){
                                        if(song.get(i).getTitle().equals(doc.getString("Music"))){
                                            int scoreMusic=song.get(i).getScore();
                                            int scorefinal=doc.getDouble("Score").intValue()+scoreMusic;
                                            song.get(i).setScore(scorefinal);
                                            count=count+1;
                                        }
                                    }
                                    if(count==0){
                                        Song track=new Song(doc.getString("Music"),doc.getDouble("Score").intValue(),doc.getString("Image"));
                                        song.add(track);
                                    }
                                    songCount=songCount+1;
                                }
                                if(songCount==0) {
                                    CreateRanking(0,GlobalLayout,null);
                                } else {
                                    List<Song> values=tri(song);
                                    for(int i=values.size()-1;i>=0;i=i-1){
                                        Log.i("*****",values.get(i).getTitle());
                                        int pos=values.size()-i;
                                        if(!values.get(i).getTitle().equals("") &&!values.get(i).equals(null)) {
                                            CreateRanking(pos,GlobalLayout,values.get(i));
                                        }
                                    }
                                }
                            } else {
                                Log.d("******","ERROR QUERY RANKING");
                            }
                        }
                    });
        } else {
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            db.collection("Ranking")
                    .whereEqualTo("Artist",name)
                    .whereEqualTo("Country",country)
                    .orderBy("Score", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.i("*******","onComplete invoked");
                            if (task.isSuccessful()) {
                                List<Song> song=new ArrayList<>();
                                int songCount=0;
                                for (DocumentSnapshot doc : task.getResult()) {
                                    int count=0;
                                    for(int i=0;i<song.size();i=i+1){
                                        if(song.get(i).getTitle().equals(doc.getString("Music"))){
                                            int scoreMusic=song.get(i).getScore();
                                            int scorefinal=doc.getDouble("Score").intValue()+scoreMusic;
                                            song.get(i).setScore(scorefinal);
                                            count=count+1;
                                        }
                                    }
                                    if(count==0){
                                        Song track=new Song(doc.getString("Music"),doc.getDouble("Score").intValue(),doc.getString("Image"));
                                        song.add(track);
                                    }
                                    songCount=songCount+1;
                                }
                                if(songCount==0) {
                                    CreateRanking(0,GlobalLayout,null);
                                } else {
                                    List<Song> values=tri(song);
                                    for(int i=values.size()-1;i>=0;i=i-1){
                                        int pos=values.size()-i;
                                        if(!values.get(i).getTitle().equals("") &&values.get(i).equals(null)==false) {
                                            CreateRanking(pos,GlobalLayout,values.get(i));
                                        }
                                    }
                                }
                            } else {
                                Log.d("******","ERROR QUERY RANKING");
                            }
                        }
                    });
        }

    }

    public void FriendResult(TextView txt){
        selectEmail1(txt);
    }

    protected void selectRanking(List<String> mails,FirebaseFirestore ff,TextView txt){
        List<String> affichage=new ArrayList<>();
        for(int i=0;i<mails.size();i=i+1) {
            affichage.add("non");
        }
        for(int i=0;i<mails.size();i=i+1){
            final int i2=i;
            ff.collection("Users")
                    .whereEqualTo("Email",mails.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.i("*******", "onComplete invoked");
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    ff.collection("Ranking")
                                            .whereEqualTo("User",mails.get(i2))
                                            .whereEqualTo("Artist",name)
                                            .orderBy("Score",Query.Direction.DESCENDING)
                                            .limit(3)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    Log.i("*******", "onComplete invoked");
                                                    if (task.isSuccessful()) {
                                                        int pos=1;
                                                        for (DocumentSnapshot doc2 : task.getResult()) {
                                                            if(affichage.get(i2).equals("non")){
                                                                txt.append(doc.getString("Username")+"\n");
                                                                affichage.set(i2,"oui");
                                                            }
                                                            txt.append(pos+"/"+doc2.getString("Music")+"\n");
                                                            pos=pos+1;
                                                        }
                                                        txt.append("\n\n");
                                                    } else {
                                                        Log.d("******", "ERROR QUERY RANKING");
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.d("******", "ERROR QUERY RANKING");
                            }
                        }
                    });

        }

    }

    protected void selectEmail1(TextView txt){
        List<String> FriendList=new ArrayList<>();
        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.collection("Friends")
                .whereEqualTo("Email1",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******", "onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                if(doc.getString("State").equals("Accepted")){
                                    FriendList.add(doc.getString("Email2"));
                                }
                            }
                            selectEmail2(FriendList,ff,txt);
                        } else {
                            Log.d("******", "ERROR QUERY RANKING");
                        }
                    }
                });
    }

    protected void selectEmail2(List<String> FriendList,FirebaseFirestore ff,TextView txt){
        ff.collection("Friends")
                .whereEqualTo("Email2",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******", "onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                if(doc.getString("State").equals("Accepted")){
                                    FriendList.add(doc.getString("Email1"));
                                }
                            }
                            selectRanking(FriendList,ff,txt);
                        } else {
                            Log.d("******", "ERROR QUERY RANKING");
                        }
                    }
                });
    }

    protected List<Song> tri(List<Song> song){
        List<Song> top=new ArrayList<>();
        for (int i = 0; i < song.size() - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j < song.size(); j++)
            {
                if (song.get(j).getScore() < song.get(index).getScore()){
                    index = j;
                }
            }
            Song min = song.get(index);
            song.set(index,song.get(i));
            song.set(i,min);
        }
        top.addAll(song);
        return top;

    }

    @SuppressLint("SetTextI18n")
    protected void CreateRanking(int pos, LinearLayout GlobalLayout, Song tracks){
        if(pos==0){
            TextView txt=new TextView(getApplicationContext());
            txt.setText("This country does not have a ranking for"+name);
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            GlobalLayout.addView(txt);
        } else {
            LinearLayout linear = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(0,10,0,10);
            linear.setPadding(5,5,5,5);
            linear.setLayoutParams(param);
            linear.setOrientation(LinearLayout.HORIZONTAL);
            if (pos == 1) linear.setBackgroundColor(Color.parseColor("#FFD700"));
            else if (pos == 2) linear.setBackgroundColor(Color.parseColor("#C0C0C0"));
            else if (pos == 3) linear.setBackgroundColor(Color.parseColor("#614e1a"));
            else {
                GradientDrawable border = new GradientDrawable();//white background
                border.setStroke(1, 0xFF000000); //black border with full opacity
                linear.setBackground(border);
            }

            ImageView im=new ImageView(getApplicationContext());
            Picasso.get().load(tracks.getImage()).into(im);
            final float density = getResources().getDisplayMetrics().density;
            int taille=(int)density*80;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(taille,taille);

            im.setLayoutParams(params);
            linear.addView(im);

            TextView txt = new TextView(getApplicationContext());
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            txt.setTextSize(25);
            txt.setText(pos+". "+tracks.getTitle() + "\nScore : " + tracks.getScore());

            linear.addView(txt);
            GlobalLayout.addView(linear);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), GenreList.class);
        start.putExtra("Email",Email);
        startActivity(start);
        finish();
    }
}
