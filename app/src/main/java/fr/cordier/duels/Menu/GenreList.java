package fr.cordier.duels.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import fr.cordier.duels.Game.GroupList;
import fr.cordier.duels.GameC.MusicList;
import fr.cordier.duels.Log.MainActivity;
import fr.cordier.duels.R;
import fr.cordier.duels.User.Info;

public class GenreList extends AppCompatActivity {

    TextView play,profile,titre,friends,share,custom;
    ImageView settings,vinyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutgenre);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

        //Récup data
        Intent intent=getIntent();
        String Email=intent.getStringExtra("Email");
        Log.i("*****",Email);

        //Initialisation Widgets

        vinyle=(ImageView) findViewById(R.id.imageView3);

        play= findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final LottieAnimationView circle=findViewById(R.id.animCircle);
                circle.playAnimation();*/
                Intent start = new Intent(getApplicationContext(), GroupList.class);
                Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                vinyle.startAnimation(anim);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                finish();
            }
        });
        play.setVisibility(View.GONE);

        titre=findViewById(R.id.titreMenu);

        settings= findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Settings.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left );
                finish();
            }
        });

        custom= findViewById(R.id.customduels);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), GameMode.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        profile= findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Info.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down );
                finish();
            }
        });

        friends= findViewById(R.id.friends);
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Profile.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up);
                finish();
            }
        });

        share=(TextView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Share.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
                finish();
            }
        });

        AnimationButton();
    }

    protected void AnimationButton(){
        play.setVisibility(View.VISIBLE);
        Animation slide= AnimationUtils.loadAnimation(this, R.anim.slide_button);
        play.startAnimation(slide);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), MainActivity.class);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
        startActivity(start);
        finish();
    }
}
