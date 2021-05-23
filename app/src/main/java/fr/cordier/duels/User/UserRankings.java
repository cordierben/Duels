package fr.cordier.duels.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.cordier.duels.Class.Artist;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class UserRankings extends AppCompatActivity {

    GridLayout grille;
    ImageView back;
    EditText search;
    ImageView loupe;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rankings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutRankings);

        //Recup data
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");

        grille=(GridLayout) findViewById(R.id.gridRanking);

        back=(ImageView) findViewById(R.id.UserRankingsToInfo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                finish();
            }
        });

        search=(EditText) findViewById(R.id.searchRanking);
        loupe=(ImageView) findViewById(R.id.loupeRanking);

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                search.setText("");
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindArtist();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindArtist();
            }
        });


        //Récupération artistes
        List<String> data=new ArrayList<>();
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("Ranking")
                .whereEqualTo("User",Email)
                .orderBy("Artist", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                int count=0;
                                for(int i=0;i<data.size();i=i+1){
                                    if(doc.getString("Artist").equals(data.get(i))){
                                        count=count+1;
                                    }
                                }
                                if(count==0){
                                    data.add(doc.getString("Artist"));
                                }

                            }
                            if(data.size()>0){
                                InfoArtist(data);
                            } else {
                                grille.removeAllViews();
                                TextView txt=new TextView(getApplicationContext());
                                txt.setText("You have not attempted any artists. Let's try some!");
                                grille.addView(txt);
                            }
                        } else {
                            Log.d("******","ERROR");
                        }
                    }
                });

    }

    protected void FindArtist(){
        String name=String.valueOf(search.getText());
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("Artists")
                .orderBy("Name").startAt(name).endAt(name+'\uf8ff')
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Artist> searchList=new ArrayList<Artist>();
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Artist artiste=new Artist(doc.getString("Name"),String.valueOf(doc.get("Image")),String.valueOf(doc.get("IdA")));
                                searchList.add(artiste);
                            }
                            ArtistList(searchList);
                        } else {
                            Log.d("******","ERROR");
                        }
                    }
                });
    }

    protected void InfoArtist(List<String> data){
        List<Artist> artists=new ArrayList<>();
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        for(int i=0;i<data.size();i=i+1){
            ff.collection("Artists")
                    .whereEqualTo("Name",data.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.i("*******","onComplete invoked");
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    Log.i("*****",doc.getString("Name"));
                                    Artist artist=new Artist(doc.getString("Name"),doc.getString("Image"),String.valueOf(doc.get("IdA")));
                                    artists.add(artist);
                                }
                                ArtistList(artists);
                            } else {
                                Log.d("******","ERROR");
                            }
                        }
                    });
        }

    }

    protected void ArtistList(List<Artist> artist){
        grille.removeAllViews();
        for(int i=0;i<artist.size();i=i+1){
            final int id=i;

            ImageView im=new ImageView(getApplicationContext());
            Picasso.get().load(artist.get(i).getImage()).into(im);
            grille.addView(im);

            TextView txt=new TextView(getApplicationContext());
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            txt.setText(artist.get(i).getName());
            txt.setTextSize(25);
            grille.addView(txt);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(txt.getLayoutParams());
            params.setGravity(Gravity.CENTER_VERTICAL);
            txt.setLayoutParams(params);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent start = new Intent(getApplicationContext(), UserScores.class);
                    start.putExtra("Email",Email);
                    start.putExtra("Artiste", String.valueOf(artist.get(id).getName()));
                    start.putExtra("Image",String.valueOf(artist.get(id).getImage()));
                    startActivity(start);
                    finish();
                }
            });
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent start = new Intent(getApplicationContext(), UserScores.class);
                    start.putExtra("Email",Email);
                    start.putExtra("Artiste", String.valueOf(artist.get(id).getName()));
                    start.putExtra("Image",String.valueOf(artist.get(id).getImage()));
                    startActivity(start);
                    finish();
                }
            });

        }
        for(int i=0;i<10;i=i+1) {
            TextView txt = new TextView(getApplicationContext());
            grille.addView(txt);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), Menu.class);
        start.putExtra("Email",Email);
        startActivity(start);
        finish();
    }
}
