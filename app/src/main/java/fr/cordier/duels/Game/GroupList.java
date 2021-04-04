package fr.cordier.duels.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.cordier.duels.Class.Artist;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class GroupList extends AppCompatActivity {


    TextView Back;
    GridLayout grille;
    ScrollView listG;
    EditText search;
    ImageView loupe;
    String Email;
    ImageView mvt1,mvt2,mvt3,mvt4,mvt5,mvt6;
    WaveSideBar sidebar;
    String[] indexTab={"1","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
    int[] pos=new int[28];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutgroup);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

        for(int i=0;i<28;i=i+1)pos[i]=0;

        //Recuperation données
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");

        //Initialisation Widget
        Back=(TextView) findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                finish();
            }
        });

        listG=(ScrollView) findViewById(R.id.listG);
        grille=(GridLayout) findViewById(R.id.gridArtist);
        search=(EditText) findViewById(R.id.search);
        loupe=(ImageView) findViewById(R.id.loupe);
        sidebar=findViewById(R.id.side_bar);
        sidebar.setIndexItems("1","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#");
        sidebar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for(int i=0;i<indexTab.length;i=i+1){
                    Log.i("****",pos[i]+" "+indexTab[i]);
                    if(index.equals(indexTab[i])){
                        listG.scrollTo(0,pos[i]);
                    }
                }
                if(index.equals("1")) grille.scrollTo(0,0);

            }
        });


        //Récupération artistes
        SearchArtist();
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                search.setText("");
            }
        });

        loupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameL=String.valueOf(search.getText());
                FindArtist(nameL);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameL=String.valueOf(search.getText());
                FindArtist(nameL);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void SearchArtist(){
        Log.i("******","test");
        List<Artist> data=new ArrayList<Artist>();
        try {
            FileInputStream fileInputStream = openFileInput("artist.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String ligne;
            while ((ligne = bufferedReader.readLine()) != null) {
                ligne = bufferedReader.readLine();
                if(ligne!=null){
                    String[] values=ligne.split(";");
                    Artist artiste=new Artist(values[0],values[1],values[2]);
                    data.add(artiste);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        liste(data);
    }

    private void FindArtist(String artist){
        if(!artist.equals("")){
            List<Artist> data=new ArrayList<Artist>();
            try {
                FileInputStream fileInputStream = openFileInput("artist.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();

                String ligne;
                while ((ligne = bufferedReader.readLine()) != null) {
                    ligne = bufferedReader.readLine();
                    if(ligne!=null){
                        String[] values=ligne.split(";");
                        if(values[0].toLowerCase().contains(artist.toLowerCase())){
                            Artist artiste=new Artist(values[0],values[1],values[2]);
                            data.add(artiste);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(data.size()==0){
                DeezerConnect deezerConnect = new DeezerConnect("423942");
                String[] permissions = new String[]{Permissions.BASIC_ACCESS, Permissions.MANAGE_LIBRARY, Permissions.LISTENING_HISTORY};

                //Requete Deezer pour insertion en boucle
                RequestListener listener = new JsonRequestListener() {

                    public void onResult(Object result, Object requestId) {
                        List<com.deezer.sdk.model.Artist> artiste= (List<com.deezer.sdk.model.Artist>) result;
                        for(int i=0;i<artiste.size();i=i+1){
                            if(artiste.get(i).getNbFans()>3000){
                                Artist objet=new Artist(artiste.get(i).getName(),artiste.get(i).getMediumImageUrl(),String.valueOf(artiste.get(i).getId()));
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
                DeezerRequest request = DeezerRequestFactory.requestSearchArtists(artist);
                // set a requestId, that will be passed on the listener's callback methods
                request.setId("myRequest");
                // launch the request asynchronously
                deezerConnect.requestAsync(request, listener);
            } else{
                liste(data);
            }
        } else {
            SearchArtist();
        }
    }

    public void liste(List<Artist> artist){
        grille.removeAllViews();
        for(int i=0;i<artist.size();i=i+1){
            final int id=i;
            ImageView im=new ImageView(getApplicationContext());
            Picasso.get().load(artist.get(i).getImage()).into(im);
            grille.addView(im);

            TextView txt=new TextView(getApplicationContext());
            ViewGroup.LayoutParams paramtxt=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txt.setLayoutParams(paramtxt);
            String name=artist.get(i).getName();
            if(name.length()>15){
                name=name.replaceFirst(" ","\n");
            }
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            txt.setText(name);

            txt.setTextSize(25);
            grille.addView(txt);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(txt.getLayoutParams());
            params.setGravity(Gravity.CENTER_VERTICAL);
            txt.setLayoutParams(params);

            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent start = new Intent(getApplicationContext(), fr.cordier.duels.Game.start.class);
                    start.putExtra("Email",Email);
                    start.putExtra("NomArtiste", String.valueOf(txt.getText()).replace("\n"," "));
                    start.putExtra("IdArtiste", String.valueOf(artist.get(id).getId()));
                    startActivity(start);
                    finish();
                }
            });
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent start = new Intent(getApplicationContext(), start.class);
                    start.putExtra("Email",Email);
                    start.putExtra("NomArtiste", String.valueOf(txt.getText()));
                    start.putExtra("IdArtiste", String.valueOf(artist.get(id).getId()));
                    startActivity(start);
                    finish();
                }
            });

            posScroll(artist.get(i).getName());
        }
        for(int i=1;i<pos.length;i=i+1){
            pos[i]=pos[i-1]+pos[i];
        }
        for(int i=0;i<10;i=i+1){
            TextView txt=new TextView(getApplicationContext());
            grille.addView(txt);
        }
    }

    protected void posScroll(String name){
        String first=name.substring(0,1);
        for(int i=0;i<indexTab.length;i=i+1){
            if(first.equals(indexTab[i])){
                pos[i+1]=pos[i+1]+266;
            }
        }
        try {
            Integer.parseInt(first);
            pos[1]=pos[1]+266;
        } catch (NumberFormatException nfe) {

        }
    }

    private void anim2(int a){
        final float density = getResources().getDisplayMetrics().density;
        mvt1=findViewById(R.id.mvt1);
        mvt1.setX(420*density);
        mvt1.setY(220*density);
        mvt2=findViewById(R.id.mvt2);
        mvt2.setX(420*density);
        mvt2.setY(350*density);
        mvt3=findViewById(R.id.mvt3);
        mvt3.setX(420*density);
        mvt3.setY(500*density);
        mvt4=findViewById(R.id.mvt4);
        mvt4.setX(-40*density);
        mvt4.setY(250*density);
        mvt5=findViewById(R.id.mvt5);
        mvt5.setX(200*density);
        mvt5.setY(750*density);
        mvt6=findViewById(R.id.mvt6);
        mvt6.setX(230*density);
        mvt6.setY(-40*density);
        ImageView[] mvt={mvt1,mvt4,mvt3,mvt5,mvt2,mvt6};
        Animation anim1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.note_move_1);
        Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.note_move_2);
        Animation anim3= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.note_move_3);
        Animation anim4= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.note_move_4);
        Animation anim5= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.note_move_5);
        Animation anim6= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.note_move_6);
        Animation[] anim={anim1,anim4,anim3,anim5,anim2,anim6};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mvt[a].startAnimation(anim[a]);
                        if(a==5){
                            anim2(0);
                        }else{
                            anim2(a+1);
                        }
                    }
                });
            }
        }).start();

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