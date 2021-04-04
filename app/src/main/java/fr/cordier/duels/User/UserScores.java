package fr.cordier.duels.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import fr.cordier.duels.Class.Song;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;
import fr.cordier.duels.User.UserRankings;

public class UserScores extends AppCompatActivity {

    TextView classement;
    ImageView photo;
    TextView back;
    TextView artiste;
    String Email;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scores);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutScore);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

        //Recup data
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");
        String Artiste=intent.getStringExtra("Artiste");
        String Image=intent.getStringExtra("Image");

        //Init widget
        layout=findViewById(R.id.UserScoreLayout);
        photo=(ImageView) findViewById(R.id.ArtistImage);
        artiste=(TextView) findViewById(R.id.UserScoreArtist);
        artiste.setText(Artiste);
        Picasso.get().load(Image).into(photo);
        back=(TextView) findViewById(R.id.UserScoreToUserRankings);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                finish();
            }
        });

        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("Ranking")
                .whereEqualTo("User",Email)
                .whereEqualTo("Artist",Artiste)
                .orderBy("Score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******", "onComplete invoked");
                        if (task.isSuccessful()) {
                            List<Song> tracks=new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()) {
                                Song song=new Song(doc.getString("Music"),doc.getDouble("Score").intValue(),doc.getString("Image"));
                                tracks.add(song);
                            }
                            for(int i=0;i<tracks.size();i=i+1){
                                CreateRanking(i+1,layout,tracks.get(i));
                            }
                        } else {
                            Log.d("******", "ERROR QUERY RANKING");
                        }
                    }
                });
    }

    protected void CreateRanking(int pos, LinearLayout GlobalLayout, Song tracks){
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
        int taille=(int)density*70;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(taille,taille);

        im.setLayoutParams(params);
        linear.addView(im);

        TextView txt = new TextView(getApplicationContext());
        txt.setTextColor(Color.parseColor("#FFFFFF"));
        txt.setTextSize(24);
        txt.setText(pos+". "+tracks.getTitle() + "\nScore : " + tracks.getScore());

        linear.addView(txt);
        GlobalLayout.addView(linear);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), UserRankings.class);
        start.putExtra("Email",Email);
        startActivity(start);
        finish();
    }
}
