package fr.cordier.duels.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
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
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
import fr.cordier.duels.Class.RoundedTransform;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;
import jp.wasabeef.blurry.Blurry;

public class GroupList extends AppCompatActivity {


    ImageView Back;
    GridLayout grille;
    ScrollView listG;
    EditText search;
    ImageView loupe;
    String Email;
    WaveSideBar sidebar;
    String[] indexTab={"1","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    int[] pos=new int[27];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        for(int i=0;i<27;i=i+1)pos[i]=0;

        //Recuperation données
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");

        //Initialisation Widget
        Back=(ImageView) findViewById(R.id.back);
        Back.setOnClickListener(v -> {
            Intent start=new Intent(getApplicationContext(), Menu.class);
            start.putExtra("Email",Email);
            startActivity(start);
            finish();
        });

        listG=(ScrollView) findViewById(R.id.listG);
        grille=(GridLayout) findViewById(R.id.gridArtist);
        search=(EditText) findViewById(R.id.search);
        loupe=(ImageView) findViewById(R.id.loupe);
        sidebar=findViewById(R.id.side_bar);
        sidebar.setIndexItems("1","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");

        sidebar.setOnSelectIndexItemListener(index -> {
            for(int i=0;i<indexTab.length;i=i+1){
                if(index.equals(indexTab[i])){
                    listG.scrollTo(0,pos[i]);
                }
            }
        });


        //Récupération artistes
        SearchArtist();
        search.setOnFocusChangeListener((view, b) -> search.setText(""));

        loupe.setOnClickListener(v -> {
            String nameL=String.valueOf(search.getText());
            FindArtist(nameL);
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

            String ligne;
            while ((ligne = bufferedReader.readLine()) != null) {
                ligne = bufferedReader.readLine();
                if(ligne!=null){
                    String[] values=ligne.split(";");
                    Artist artiste=new Artist(values[0],values[1],values[2]);
                    data.add(artiste);
                }
            }
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

                String ligne;
                while (bufferedReader.readLine() != null) {
                    ligne = bufferedReader.readLine();
                    if(ligne!=null){
                        String[] values=ligne.split(";");
                        if(values[0].toLowerCase().contains(artist.toLowerCase())){
                            Artist artiste=new Artist(values[0],values[1],values[2]);
                            data.add(artiste);
                        }
                    }
                }
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
        final int density = Math.round(getResources().getDisplayMetrics().density);

        for(int i=0;i<artist.size();i=i+1){
            final int id=i;

            RelativeLayout rLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rLayout.setGravity(Gravity.CENTER);
            rLayout.setLayoutParams(rlParams);

            ImageView im= new ImageView(this);
            RelativeLayout.LayoutParams imParams = new RelativeLayout.LayoutParams(density*130,density*130);
            im.setLayoutParams(imParams);
            Picasso.get().load(artist.get(i).getImage()).transform(new RoundedTransform()).into(im);
            im.setPadding(10,10,10,10);
            imParams.setMargins(density*10,0,density*10,0);
            rLayout.addView(im);

            RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            tParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            TextView txt=new TextView(this);
            String name=artist.get(i).getName();
            if(name.length()>15) name=name.replaceFirst(" ","\n");
            txt.setTextColor(Color.WHITE);
            txt.setTypeface(Typeface.DEFAULT_BOLD);
            txt.setText(name);
            txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txt.setTextSize(18);
            txt.setLayoutParams(tParams);
            rLayout.addView(txt);

            grille.addView(rLayout);

            im.setOnClickListener(v -> {
                Intent start = new Intent(getApplicationContext(), fr.cordier.duels.Game.start.class);
                start.putExtra("Email",Email);
                start.putExtra("NomArtiste", String.valueOf(txt.getText()).replace("\n"," "));
                start.putExtra("IdArtiste", String.valueOf(artist.get(id).getId()));
                startActivity(start);
                finish();
            });
            txt.setOnClickListener(v -> {
                Intent start = new Intent(getApplicationContext(), start.class);
                start.putExtra("Email",Email);
                start.putExtra("NomArtiste", String.valueOf(txt.getText()));
                start.putExtra("IdArtiste", String.valueOf(artist.get(id).getId()));
                startActivity(start);
                finish();
            });

            posScroll(artist.get(i).getName());
        }
        for(int i=0;i<10;i=i+1){
            TextView txt=new TextView(getApplicationContext());
            grille.addView(txt);
        }
    }

    protected void posScroll(String name){
        String first=name.substring(0,1);
        for(int i=1;i<indexTab.length-1;i=i+1){
            if(first.equals(indexTab[i])){
                for(int j=i;j<indexTab.length-1;j=j+1){
                    pos[j+1]+=98;
                }
            }
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