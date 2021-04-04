package fr.cordier.duels.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import fr.cordier.duels.Game.GroupList;
import fr.cordier.duels.GameC.MusicList;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class GameMode extends AppCompatActivity {

    private TextView normal,custom,back;
    private String Email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        //Recuperation données
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");

        normal=findViewById(R.id.normalMode);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), MusicList.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                finish();
            }
        });

        custom=findViewById(R.id.customMode);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), GroupList.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                finish();
            }
        });

        back=findViewById(R.id.gameModeToMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

    }
}
