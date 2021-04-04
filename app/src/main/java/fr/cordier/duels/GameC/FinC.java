package fr.cordier.duels.GameC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.cordier.duels.Class.Song;
import fr.cordier.duels.Game.Rankings;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class FinC extends AppCompatActivity {

    Button menu;
    String Email;
    LinearLayout ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_c);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutFC);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

        ranking=findViewById(R.id.FinalRankingLayout);
        menu=(Button) findViewById(R.id.menu);

        //Recup data
        Intent intent=getIntent();
        int val=Integer.parseInt(intent.getStringExtra("nbre"));
        Email=intent.getStringExtra("Email");
        int taille=Integer.parseInt(intent.getStringExtra("totalP"));
        List<Song> tracks=new ArrayList<>();
        tracks.add(new Song(intent.getStringExtra("vainqueur"),0,intent.getStringExtra("vainqueurIm")));
        for(int i=1;i<val;i=i+1){
            int index=taille-i;
            tracks.add(new Song(intent.getStringExtra("songD "+index),0,intent.getStringExtra("imageD "+index)));
        }
        CreateRanking(tracks);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                finish();
            }
        });
    }

    protected void CreateRanking(List<Song> song){
        for(int i=0;i<song.size();i=i+1){
            final float density = getResources().getDisplayMetrics().density;

            ImageView im=new ImageView(getApplicationContext());

            ImageView pos=new ImageView(getApplicationContext());
            int SizeNumber=(int)density*40;
            ViewGroup.LayoutParams parampos = new ViewGroup.LayoutParams(SizeNumber,SizeNumber);

            pos.setLayoutParams(parampos);
            if(i==0){
                //Layout
                LinearLayout linear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                param.setMargins(0,10,0,10);
                linear.setPadding(5,5,5,5);
                linear.setLayoutParams(param);
                linear.setOrientation(LinearLayout.HORIZONTAL);
                linear.setBackgroundColor(Color.parseColor("#FFD700"));

                //Image numéro
                Picasso.get().load(R.drawable.un).into(pos);
                linear.addView(pos);

                //Image album
                Picasso.get().load(song.get(i).getImage()).into(im);
                int taille=(int)density*50;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(taille,taille);
                im.setLayoutParams(params);

                linear.addView(im);

                //Texte morceau
                TextView txt = new TextView(getApplicationContext());
                txt.setTextColor(Color.parseColor("#FFFFFF"));
                txt.setTextSize(25);
                LinearLayout.LayoutParams paramtxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                txt.setLayoutParams(paramtxt);
                txt.setText(song.get(i).getTitle());
                linear.addView(txt);

                ranking.addView(linear);

            } else {
                LinearLayout linear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                param.setMargins(0,10,0,10);
                linear.setPadding(5,5,5,5);
                linear.setLayoutParams(param);
                linear.setOrientation(LinearLayout.HORIZONTAL);
                if (i == 1) linear.setBackgroundColor(Color.parseColor("#C0C0C0"));
                else if (i == 2) linear.setBackgroundColor(Color.parseColor("#614e1a"));
                else {
                    GradientDrawable border = new GradientDrawable();//white background
                    border.setStroke(1, 0xFF000000); //black border with full opacity
                    linear.setBackground(border);
                }

                if(i==1)Picasso.get().load(R.drawable.deux).into(pos);
                if(i==2 || i==3)Picasso.get().load(R.drawable.quatre).into(pos);
                if(i==4||i==5||i==6||i==7)Picasso.get().load(R.drawable.huit).into(pos);
                linear.addView(pos);

                Picasso.get().load(song.get(i).getImage()).into(im);
                int taille=(int)density*50;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(taille,taille);
                im.setLayoutParams(params);
                linear.addView(im);

                TextView txt = new TextView(getApplicationContext());
                txt.setTextColor(Color.parseColor("#FFFFFF"));
                txt.setTextSize(25);
                txt.setText(song.get(i).getTitle());

                linear.addView(txt);
                ranking.addView(linear);
            }
        }

    }

    private long backPressedTime;
    private Toast backToast;
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to leave the game", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
