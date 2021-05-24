package fr.cordier.duels.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
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
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class Game16 extends AppCompatActivity {

    Button Match;
    TextView m1, m2, m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14,m15,m16;
    TextView[] t;
    int artiste;
    String nameArtiste;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    String applicationID = "423942";
    DeezerConnect deezerConnect;
    String mode;
    String Email;
    List<Song> songList=new ArrayList<>(16);
    List<Song> perdantList=new ArrayList<>(16);
    List<Song> songListR=new ArrayList<>();

    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game16);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Initialisation
        Intent intent=getIntent();
        fa=this;
        mode=intent.getStringExtra("Mode");
        artiste=Integer.parseInt(intent.getStringExtra("Artiste"));
        Email=intent.getStringExtra("Email");
        nameArtiste=intent.getStringExtra("NomArtiste");
        Match=(Button) findViewById(R.id.match);
        initialisationText();
        t=tab();
        for(int i=0;i<16;i=i+1) songList.add(new Song("","","",0));

        deezerConnect = new DeezerConnect(applicationID);

        //Récupération des morceaux Deezer
        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                List<Album> albums=(List<Album>) result;
                titres(albums);
            }
            public void onUnparsedResult(String requestResponse, Object requestId) {}
            public void onException(Exception e, Object requestId) {}
        };

        // create the request
        DeezerRequest request = DeezerRequestFactory.requestArtistAlbums(artiste);
        // set a requestId, that will be passed on the listener's callback methods
        request.setId("myRequest");
        // launch the request asynchronously
        deezerConnect.requestAsync(request,listener);
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
        m9=(TextView) findViewById(R.id.m9);
        m10=(TextView) findViewById(R.id.m10);
        m11=(TextView) findViewById(R.id.m11);
        m12=(TextView) findViewById(R.id.m12);
        m13=(TextView) findViewById(R.id.m13);
        m14=(TextView) findViewById(R.id.m14);
        m15=(TextView) findViewById(R.id.m15);
        m16=(TextView) findViewById(R.id.m16);
    }

    protected TextView[] tab(){
        TextView[] t=new TextView[16];
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
        for (TextView textView : t) {
            textView.setSelected(true);
        }
        return t;
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
        }
        startAnimation();
        NextMatch(NumSong);
    }

    protected void ordre(String mode, List<Song> song){
        List<Song> NumSong=new ArrayList<>(16);
        if(mode.equals("Top")){
            for(int i=0;i<16;i=i+1) NumSong.add(new Song(song.get(i),String.valueOf(i+1),String.valueOf(i+1)));
        }
        if(mode.equals("Random")){
            List<String> randomList=new ArrayList<>();
            for(int i=0;i<16;i=i+1) randomList.add(String.valueOf(i+1));
            //Mixing postitions randomly
            Collections.shuffle(randomList);
            for(int i=0;i<16;i=i+1) NumSong.add(new Song(song.get(i),randomList.get(i),randomList.get(i)));
        }
        position(NumSong);
    }

    protected void startAnimation(){
        final LottieAnimationView loading=findViewById(R.id.musicLoading);
        loading.cancelAnimation();

        LottieAnimationView a1=findViewById(R.id.branche1681);
        LottieAnimationView a2=findViewById(R.id.branche1682);
        LottieAnimationView a3=findViewById(R.id.branche1683);
        LottieAnimationView a4=findViewById(R.id.branche1684);
        LottieAnimationView a5=findViewById(R.id.branche1685);
        LottieAnimationView a6=findViewById(R.id.branche1686);
        LottieAnimationView a7=findViewById(R.id.branche1687);
        LottieAnimationView a8=findViewById(R.id.branche1688);
        LottieAnimationView a9=findViewById(R.id.branche1689);
        LottieAnimationView a10=findViewById(R.id.branche16810);
        LottieAnimationView a11=findViewById(R.id.branche16811);
        LottieAnimationView a12=findViewById(R.id.branche16812);
        LottieAnimationView a13=findViewById(R.id.branche16813);
        LottieAnimationView a14=findViewById(R.id.branche16814);
        LottieAnimationView a15=findViewById(R.id.branche16815);
        LottieAnimationView a16=findViewById(R.id.branche16816);
        LottieAnimationView[] tab={a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16};
        for (LottieAnimationView lottieAnimationView : tab) {
            lottieAnimationView.setSpeed(0.5f);
            lottieAnimationView.playAnimation();
        }
        loading.setVisibility(View.GONE);
        Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        findViewById(R.id.match).setVisibility(View.VISIBLE);
        findViewById(R.id.match).startAnimation(anim);
        for (TextView textView : t) {
            textView.setVisibility(View.VISIBLE);
            textView.startAnimation(anim);
        }
    }

    protected void animation(final int match,final int pos,List<Song> song){
        final float density = getResources().getDisplayMetrics().density;

        if(match==8){
            Animation anim=null;
            if(pos<=8) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_8_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_8_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X;
                    if(pos<=8) X=170*density;
                    else X=620*density;
                    t[pos-1].setX(X);
                    Animation anim;
                    if(pos%2!=0) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_8_down);}
                    else  {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_8_up);}
                    t[pos-1].startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y=110*density;
                            float Y2=255*density;
                            float Y3=400*density;
                            float Y4=560*density;
                            if(pos==1) m1.setY(Y);
                            if(pos==2) m2.setY(Y);
                            if(pos==3) m3.setY(Y2);
                            if(pos==4) m4.setY(Y2);
                            if(pos==5) m5.setY(Y3);
                            if(pos==6) m6.setY(Y3);
                            if(pos==7) m7.setY(Y4);
                            if(pos==8) m8.setY(Y4);
                            if(pos==9) m9.setY(Y);
                            if(pos==10) m10.setY(Y);
                            if(pos==11) m11.setY(Y2);
                            if(pos==12) m12.setY(Y2);
                            if(pos==13) m13.setY(Y3);
                            if(pos==14) m14.setY(Y3);
                            if(pos==15) m15.setY(Y4);
                            if(pos==16) m16.setY(Y4);
                            LottieAnimationView a1=findViewById(R.id.branche1641);
                            LottieAnimationView a2=findViewById(R.id.branche1642);
                            LottieAnimationView a3=findViewById(R.id.branche1643);
                            LottieAnimationView a4=findViewById(R.id.branche1644);
                            LottieAnimationView a5=findViewById(R.id.branche1645);
                            LottieAnimationView a6=findViewById(R.id.branche1646);
                            LottieAnimationView a7=findViewById(R.id.branche1647);
                            LottieAnimationView a8=findViewById(R.id.branche1648);
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
            Log.i("*****",pos+" ");
            Animation anim=null;
            if(pos<=8) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_4_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_4_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X;
                    if(pos<=8) X=250*density;
                    else X=570*density;
                    t[pos-1].setX(X);
                    Animation anim;
                    if(pos==1||pos==2||pos==5||pos==6||pos==9||pos==10||pos==13||pos==14) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_4_down);}
                    else  {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_4_up);}
                    t[pos-1].startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y;
                            if((pos>=1&&pos<=4)||(pos>=9&&pos<=12)) Y=180*density;
                            else Y=485*density;
                            t[pos-1].setY(Y);
                            LottieAnimationView a1=findViewById(R.id.branche1621);
                            LottieAnimationView a2=findViewById(R.id.branche1622);
                            LottieAnimationView a3=findViewById(R.id.branche1623);
                            LottieAnimationView a4=findViewById(R.id.branche1624);
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
            if(pos<=8) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_2_right);}
            else {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_16_2_left);}
            t[pos-1].startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    float X=325*density;
                    float X2=515*density;
                    if(pos<=8) t[pos-1].setX(X);
                    else t[pos-1].setX(X2);
                    Animation anim;
                    if(pos<=4 || (pos>8 && pos<=12)){
                        anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_16_2_down);
                        t[pos-1].startAnimation(anim);
                    }
                    else{
                        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_16_2_up);
                        t[pos-1].startAnimation(anim);
                    }
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            float Y=335*density;
                            t[pos-1].setY(Y);
                            LottieAnimationView a=findViewById(R.id.branche16finale);
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
            if(pos<=8) {anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_8_2_right);}
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
                            float Y=270*density;
                            t[pos-1].setY(Y);
                            LottieAnimationView a=findViewById(R.id.winner);
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
                }
                startActivityForResult(choice,SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
        Log.i("Test","nextmatch fin");
    }

    protected void titres(List<Album> albums){
        for(int i=0;i<albums.size();i=i+1){
            final int id=i;
            final String image=albums.get(i).getBigImageUrl();
            RequestListener listener = new JsonRequestListener() {
                public void onResult(Object result, Object requestId) {
                    List<Track> AlbumTracks=(List<Track>) result;
                    if(mode.equals("Top")){
                        for(int i=0;i<AlbumTracks.size();i=i+1){
                            Song track=new Song(AlbumTracks.get(i).getShortTitle(),image,AlbumTracks.get(i).getPreviewUrl(),AlbumTracks.get(i).getRank());
                            Log.i("**",track.getTitle());
                            //On retire les live et les Remix
                            boolean ban=ban(track.getTitle());
                            if(!ban){
                                boolean doublon=false;
                                //On check que le morceau ne soit pas déjà présent
                                for(int j=0;j<songList.size();j=j+1){
                                    if(songList.get(j).getTitle().equals(track.getTitle()) || track.getTitle().equals(songList.get(j).getTitle()+" ")) {
                                        doublon=true;
                                        //Si doublon, on check si rank doublon < rank premier doublon
                                        if(songList.get(j).getRank()<=track.getRank()) {
                                            songList.get(j).setTitle(track.getTitle());
                                            songList.get(j).setImage(track.getImage());
                                            songList.get(j).setPreview(track.getPreview());
                                            songList.get(j).setRank(track.getRank());
                                        }
                                    }
                                }
                                //Si pas doublon, on cherche le min
                                if(!doublon){
                                    //Recherche du min
                                    int min=songList.get(0).getRank();
                                    int indexmin=0;
                                    for(int j=0;j<16;j=j+1){
                                        if(songList.get(j).getRank()<min){
                                            min=songList.get(j).getRank();
                                            indexmin=j;
                                        }
                                    }
                                    //Remplacement du min
                                    if(AlbumTracks.get(i).getRank()>=min){
                                        songList.get(indexmin).setTitle(track.getTitle());
                                        songList.get(indexmin).setImage(track.getImage());
                                        songList.get(indexmin).setPreview(track.getPreview());
                                        songList.get(indexmin).setRank(track.getRank());
                                    }
                                }
                            }

                        }
                    }
                    if(mode.equals("Random")){
                        for(int i=0;i<AlbumTracks.size();i=i+1){
                            Song track=new Song(AlbumTracks.get(i).getShortTitle(),albums.get(id).getBigImageUrl(),AlbumTracks.get(i).getPreviewUrl(),AlbumTracks.get(i).getRank());
                            if(track.getRank()>100000){
                                boolean ban=ban(track.getTitle());
                                if(!ban){
                                    songListR.add(track);
                                }

                            }
                        }
                    }
                }
                public void onUnparsedResult(String requestResponse, Object requestId) {}
                public void onException(Exception e, Object requestId) {}
            };
            // create the request
            DeezerRequest request = DeezerRequestFactory.requestAlbumTracks(albums.get(i).getId());
            // set a requestId, that will be passed on the listener's callback methods
            request.setId("myRequest");
            // launch the request asynchronously
            deezerConnect.requestAsync(request,listener);
        }

        //Mélange et création liste finale
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
                        if(mode.equals("Top")){
                            verification(songList);
                            ordre(mode,songList);
                        }
                        if(mode.equals("Random")){
                            verification(songListR);
                            int i=0;
                            List<Song> FinalList=new ArrayList<>();
                            while(i<16){
                                int index=algorithm();
                                Song track=songListR.get(index);
                                //On check que le morceau ne soit pas déjà dans la liste
                                boolean doublon=false;
                                for(int j=0;j<FinalList.size();j=j+1){
                                    if(track.getTitle().equals(FinalList.get(j).getTitle())) doublon=true;
                                }

                                //On ajoute s'il n'y est pas
                                if(!doublon){
                                    FinalList.add(track);
                                    i=i+1;
                                }
                            }
                            ordre(mode,FinalList);
                        }
                    }
                });
            }
        }).start();
    }

    protected void verification(List<Song> tracks){
        for(Song track:tracks){
            if(track.getTitle().isEmpty() || track.getTitle()==null){
                Toast.makeText(this,"This artist does not have 16 songs",Toast.LENGTH_SHORT).show();
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                finish();
            }
        }
    }

    protected int algorithm(){
        Random r=new Random();
        List<String> repartition=new ArrayList<>();
        for(int i=0;i<songListR.size();i=i+1){
            if(songListR.get(i).getRank()>10000){
                int total=(int)(songListR.get(i).getRank()/10000)*(songListR.get(i).getRank()/10000)*(songListR.get(i).getRank()/10000)/1000;
                for(int j=0;j<total;j=j+1){
                    repartition.add(String.valueOf(i));
                }
            }
        }
        return Integer.parseInt(repartition.get(r.nextInt(repartition.size())));
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
            classement(perdantList,resultF.get(0));
            Match.setText("See Ranking!");
            Match.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent start=new Intent(getApplicationContext(), Fin.class);
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
                    start.putExtra("activity","Game16");
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

    protected void classement(List<Song> perdant,Song vainqueur){
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        //Ranking vainqueur
        db.collection("Ranking")
                .whereEqualTo("Artist",nameArtiste)
                .whereEqualTo("User",Email)
                .whereEqualTo("Music",vainqueur.getTitle())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            int i=0;
                            int score=0;
                            String key="";
                            for (DocumentSnapshot doc : task.getResult()) {
                                i++;
                                score=doc.getDouble("Score").intValue();
                                key=doc.getId();
                            }
                            if(i==0){
                                add(vainqueur,db,20);
                            } else {
                                update(db,key,20+score);
                            }
                        } else {
                            Log.d("******","ERROR QUERY RANKING");
                        }
                    }
                });

        //Ranking perdants
        for(int song=0;song<perdant.size();song=song+1){
            final int songFinal=song;
            db.collection("Ranking")
                    .whereEqualTo("Artist",nameArtiste)
                    .whereEqualTo("User",Email)
                    .whereEqualTo("Music",perdant.get(songFinal).getTitle())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.i("*******","onComplete invoked");
                            if (task.isSuccessful()) {
                                int i=0;
                                int score=0;
                                String key="";
                                for (DocumentSnapshot doc : task.getResult()) {
                                    i++;
                                    score=doc.getDouble("Score").intValue();
                                    key=doc.getId();
                                }
                                if(i==0){
                                    add(perdant.get(songFinal),db,songFinal);
                                } else {
                                    update(db,key,songFinal+score);
                                }
                            } else {
                                Log.d("******","ERROR QUERY RANKING");
                            }
                        }
                    });
        }
    }

    protected void add(Song music,FirebaseFirestore db,int score){
        Map<String,Object> ranking=new HashMap<>();
        db.collection("Users")
                .whereEqualTo("Email",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                ranking.put("Artist",nameArtiste);
                                ranking.put("User",Email);
                                ranking.put("Country",doc.getString("Country"));
                                ranking.put("Music",music.getTitle());
                                ranking.put("Score",score);
                                ranking.put("Image",music.getImage());
                                // Add a new document with a generated ID
                                db.collection("Ranking")
                                        .add(ranking)
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
                            Log.d("******","ERROR QUERY RANKING");
                        }
                    }
                });

    }

    protected void update(FirebaseFirestore db,String key,int score){
        DocumentReference document=db.collection("Ranking").document(key);
        document.update("Score",score)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("******", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("******", "Error updating document", e);
                    }
                });
    }

    protected boolean ban(String song){
        boolean banni=false;
        if(song.contains("Remix") || song.contains("Live") || song.contains("Reanimation")){
            banni=true;
        }
        return banni;
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

