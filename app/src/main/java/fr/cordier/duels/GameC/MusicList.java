package fr.cordier.duels.GameC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.cordier.duels.Class.Artist;
import fr.cordier.duels.Class.Song;
import fr.cordier.duels.Game.GroupList;
import fr.cordier.duels.Game.start;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class MusicList extends AppCompatActivity {

    ImageView Back;
    LinearLayout listG;
    EditText search;
    ImageView loupe;
    String Email;
    List<Song> tracks=new ArrayList<>();
    GridLayout choices;
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.activity_music_list);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

        //Recuperation données
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");

        Back=(ImageView) findViewById(R.id.MusicListToGroupList);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                finish();
            }
        });

        listG=(LinearLayout) findViewById(R.id.listMusic);
        search=(EditText) findViewById(R.id.searchMusic);
        loupe=(ImageView) findViewById(R.id.loupeMusic);
        choices=(GridLayout) findViewById(R.id.listMusicChoice);

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
                String nameL=String.valueOf(search.getText());
                FindMusic(nameL);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameL=String.valueOf(search.getText());
                FindMusic(nameL);
            }
        });

        go=(Button) findViewById(R.id.customGo);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tracks.size()==8){
                    Intent start=new Intent(getApplicationContext(), Game8C.class);
                    start.putExtra("Email",Email);
                    start.putExtra("nbre",String.valueOf(tracks.size()));
                    for(int i=0;i<tracks.size();i=i+1){
                        Log.i("******",tracks.get(i).getTitle());
                        start.putExtra("TrackName "+i,tracks.get(i).getTitle());
                        start.putExtra("TrackImage "+i,tracks.get(i).getImage());
                        start.putExtra("TrackPreview "+i,tracks.get(i).getPreview());
                        startActivity(start);
                        finish();
                    }
                }
                else if(tracks.size()==16){
                    Intent start=new Intent(getApplicationContext(), Game16C.class);
                    start.putExtra("Email",Email);
                    start.putExtra("nbre",String.valueOf(tracks.size()));
                    for(int i=0;i<tracks.size();i=i+1){
                        Log.i("******",tracks.get(i).getTitle());
                        start.putExtra("TrackName "+i,tracks.get(i).getTitle());
                        start.putExtra("TrackImage "+i,tracks.get(i).getImage());
                        start.putExtra("TrackPreview "+i,tracks.get(i).getPreview());
                        startActivity(start);
                        finish();
                    }
                }
                else if(tracks.size()==32){
                    Intent start=new Intent(getApplicationContext(), Game32C.class);
                    start.putExtra("Email",Email);
                    start.putExtra("nbre",String.valueOf(tracks.size()));
                    for(int i=0;i<tracks.size();i=i+1){
                        Log.i("******",tracks.get(i).getTitle());
                        start.putExtra("TrackName "+i,tracks.get(i).getTitle());
                        start.putExtra("TrackImage "+i,tracks.get(i).getImage());
                        start.putExtra("TrackPreview "+i,tracks.get(i).getPreview());
                        startActivity(start);
                        finish();
                    }
                }
                else{
                    Toast toast= Toast.makeText(getBaseContext(), "You must have 8, 16 or 32 songs. You have "+tracks.size(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    private void FindMusic(String music){
        List<Song> data=new ArrayList<Song>();
        if(!music.equals("")){
            DeezerConnect deezerConnect = new DeezerConnect("423942");
            String[] permissions = new String[]{Permissions.BASIC_ACCESS, Permissions.MANAGE_LIBRARY, Permissions.LISTENING_HISTORY};

            RequestListener listener = new JsonRequestListener() {

                public void onResult(Object result, Object requestId) {
                    List<Track> musics= (List<Track>) result;
                    for(int i=0;i<musics.size();i=i+1){
                        if(musics.get(i).getRank()>10000){
                            Song objet=new Song(musics.get(i).getShortTitle(),musics.get(i).getArtist().getName(),musics.get(i).getAlbum().getMediumImageUrl(),musics.get(i).getPreviewUrl(),musics.get(i).getId());
                            data.add(objet);
                        }
                    }
                    liste(data);
                }
                public void onUnparsedResult(String requestResponse, Object requestId) {

                }
                public void onException(Exception e, Object requestId) {

                }
            };
            // create the request
            DeezerRequest request = DeezerRequestFactory.requestSearchTracks(music);
            // set a requestId, that will be passed on the listener's callback methods
            request.setId("myRequest");
            // launch the request asynchronously
            deezerConnect.requestAsync(request, listener);
        } else {
            listG.removeAllViews();
        }
    }

    public void liste(List<Song> musics){
        listG.removeAllViews();
        for(int i=0;i<musics.size();i=i+1){
            boolean present=false;
            for(int j=0;j<tracks.size();j=j+1){
                if(tracks.get(j).getId()==musics.get(i).getId()) present=true;
            }
            ImageView im=new ImageView(getApplicationContext());
            //Layout
            LinearLayout linear = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(0,10,0,10);
            linear.setPadding(5,5,5,5);
            linear.setLayoutParams(param);
            linear.setOrientation(LinearLayout.HORIZONTAL);
            if(present==true) linear.setBackgroundColor(Color.parseColor("#3AD51F"));

            //Image album
            Picasso.get().load(musics.get(i).getImage()).into(im);
            linear.addView(im);

            //Texte morceau
            TextView txt = new TextView(getApplicationContext());
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            txt.setTextSize(18);
            LinearLayout.LayoutParams paramtxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            txt.setLayoutParams(paramtxt);
            txt.setText(musics.get(i).getTitle()+"\n"+musics.get(i).getArtist());
            linear.addView(txt);

            listG.addView(linear);

            final boolean presentF=present;
            final int id=i;

            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(presentF==false){
                        Log.i("*****","add");
                        linear.setBackgroundColor(Color.parseColor("#3AD51F"));
                        tracks.add(musics.get(id));
                        choice();
                        if(tracks.size()==8||tracks.size()==16||tracks.size()==32){
                            go.setBackgroundColor(Color.parseColor("#7C23F0"));
                        } else{
                            go.setBackgroundColor(Color.parseColor("#EEAAFA"));
                            go.setAlpha(0.6f);
                        }
                        FindMusic(String.valueOf(search.getText()));
                    } else {
                        Log.i("*****","delete");
                        linear.setBackgroundColor(Color.TRANSPARENT);
                        for(int i=0;i<tracks.size();i=i+1){
                            if(tracks.get(i).getId()==musics.get(id).getId()) tracks.remove(i);
                        }
                        choice();
                        if(tracks.size()==8||tracks.size()==16||tracks.size()==32){
                            go.setBackgroundColor(Color.parseColor("#7C23F0"));
                        } else{
                            go.setBackgroundColor(Color.parseColor("#EEAAFA"));
                            go.setAlpha(0.6f);
                        }
                        FindMusic(String.valueOf(search.getText()));
                    }

                }
            });

        }
    }

    public void choice(){
        final float density = getResources().getDisplayMetrics().density;
        choices.removeAllViews();
        for(Song element:tracks){

            LinearLayout linear = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(0,10,0,10);
            linear.setPadding(5,5,5,5);
            linear.setLayoutParams(param);
            linear.setOrientation(LinearLayout.VERTICAL);

            TextView txt = new TextView(getApplicationContext());
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            txt.setTextSize(15);
            LinearLayout.LayoutParams paramtxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            txt.setLayoutParams(paramtxt);
            txt.setText("\n"+element.getTitle());
            linear.addView(txt);

            ImageView im=new ImageView(getApplicationContext());
            Picasso.get().load(element.getImage()).into(im);
            int taille=(int)density*50;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(taille,taille);
            params.gravity=Gravity.CENTER;
            im.setLayoutParams(params);
            linear.addView(im);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<tracks.size();i=i+1){
                        if(tracks.get(i).getId()==element.getId()) tracks.remove(i);
                    }
                    if(tracks.size()==8||tracks.size()==16||tracks.size()==32){
                        go.setBackgroundColor(Color.parseColor("#7C23F0"));
                    } else{
                        go.setBackgroundColor(Color.parseColor("#EEAAFA"));
                        go.setAlpha(0.6f);
                    }
                    choice();
                }
            });
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<tracks.size();i=i+1){
                        if(tracks.get(i).getId()==element.getId()) tracks.remove(i);
                    }
                    if(tracks.size()==8||tracks.size()==16||tracks.size()==32){
                        go.setBackgroundColor(Color.parseColor("#7C23F0"));
                    } else{
                        go.setBackgroundColor(Color.parseColor("#EEAAFA"));
                        go.setAlpha(0.6f);
                    }
                    choice();
                }
            });

            choices.addView(linear);
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
