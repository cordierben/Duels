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
import android.widget.ImageView;
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

public class Game32C extends AppCompatActivity {

    Button Match;
    TextView m1, m2, m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14,m15,m16,m17,m18,m19,m20,m21,m22,m23,m24,m25,m26,m27,m28,m29,m30,m31,m32;
    TextView[] t;
    int artiste;
    String nameArtiste;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    String applicationID = "423942";
    DeezerConnect deezerConnect;
    String mode;
    String Email;
    List<Song> songList=new ArrayList<>(32);
    List<Song> perdantList=new ArrayList<>(32);
    List<Song> songListR=new ArrayList<>();
    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game32_c);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


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
        Log.i("test","Init");
        m1=(TextView) findViewById(R.id.m1);
        m2=(TextView) findViewById(R.id.m2);
        m3=(TextView) findViewById(R.id.m3);
        m4=(TextView) findViewById(R.id.m4);
        m5=(TextView) findViewById(R.id.m5);
        m6=(TextView) findViewById(R.id.m6);
        m7=(TextView) findViewById(R.id.m7);
        m8=(TextView) findViewById(R.id.m8);
        m9=(TextView) findViewById(R.id.m9);
        m10=(TextView) findViewById(R.id.m10);
        m11=(TextView) findViewById(R.id.m11);
        m12=(TextView) findViewById(R.id.m12);
        m13=(TextView) findViewById(R.id.m13);
        m14=(TextView) findViewById(R.id.m14);
        m15=(TextView) findViewById(R.id.m15);
        m16=(TextView) findViewById(R.id.m16);
        m17=(TextView) findViewById(R.id.m17);
        m18=(TextView) findViewById(R.id.m18);
        m19=(TextView) findViewById(R.id.m19);
        m20=(TextView) findViewById(R.id.m20);
        m21=(TextView) findViewById(R.id.m21);
        m22=(TextView) findViewById(R.id.m22);
        m23=(TextView) findViewById(R.id.m23);
        m24=(TextView) findViewById(R.id.m24);
        m25=(TextView) findViewById(R.id.m25);
        m26=(TextView) findViewById(R.id.m26);
        m27=(TextView) findViewById(R.id.m27);
        m28=(TextView) findViewById(R.id.m28);
        m29=(TextView) findViewById(R.id.m29);
        m30=(TextView) findViewById(R.id.m30);
        m31=(TextView) findViewById(R.id.m31);
        m32=(TextView) findViewById(R.id.m32);
    }

    protected void position(List<Song> NumSong){
        for(int i=0;i<NumSong.size();i=i+1){
            if(NumSong.get(i).getPosGrille().equals("1")){
                m1.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("2")){
                m2.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("3")){
                m3.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("4")){
                m4.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("5")){
                m5.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("6")){
                m6.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("7")){
                m7.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("8")){
                m8.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("9")){
                m9.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("10")){
                m10.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("11")){
                m11.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("12")){
                m12.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("13")){
                m13.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("14")){
                m14.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("15")){
                m15.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("16")){
                m16.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("17")){
                m17.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("18")){
                m18.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("19")){
                m19.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("20")){
                m20.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("21")){
                m21.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("22")){
                m22.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("23")){
                m23.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("24")){
                m24.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("25")){
                m25.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("26")){
                m26.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("27")){
                m27.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("28")){
                m28.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("29")){
                m29.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("30")){
                m30.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("31")){
                m31.setText(NumSong.get(i).getTitle());
            }
            if(NumSong.get(i).getPosGrille().equals("32")){
                m32.setText(NumSong.get(i).getTitle());
            }
        }
        startAnimation();
        NextMatch(NumSong);
    }

    protected void startAnimation(){
        final LottieAnimationView loading=findViewById(R.id.musicLoading);
        loading.cancelAnimation();
        loading.setVisibility(View.GONE);
        Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        findViewById(R.id.match).setVisibility(View.VISIBLE);
        findViewById(R.id.match).startAnimation(anim);
        for(int i=0;i<t.length;i=i+1){
            t[i].setVisibility(View.VISIBLE);
            t[i].startAnimation(anim);
        }

        ImageView a1=findViewById(R.id.branche321);
        ImageView a2=findViewById(R.id.branche322);
        ImageView a3=findViewById(R.id.branche323);
        ImageView a4=findViewById(R.id.branche324);
        ImageView a5=findViewById(R.id.branche325);
        ImageView a6=findViewById(R.id.branche326);
        ImageView a7=findViewById(R.id.branche327);
        ImageView a8=findViewById(R.id.branche328);
        ImageView a9=findViewById(R.id.branche329);
        ImageView a10=findViewById(R.id.branche3210);
        ImageView a11=findViewById(R.id.branche3211);
        ImageView a12=findViewById(R.id.branche3212);
        ImageView a13=findViewById(R.id.branche3213);
        ImageView a14=findViewById(R.id.branche3214);
        ImageView a15=findViewById(R.id.branche3215);
        ImageView a16=findViewById(R.id.branche3216);
        ImageView a17=findViewById(R.id.branche3217);
        ImageView a18=findViewById(R.id.branche3218);
        ImageView a19=findViewById(R.id.branche3219);
        ImageView a20=findViewById(R.id.branche3220);
        ImageView a21=findViewById(R.id.branche32_21);
        ImageView a22=findViewById(R.id.branche32_22);
        ImageView a23=findViewById(R.id.branche32_23);
        ImageView a24=findViewById(R.id.branche32_24);
        ImageView a25=findViewById(R.id.branche32_25);
        ImageView a26=findViewById(R.id.branche32_26);
        ImageView a27=findViewById(R.id.branche32_27);
        ImageView a28=findViewById(R.id.branche32_28);
        ImageView a29=findViewById(R.id.branche32_29);
        ImageView a30=findViewById(R.id.branche32_30);
        ImageView a31=findViewById(R.id.branche32_31);
        ImageView a32=findViewById(R.id.branche32_32);
        ImageView[] tab={a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32};
        for(int i=0;i<tab.length;i=i+1){
            tab[i].setVisibility(View.VISIBLE);
            tab[i].startAnimation(anim);
        }
    }

    protected void animation(final int match,final int pos,List<Song> song) {
        final float density = getResources().getDisplayMetrics().density;

        if(match==16){
            Animation anim;
            if(pos<=16) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_16_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_16_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X;
                    if(pos<=16) X=140*density;
                    else X=690*density;
                    t[pos-1].setX(X);
                    Animation anim;
                    if(pos%2!=0) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_16_down);}
                    else  {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_16_up);}
                    t[pos-1].startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y = 85 * density;
                            float Y2 = 215 * density;
                            float Y3 = 325 * density;
                            float Y4 = 445 * density;
                            float Y5 = 565 * density;
                            float Y6 = 685 * density;
                            float Y7 = 805 * density;
                            float Y8 = 925 * density;
                            if ((pos == 1 || pos == 2 || pos == 17 || pos == 18))
                                t[pos-1].setY(Y);
                            if ((pos == 3 || pos == 4 || pos == 19 || pos == 20))
                                t[pos-1].setY(Y2);
                            if ((pos == 5 || pos == 6 || pos == 21 || pos == 22))
                                t[pos-1].setY(Y3);
                            if ((pos == 7 || pos == 8 || pos == 23 || pos == 24))
                                t[pos-1].setY(Y4);
                            if ((pos == 9 || pos == 10 || pos == 25 || pos == 26))
                                t[pos-1].setY(Y5);
                            if ((pos == 11 || pos == 12 || pos == 27 || pos == 28))
                                t[pos-1].setY(Y6);
                            if ((pos == 13 || pos == 14 || pos == 29 || pos == 30))
                                t[pos-1].setY(Y7);
                            if ((pos == 15 || pos == 16 || pos == 31 || pos == 32))
                                t[pos-1].setY(Y8);
                            LottieAnimationView a1=findViewById(R.id.branche3281);
                            LottieAnimationView a2=findViewById(R.id.branche3282);
                            LottieAnimationView a3=findViewById(R.id.branche3283);
                            LottieAnimationView a4=findViewById(R.id.branche3284);
                            LottieAnimationView a5=findViewById(R.id.branche3285);
                            LottieAnimationView a6=findViewById(R.id.branche3286);
                            LottieAnimationView a7=findViewById(R.id.branche3287);
                            LottieAnimationView a8=findViewById(R.id.branche3288);
                            LottieAnimationView a9=findViewById(R.id.branche3289);
                            LottieAnimationView a10=findViewById(R.id.branche32810);
                            LottieAnimationView a11=findViewById(R.id.branche32811);
                            LottieAnimationView a12=findViewById(R.id.branche32812);
                            LottieAnimationView a13=findViewById(R.id.branche32813);
                            LottieAnimationView a14=findViewById(R.id.branche32814);
                            LottieAnimationView a15=findViewById(R.id.branche32815);
                            LottieAnimationView a16=findViewById(R.id.branche32816);
                            LottieAnimationView[] tab8={a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16};
                            for (LottieAnimationView lottieAnimationView : tab8) {
                                lottieAnimationView.setSpeed(0.5f);
                                lottieAnimationView.playAnimation();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if(match==8){
            Animation anim=null;
            if(pos<=16) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_8_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_8_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X;
                    if(pos<=16) X=200*density;
                    else X=620*density;
                    t[pos-1].setX(X);
                    Animation anim;
                    if(pos==1 || pos==2 || pos==5 || pos==6 || pos==9||pos==10 || pos==13 || pos==14 || pos==17 || pos==18 || pos==21 || pos==22 || pos==25 || pos==26 || pos==29||pos==30) {
                        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_8_down);
                    } else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_8_up);}
                    t[pos-1].startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y = 150 * density;
                            float Y2 = 390 * density;
                            float Y3 = 625 * density;
                            float Y4 = 870 * density;
                            if(pos<=4 || (pos>16 && pos<=20)){
                                t[pos-1].setY(Y);
                            }
                            if((pos>4 && pos<=8) || (pos>20 && pos<=24)){
                                t[pos-1].setY(Y2);
                            }
                            if((pos>8 && pos<=12) || (pos>24 && pos<=28)){
                                t[pos-1].setY(Y3);
                            }
                            if((pos>12&&pos<=16) || (pos>28)){
                                t[pos-1].setY(Y4);
                            }
                            LottieAnimationView a1=findViewById(R.id.branche3241);
                            LottieAnimationView a2=findViewById(R.id.branche3242);
                            LottieAnimationView a3=findViewById(R.id.branche3243);
                            LottieAnimationView a4=findViewById(R.id.branche3244);
                            LottieAnimationView a5=findViewById(R.id.branche3245);
                            LottieAnimationView a6=findViewById(R.id.branche3246);
                            LottieAnimationView a7=findViewById(R.id.branche3247);
                            LottieAnimationView a8=findViewById(R.id.branche3248);
                            a1.playAnimation();
                            a2.playAnimation();
                            a3.playAnimation();
                            a4.playAnimation();
                            a5.playAnimation();
                            a6.playAnimation();
                            a7.playAnimation();
                            a8.playAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if(match==4){
            Animation anim=null;
            if(pos<=16) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_4_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_4_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X;
                    if(pos<=16) X=285*density;
                    else X=560*density;
                    t[pos-1].setX(X);
                    Animation anim;
                    if(pos<=4||(pos>8&&pos<=12)||(pos>16&&pos<=20)||(pos>24&&pos<=28)) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_4_down);}
                    else  {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_32_4_up);}
                    t[pos-1].startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y = 275 * density;
                            float Y2 = 750 * density;
                            if(pos<=8||pos>16&&pos<=24){
                                t[pos-1].setY(Y);
                            } else{
                                t[pos-1].setY(Y2);
                            }
                            LottieAnimationView a1=findViewById(R.id.branche3221);
                            LottieAnimationView a2=findViewById(R.id.branche3222);
                            LottieAnimationView a3=findViewById(R.id.branche3223);
                            LottieAnimationView a4=findViewById(R.id.branche3224);
                            a1.playAnimation();
                            a2.playAnimation();
                            a3.playAnimation();
                            a4.playAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if(match==2){
            Animation anim;
            if(pos<=16) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_2_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_2_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X=335*density;
                    float X2=510*density;
                    if(pos<=16) t[pos-1].setX(X);
                    else t[pos-1].setX(X2);
                    Animation anim;
                    if(pos<=8 || (pos>16 && pos<=24)){
                        anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_2_down);
                    }
                    else{
                        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_2_up);
                    }
                    t[pos-1].startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y=510*density;
                            t[pos-1].setY(Y);
                            LottieAnimationView a=findViewById(R.id.branche32finale);
                            a.setSpeed(0.5f);
                            a.playAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if(match==1){
            Animation anim;
            if(pos<=16) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_8_2_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_8_2_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X=420*density;
                    t[pos-1].setX(X);
                    Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_8_1_up);
                    t[pos-1].startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y=460*density;
                            t[pos-1].setY(Y);
                            LottieAnimationView a=findViewById(R.id.winner32);
                            a.playAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    protected void NextMatch(final List<Song> NumSongFinal){
        Match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choice=new Intent(getApplicationContext(), Choice.class);
                choice.putExtra("nbre",String.valueOf(NumSongFinal.size()));
                for(int i=0;i<NumSongFinal.size();i=i+1){
                    Log.i("********",NumSongFinal.get(i).getTitle()+" "+NumSongFinal.get(i).getPosGrille()+" "+NumSongFinal.get(i).getPosGrilleAct());
                    choice.putExtra("song "+i,NumSongFinal.get(i).getTitle());//Titre
                    choice.putExtra("pos "+i,NumSongFinal.get(i).getPosGrille());//Position grille
                    choice.putExtra("posAct "+i,NumSongFinal.get(i).getPosGrilleAct());//Position grille
                    choice.putExtra("info "+i,NumSongFinal.get(i).getImage());//image
                    choice.putExtra("urlPreview "+i,NumSongFinal.get(i).getPreview());//preview
                    choice.putExtra("activity","Game32C");
                }
                startActivityForResult(choice,SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
        Log.i("Test","nextmatch fin");
    }

    protected TextView[] tab(){
        TextView[] t=new TextView[32];
        t[0]=m1;
        t[1]=m2;
        t[2]=m3;
        t[3]=m4;
        t[4]=m5;
        t[5]=m6;
        t[6]=m7;
        t[7]=m8;
        t[8]=m9;
        t[9]=m10;
        t[10]=m11;
        t[11]=m12;
        t[12]=m13;
        t[13]=m14;
        t[14]=m15;
        t[15]=m16;
        t[16]=m17;
        t[17]=m18;
        t[18]=m19;
        t[19]=m20;
        t[20]=m21;
        t[21]=m22;
        t[22]=m23;
        t[23]=m24;
        t[24]=m25;
        t[25]=m26;
        t[26]=m27;
        t[27]=m28;
        t[28]=m29;
        t[29]=m30;
        t[30]=m31;
        t[31]=m32;
        for(int i=0;i<t.length;i=i+1){
            t[i].setSelected(true);
        }
        return t;
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
                    start.putExtra("NomArtiste",nameArtiste);
                    start.putExtra("Email",Email);
                    start.putExtra("IdArtiste",String.valueOf(artiste));
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
