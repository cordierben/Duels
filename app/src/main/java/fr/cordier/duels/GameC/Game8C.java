package fr.cordier.duels.GameC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Permissions;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.cordier.duels.Class.Song;
import fr.cordier.duels.Game.Choice;
import fr.cordier.duels.Game.Fin;
import fr.cordier.duels.R;

public class Game8C extends AppCompatActivity {

    Button Match;
    TextView m1, m2, m3,m4,m5,m6,m7,m8;
    TextView[] t;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    String Email;
    List<Song> perdantList=new ArrayList<>(8);
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game8_c);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        LinearLayout constraintlayout=findViewById(R.id.layoutG8C);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

        //Initialisation
        fa=this;
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");
        int nbre=Integer.parseInt(intent.getStringExtra("nbre"));
        List<Song> songList=new ArrayList<>();
        for(int i=0;i<nbre;i=i+1){
            String title=intent.getStringExtra("TrackName "+i);
            String image=intent.getStringExtra("TrackImage "+i);
            String preview=intent.getStringExtra("TrackPreview "+i);
            Song temp=new Song(title,0,image,preview,0);
            Song finalSong=new Song(temp,String.valueOf(i+1),String.valueOf(i+1));
            songList.add(finalSong);
        }

        Match=(Button) findViewById(R.id.match);
        initialisationText();
        t=tab();
        position(songList);
    }

    protected void initialisationText(){
        m1=(TextView) findViewById(R.id.m1);
        m2=(TextView) findViewById(R.id.m2);
        m3=(TextView) findViewById(R.id.m3);
        m4=(TextView) findViewById(R.id.m4);
        m5=(TextView) findViewById(R.id.m5);
        m6=(TextView) findViewById(R.id.m6);
        m7=(TextView) findViewById(R.id.m7);
        m8=(TextView) findViewById(R.id.m8);
    }

    protected TextView[] tab(){
        TextView[] t=new TextView[8];
        t[0]=m1;
        t[1]=m2;
        t[2]=m3;
        t[3]=m4;
        t[4]=m5;
        t[5]=m6;
        t[6]=m7;
        t[7]=m8;
        for(int i=0;i<t.length;i=i+1){
            t[i].setSelected(true);
        }
        return t;
    }

    protected void position(List<Song> NumSong){
        for(int i=0;i<NumSong.size();i=i+1){
            if(NumSong.get(i).getPosGrille().equals("1")){
                m1.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("1");
            }
            if(NumSong.get(i).getPosGrille().equals("2")){
                m2.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("8");
            }
            if(NumSong.get(i).getPosGrille().equals("3")){
                m3.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("4");
            }
            if(NumSong.get(i).getPosGrille().equals("4")){
                m4.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("5");
            }
            if(NumSong.get(i).getPosGrille().equals("5")){
                m5.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("6");
            }
            if(NumSong.get(i).getPosGrille().equals("6")){
                m6.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("3");
            }
            if(NumSong.get(i).getPosGrille().equals("7")){
                m7.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("7");
            }
            if(NumSong.get(i).getPosGrille().equals("8")){
                m8.setText(NumSong.get(i).getTitle());
                //NumSong.get(i).setPosGrille("2");
            }
        }
        startAnimation();
        NextMatch(NumSong);
    }

    protected void animation(final int match,final int pos,List<Song> song){
        final float density = getResources().getDisplayMetrics().density;
        //Animation latérale
        if(match==4){
            if(pos<=4){
                Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_4_right);
                for(int k=0;k<t.length/2;k=k+1){
                    if(pos==k+1) t[k].startAnimation(anim);
                }
            }
            else{
                Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_4_left);
                for(int k=t.length-1;k>=t.length/2;k=k-1){
                    if(pos==k+1) t[k].startAnimation(anim);
                }
            }
            //Animation verticale + new position X
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(950);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            float X=185*density;
                            float X2=560*density;
                            for(int i=0;i<t.length;i=i+1){
                                if(pos==i+1 && pos<=4) t[i].setX(X);
                                if(pos==i+1 && pos>4) t[i].setX(X2);
                            }
                            if(match==4){
                                if(pos%2==0){
                                    Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_4_up);
                                    for(int k=2;k<t.length+1;k=k+2){
                                        if(pos==k) t[k-1].startAnimation(anim2);
                                    }
                                }
                                else{
                                    Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_4_down);
                                    for(int k=1;k<t.length+1;k=k+2){
                                        if(pos==k) t[k-1].startAnimation(anim2);
                                    }
                                }
                            }
                        }
                    });
                }
            }).start();

            //New position Y
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1955);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            float X=185*density;
                            float X2=560*density;
                            float Y=150*density;
                            float Y2=455*density;
                            if(pos==1) m1.setX(X);
                            if(pos==1) m1.setY(Y);
                            if(pos==2) m2.setX(X);
                            if(pos==2) m2.setY(Y);
                            if(pos==3) m3.setX(X);
                            if(pos==3) m3.setY(Y2);
                            if(pos==4) m4.setX(X);
                            if(pos==4) m4.setY(Y2);
                            if(pos==5) m5.setX(X2);
                            if(pos==5) m5.setY(Y);
                            if(pos==6) m6.setX(X2);
                            if(pos==6) m6.setY(Y);
                            if(pos==7) m7.setX(X2);
                            if(pos==7) m7.setY(Y2);
                            if(pos==8) m8.setX(X2);
                            if(pos==8) m8.setY(Y2);

                            LottieAnimationView a1=findViewById(R.id.branche821);
                            LottieAnimationView a2=findViewById(R.id.branche822);
                            LottieAnimationView a3=findViewById(R.id.branche823);
                            LottieAnimationView a4=findViewById(R.id.branche824);
                            a1.playAnimation();
                            a2.playAnimation();
                            a3.playAnimation();
                            a4.playAnimation();
                        }
                    });
                }
            }).start();
        }

        if(match==2){
            for(int i=0;i<song.size();i=i+1){
                for(int j=0;j<t.length;j=j+1){
                    if(String.valueOf(t[j].getText()).equals(song.get(i).getTitle())){
                        final int j2=j;

                        //Animation latérale
                        if(j<4){
                            Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_2_right);
                            t[j].startAnimation(anim);
                        }
                        else{
                            Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_2_left);
                            t[j].startAnimation(anim);
                        }

                        //Animation verticale + new position X
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(950);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        float X=245*density;
                                        float X2=485*density;
                                        if(j2<4) t[j2].setX(X);
                                        else t[j2].setX(X2);
                                        if(j2<2 || j2==4 || j2==5){
                                            Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_2_down);
                                            t[j2].startAnimation(anim2);
                                        }
                                        else{
                                            Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_2_up);
                                            t[j2].startAnimation(anim2);
                                        }
                                    }
                                });
                            }
                        }).start();

                        //New position Y
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1955);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        float X=245*density;
                                        float X2=485*density;
                                        float Y=305*density;
                                        float Y2=305*density;
                                        if(j2<4){
                                            t[j2].setX(X);
                                            t[j2].setY(Y);
                                        }
                                        else{
                                            t[j2].setX(X2);
                                            t[j2].setY(Y2);
                                        }

                                        LottieAnimationView a=findViewById(R.id.branchefinale);
                                        a.setSpeed(0.5f);
                                        a.playAnimation();
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }
        }

        if(match==1){
            for(int i=0;i<song.size();i=i+1){
                for(int j=0;j<t.length;j=j+1) {
                    if (String.valueOf(t[j].getText()).equals(song.get(i).getTitle())) {
                        final int j2=j;
                        //Animation horizontale
                        Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_1_right);
                        Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_1_left);
                        if(j<4){
                            t[j].startAnimation(anim);
                        }
                        else{
                            t[j].startAnimation(anim2);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(950);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        float X=350*density;
                                        t[j2].setX(X);
                                        Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_1_up);
                                        t[j2].startAnimation(anim2);
                                    }
                                });
                            }
                        }).start();
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
                                        float X=360*density;
                                        float Y=240*density;
                                        t[j2].setX(X);
                                        t[j2].setY(Y);
                                    }
                                });
                            }
                        }).start();
                        LottieAnimationView a=findViewById(R.id.winner);
                        a.playAnimation();
                    }
                }
            }
        }
    }

    protected void startAnimation(){
        final LottieAnimationView loading=findViewById(R.id.musicLoading);
        loading.cancelAnimation();

        LottieAnimationView a1=findViewById(R.id.branche841);
        LottieAnimationView a2=findViewById(R.id.branche842);
        LottieAnimationView a3=findViewById(R.id.branche843);
        LottieAnimationView a4=findViewById(R.id.branche844);
        LottieAnimationView a5=findViewById(R.id.branche845);
        LottieAnimationView a6=findViewById(R.id.branche846);
        LottieAnimationView a7=findViewById(R.id.branche847);
        LottieAnimationView a8=findViewById(R.id.branche848);
        LottieAnimationView[] tab={a1,a2,a3,a4,a5,a6,a7,a8};
        for(int i=0;i<tab.length;i=i+1){
            tab[i].setSpeed(0.5f);
            tab[i].playAnimation();
        }

        loading.setVisibility(View.GONE);
        Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        findViewById(R.id.match).setVisibility(View.VISIBLE);
        findViewById(R.id.match).startAnimation(anim);
        for(int i=0;i<t.length;i=i+1){
            t[i].setVisibility(View.VISIBLE);
            t[i].startAnimation(anim);
        }
    }

    protected void NextMatch(final List<Song> NumSongFinal){
        Match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choice=new Intent(getApplicationContext(), Choice.class);
                int nbre=0;
                if(NumSongFinal.size()==8 || NumSongFinal.size()==16)nbre=8;
                if(NumSongFinal.size()==4) nbre=4;
                if(NumSongFinal.size()==2) nbre=2;
                choice.putExtra("nbre",String.valueOf(nbre));
                for(int i=0;i<NumSongFinal.size();i=i+1){
                    Log.i("********",NumSongFinal.get(i).getTitle()+" "+NumSongFinal.get(i).getPosGrille()+" "+NumSongFinal.get(i).getPosGrilleAct());
                    choice.putExtra("song "+i,NumSongFinal.get(i).getTitle());//Titre
                    choice.putExtra("pos "+i,NumSongFinal.get(i).getPosGrille());//Position grille
                    choice.putExtra("posAct "+i,NumSongFinal.get(i).getPosGrilleAct());//Position grille
                    choice.putExtra("info "+i,NumSongFinal.get(i).getImage());//image
                    choice.putExtra("urlPreview "+i,NumSongFinal.get(i).getPreview());//preview
                    choice.putExtra("activity","Game8C");
                }
                startActivityForResult(choice,SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
        Log.i("Test","nextmatch fin");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Song> resultV=new ArrayList<>();

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                int i=0;
                int tour=Integer.parseInt(data.getStringExtra("Tour"));
                while(i<tour){
                    String titreV=data.getStringExtra("songV "+i);
                    String imageV=data.getStringExtra("urlV "+i);
                    String previewV=data.getStringExtra("urlPreviewV "+i);
                    String posV=data.getStringExtra("posV "+i);
                    Song trackV=new Song(titreV,imageV,previewV,posV,String.valueOf(i+1));
                    resultV.add(trackV);

                    String titreD=data.getStringExtra("songD "+i);
                    String posD=data.getStringExtra("posD "+i);
                    String imageD=data.getStringExtra("urlD "+i);
                    String previewD=data.getStringExtra("urlPreviewD "+i);
                    Song trackD=new Song(titreD,imageD,previewD,posD,"0");
                    perdantList.add(trackD);
                    i=i+1;
                }

            }
        }
        Log.i("******","vainqueur "+resultV.size());
        //Animation de fin de tour
        for(int i=0;i<resultV.size();i=i+1){
            animation(resultV.size(),Integer.parseInt(resultV.get(i).getPosGrille()),resultV);
        }

        //S'il ne reste plus qu'un morceau
        if(resultV.size()==1){
            final List<Song> resultF=resultV;
            Match.setText("See Ranking!");
            Match.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent start=new Intent(getApplicationContext(), FinC.class);
                    for(int i=0;i<perdantList.size();i=i+1){
                        start.putExtra("songD "+i,perdantList.get(i).getTitle());
                        start.putExtra("imageD "+i,perdantList.get(i).getImage());
                    }
                    start.putExtra("vainqueur",resultF.get(0).getTitle());
                    start.putExtra("vainqueurIm",resultF.get(0).getImage());
                    start.putExtra("nbre",String.valueOf(8));
                    start.putExtra("totalP",String.valueOf(perdantList.size()));
                    start.putExtra("Email",Email);
                    startActivity(start);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            });
        }

        //Sinon prochain round
        else{
            NextMatch(resultV);
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
