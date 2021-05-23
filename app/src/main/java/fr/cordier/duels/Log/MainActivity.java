package fr.cordier.duels.Log;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.deezer.sdk.model.Artist;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.cordier.duels.Class.DatabaseManager;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;


public class MainActivity extends AppCompatActivity {

    ImageView vinyle;
    private Button inscription;
    private Button connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        //Initialisation
        vinyle=(ImageView) findViewById(R.id.vinyle);
        inscription=(Button) findViewById(R.id.inscription);
        connection=(Button) findViewById(R.id.connection);
        inscription.setVisibility(View.GONE);
        connection.setText("Installation...");

        //Animation

        Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        vinyle.startAnimation(anim);
        anim(anim);

        boolean first=false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            fichier();
            first=true;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        //insertionunique();
        final boolean firstfinal=first;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(firstfinal) Thread.sleep(6000);
                    else Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inscription.setVisibility(View.VISIBLE);
                        connection.setText("Log in");
                        inscription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent start = new Intent(getApplicationContext(), Inscription.class);
                                startActivity(start);
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                                finish();
                            }
                        });
                        connection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent start = new Intent(getApplicationContext(), Connect.class);
                                startActivity(start);
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                                finish();
                            }
                        });
                    }
                });
            }
        }).start();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        DatabaseManager db=new DatabaseManager(this);
        List<String> info=db.selectUser();
        for(int i=0;i<info.size();i=i+3){
            if(info.get(i).equals("1")){
                validation(info.get(i+1));
            }
        }
    }

    private void anim(Animation animation){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vinyle.startAnimation(animation);
                        anim(animation);
                    }
                });
            }
        }).start();
    }

    public void validation(String emailV){
        Log.i("*******","valide invoked");
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("Email",emailV)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Intent start = new Intent(getApplicationContext(), Menu.class);
                                start.putExtra("Email",emailV);
                                startActivity(start);
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                                finish();
                            }
                            if(task.getResult().isEmpty()){
                                FirebaseAuth.getInstance().signOut();

                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });
    }

    public void fichier(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("artist.txt", MODE_PRIVATE);
            FirebaseFirestore ff=FirebaseFirestore.getInstance();
            ff.collection("Artists")
                    .orderBy("Name", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.i("*******","onComplete invoked");
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    String name=doc.getString("Name");
                                    String image=String.valueOf(doc.get("Image"));
                                    String id=String.valueOf(doc.get("IdA"));
                                    try{
                                        String text=name+";"+image+";"+id+"\n";
                                        fileOutputStream.write(text.getBytes());
                                    }catch (IOException e){
                                        Log.e("Exception", "File write failed: " + e.toString());
                                    }
                                }
                            } else {
                                Log.d("******","ERROR");
                            }
                            if(task.isComplete()){
                                try{
                                    Log.i("******","closed");
                                    fileOutputStream.close();
                                } catch (IOException e){
                                    Log.e("Exception", "File write failed: " + e.toString());
                                }
                            }
                        }
                    });

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    protected void insertionunique(){
        DeezerConnect deezerConnect = new DeezerConnect("423942");
        String[] permissions = new String[]{Permissions.BASIC_ACCESS, Permissions.MANAGE_LIBRARY, Permissions.LISTENING_HISTORY};

        //Requete Deezer pour insertion en boucle
        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                Playlist playlist=(Playlist) result;
                List<Track> tracks=playlist.getTracks();
                for(int i=0;i<tracks.size();i=i+1){
                    Artist artiste=tracks.get(i).getArtist();
                    FirebaseFirestore db= FirebaseFirestore.getInstance();
                    db.collection("Artists")
                            .whereEqualTo("IdA",artiste.getId())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Log.i("*******","onComplete invoked");
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Map<String,Object> user=new HashMap<>();
                                            user.put("IdA",artiste.getId());
                                            user.put("Name",artiste.getName());
                                            user.put("Image",artiste.getMediumImageUrl());
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
                                    } else {
                                        Log.i("******","REQUEST ERROR");
                                    }
                                }
                            });
                }
            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}
            public void onException(Exception e, Object requestId) {}
        };
        // create the request
        DeezerRequest request = DeezerRequestFactory.requestPlaylist(3155776842L);
        // set a requestId, that will be passed on the listener's callback methods
        request.setId("myRequest");
        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);
    }


}



