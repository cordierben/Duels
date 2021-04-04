package fr.cordier.duels.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import fr.cordier.duels.Class.Song;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.Menu.Settings;
import fr.cordier.duels.R;

public class Game extends AppCompatActivity {

    Button Match;
    TextView m1, m2, m3,m4,m5,m6,m7,m8;
    TextView[] t;
    int artiste;
    String nameArtiste;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    String applicationID = "423942";
    DeezerConnect deezerConnect;
    String mode;
    String Email;
    String interruptedExcpetion="InterruptedException: ";
    String executionException="ExecutionException: ";
    List<Song> songList=new ArrayList<>(8);
    List<Song> perdantList=new ArrayList<>(8);
    List<Song> songListR=new ArrayList<>();
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        LinearLayout constraintlayout=findViewById(R.id.layoutG);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

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
        for(int i=0;i<8;i=i+1) songList.add(new Song("","","",0));

        deezerConnect = new DeezerConnect(applicationID);
        String[] permissions = new String[] {Permissions.BASIC_ACCESS,Permissions.MANAGE_LIBRARY,Permissions.LISTENING_HISTORY };

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
        }

        NextMatch(NumSong);
    }

    protected void ordre(String mode, List<Song> song){
        List<Song> NumSong=new ArrayList<>(8);
        if(mode.equals("Top")){
            for(int i=0;i<8;i=i+1) NumSong.add(new Song(song.get(i),String.valueOf(i+1),String.valueOf(i+1)));
        }
        if(mode.equals("Random")){
            List<String> randomList=new ArrayList<>();
            for(int i=0;i<8;i=i+1) randomList.add(String.valueOf(i+1));
            //Mixing postitions randomly
            Collections.shuffle(randomList);
            for(int i=0;i<8;i=i+1) NumSong.add(new Song(song.get(i),randomList.get(i),randomList.get(i)));
        }
        startAnimation();
        position(NumSong);
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
                                    for(int j=0;j<8;j=j+1){
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
                                if(ban==false){
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
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                    Thread.currentThread().interrupt();
                } runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mode.equals("Top")){
                            ordre(mode,songList);
                        }
                        if(mode.equals("Random")){
                            int i=0;
                            List<Song> FinalList=new ArrayList<>();
                            while(i<8){
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
        int index=Integer.parseInt(repartition.get(r.nextInt(repartition.size())));
        return index;
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
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                        Thread.currentThread().interrupt();
                    } runOnUiThread(new Runnable() {
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
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                        Thread.currentThread().interrupt();
                    } runOnUiThread(new Runnable() {
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
                                } catch (InterruptedException ie) {
                                    ie.printStackTrace();
                                    Thread.currentThread().interrupt();
                                } runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        float X=250*density;
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
                                } catch (InterruptedException ie) {
                                    ie.printStackTrace();
                                    Thread.currentThread().interrupt();
                                } runOnUiThread(new Runnable() {
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
                                } catch (InterruptedException ie) {
                                    ie.printStackTrace();
                                    Thread.currentThread().interrupt();
                                } runOnUiThread(new Runnable() {
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
                                } catch (InterruptedException ie) {
                                    ie.printStackTrace();
                                    Thread.currentThread().interrupt();
                                }runOnUiThread(new Runnable() {
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
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
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
                    start.putExtra("activity","Game8");
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
                                add(vainqueur,db,10);
                            } else {
                                update(db,key,10+score);
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
        if(song.indexOf("Remix")>-1 || song.indexOf("(Live")>-1 || song.indexOf("Live)")>-1 || song.indexOf("Reanimation")>-1){
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
