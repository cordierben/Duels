package fr.cordier.duels.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.cordier.duels.Class.Song;
import fr.cordier.duels.Game.Game;
import fr.cordier.duels.GameC.Game16C;
import fr.cordier.duels.GameC.Game32C;
import fr.cordier.duels.GameC.Game8C;
import fr.cordier.duels.R;

public class Choice extends AppCompatActivity {

    TextView match;
    Button song1, song2;
    ImageView im1, im2, preview1, preview2;
    Button suivant;
    int k=1;
    int numM=1;
    protected DeezerConnect mdeezerConnect=null;
    MediaPlayer m1=new MediaPlayer();
    MediaPlayer m2=new MediaPlayer();
    MediaPlayer prep=new MediaPlayer();
    List<Song> songList=new ArrayList<>();
    List<Song> songV=new ArrayList<>();
    List<Song> songD=new ArrayList<>();
    String activity="";
    LottieAnimationView waves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutC);

        //Lecteur Deezer
        mdeezerConnect = new DeezerConnect(this,"423942");
        DeezerConnect.forApp("423942").withContext(getApplicationContext()).build();

        //Initialisation
        match=(TextView) findViewById(R.id.numMatch);
        song1=(Button) findViewById(R.id.s1);
        song2=(Button) findViewById(R.id.s2);
        im1=(ImageView) findViewById(R.id.im1);
        im2=(ImageView) findViewById(R.id.im2);
        preview1=(ImageView) findViewById(R.id.preview1);
        preview2=(ImageView) findViewById(R.id.preview2);
        suivant=(Button) findViewById(R.id.suivant);
        waves=findViewById(R.id.waves);

        suivant.setVisibility(View.GONE);
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Récupération des matches
        Intent intent=getIntent();
        String nbre=intent.getStringExtra("nbre");
        activity=intent.getStringExtra("activity");
        final int val=Integer.parseInt(nbre);
        for(int i=0;i<val;i++){
            String title=intent.getStringExtra("song "+i);
            String pos=intent.getStringExtra("pos "+i);
            String posAct=intent.getStringExtra("posAct "+i);
            String image=intent.getStringExtra("info "+i);
            String preview=intent.getStringExtra("urlPreview "+i);
            Song track=new Song(title,image,preview,pos,posAct);
            songList.add(track);
        }
        match(songList,k);
        selection(songList);
    }

    protected void match(List<Song> songList,int k){
        match.setText("Match "+numM);
        numM=numM+1;
        for(int j=0;j<songList.size();j=j+1){
            final int j2=j;
            Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
            //Song 1
            if(Integer.parseInt(songList.get(j).getPosGrilleAct())==k){
                Picasso.get().load(songList.get(j).getImage()).into(im1);
                preview1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(m1.isPlaying()){
                            waves.pauseAnimation();
                            waves.cancelAnimation();
                            m1.stop();
                            preview1.setImageResource(R.drawable.play);
                        } else {
                            waves.playAnimation();
                            preview1.setImageResource(R.drawable.pause);
                            preview2.setImageResource(R.drawable.play);
                            m2.stop();
                            m1 = MediaPlayer.create(getApplicationContext(), Uri.parse(songList.get(j2).getPreview()));
                            m1.start();
                        }
                    }
                });
                song1.setText(songList.get(j2).getTitle());
                song1.startAnimation(anim);
            }
            //Song 2
            if(Integer.parseInt(songList.get(j).getPosGrilleAct())==k+1){
                Picasso.get().load(songList.get(j).getImage()).into(im2);
                preview2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(m2.isPlaying()){
                            waves.pauseAnimation();
                            waves.cancelAnimation();
                            m2.stop();
                            preview2.setImageResource(R.drawable.play);
                        } else {
                            waves.playAnimation();
                            preview2.setImageResource(R.drawable.pause);
                            preview1.setImageResource(R.drawable.play);
                            m1.stop();
                            m2 = MediaPlayer.create(getApplicationContext(), Uri.parse(songList.get(j2).getPreview()));
                            m2.start();
                        }
                    }
                });
                song2.setText(songList.get(j2).getTitle());
                song2.startAnimation(anim);
            }
        }
    }

    protected void selection(List<Song> songList){
        song1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview2.setImageResource(R.drawable.play);
                preview1.setImageResource(R.drawable.play);
                m1.stop();
                m2.stop();
                waves.pauseAnimation();
                waves.cancelAnimation();
                waves.setProgress(0);
                if(k<songList.size()){
                    for(int i=0;i<songList.size();i=i+1){
                        if(songList.get(i).getTitle().equals(String.valueOf(song1.getText()))){
                            songV.add(songList.get(i));
                        }
                        if(songList.get(i).getTitle().equals(String.valueOf(song2.getText()))){
                            songD.add(songList.get(i));
                        }
                    }
                    if(k==songList.size()-1){
                        suivant.setVisibility(View.VISIBLE);
                        song1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        song2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        retour();
                    }
                    else{
                        k=k+2;
                        match(songList,k);
                    }
                }
            }
        });
        song2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview2.setImageResource(R.drawable.play);
                preview1.setImageResource(R.drawable.play);
                m1.stop();
                m2.stop();
                waves.pauseAnimation();
                waves.cancelAnimation();
                waves.setProgress(0);
                if(k<songList.size()){
                    for(int i=0;i<songList.size();i=i+1){
                        if(songList.get(i).getTitle().equals(String.valueOf(song2.getText()))){
                            songV.add(songList.get(i));
                        }
                        if(songList.get(i).getTitle().equals(String.valueOf(song1.getText()))){
                            songD.add(songList.get(i));
                        }
                    }
                    if(k==songList.size()-1){
                        suivant.setVisibility(View.VISIBLE);
                        song1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        song2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        retour();
                    }
                    else{
                        k=k+2;
                        match(songList,k);
                    }
                }
            }
        });
    }

    protected void retour(){
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m2.stop();
                m1.stop();
                Intent intent=new Intent(getApplicationContext(), Game.class);
                intent.putExtra("Tour",String.valueOf(songV.size()));
                for(int i=0;i<songV.size();i=i+1){
                    intent.putExtra("songV "+i,songV.get(i).getTitle());
                    intent.putExtra("posV "+i,songV.get(i).getPosGrille());
                    intent.putExtra("urlV "+i,songV.get(i).getImage());
                    intent.putExtra("urlPreviewV "+i,songV.get(i).getPreview());

                    intent.putExtra("songD "+i,songD.get(i).getTitle());
                    intent.putExtra("posD "+i,songD.get(i).getPosGrille());
                    intent.putExtra("urlD "+i,songD.get(i).getImage());
                    intent.putExtra("urlPreviewD "+i,songD.get(i).getPreview());
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mdeezerConnect != null) {
            mdeezerConnect.logout(this);
        }
        Log.i("****","ok");

        // also clear the session store
        new SessionStore().clear(this);
    }

    private long backPressedTime;
    private Toast backToast;
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            if(Game.fa!=null)Game.fa.finish();
            if(Game16.fa!=null)Game16.fa.finish();
            if(Game32.fa!=null)Game32.fa.finish();
            if(Game8C.fa!=null)Game8C.fa.finish();
            if(Game16C.fa!=null)Game16C.fa.finish();
            if(Game32C.fa!=null)Game32C.fa.finish();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to leave the game", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
