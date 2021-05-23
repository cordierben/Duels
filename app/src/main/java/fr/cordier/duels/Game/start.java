package fr.cordier.duels.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import fr.cordier.duels.Game.Game;
import fr.cordier.duels.Game.Game16;
import fr.cordier.duels.Game.Game32;
import fr.cordier.duels.Game.GroupList;
import fr.cordier.duels.R;

public class start extends AppCompatActivity {

    TextView artiste;
    Button random;
    Button top;
    TextView t8;
    TextView t16;
    TextView t32;
    SeekBar bar;
    Button go;
    TextView nombre;
    ImageView Back;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutstart);

        //Récup donnée
        Intent intent=getIntent();
        artiste=(TextView) findViewById(R.id.artiste);
        final String nomArtiste=intent.getStringExtra("NomArtiste");
        Email=intent.getStringExtra("Email");
        Log.i("*****",Email);
        final String IdArtiste=intent.getStringExtra("IdArtiste");
        //final String genre=intent.getStringExtra("Genre");
        artiste.setText(nomArtiste);

        initialisation();

        random.setOnClickListener(v -> {
            visible();
            go.setOnClickListener(v1 -> {
                int pos=bar.getProgress();
                if(pos==0){
                    Intent start=new Intent(getApplicationContext(), Game.class);
                    start.putExtra("Mode","Random");
                    start.putExtra("Artiste",IdArtiste);
                    start.putExtra("NomArtiste",nomArtiste);
                    start.putExtra("Etat","Start");
                    start.putExtra("Email",Email);
                    startActivity(start);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
                if(pos==1){
                    Intent start=new Intent(getApplicationContext(), Game16.class);
                    start.putExtra("Mode","Random");
                    start.putExtra("Artiste",IdArtiste);
                    start.putExtra("NomArtiste",nomArtiste);
                    start.putExtra("Etat","Start");
                    start.putExtra("Email",Email);
                    startActivity(start);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
                if(pos==2){
                    Intent start=new Intent(getApplicationContext(), Game32.class);
                    start.putExtra("Mode","Random");
                    start.putExtra("Artiste",IdArtiste);
                    start.putExtra("NomArtiste",nomArtiste);
                    start.putExtra("Etat","Start");
                    start.putExtra("Email",Email);
                    startActivity(start);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            });
        });
        top.setOnClickListener(v -> {
            visible();
            go.setOnClickListener(v12 -> {
                int pos=bar.getProgress();
                if(pos==0){
                    Intent start=new Intent(getApplicationContext(),Game.class);
                    start.putExtra("Mode","Top");
                    start.putExtra("Artiste",IdArtiste);
                    start.putExtra("NomArtiste",nomArtiste);
                    start.putExtra("Etat","Start");
                    start.putExtra("Email",Email);
                    startActivity(start);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
                if(pos==1){
                    Intent start=new Intent(getApplicationContext(),Game16.class);
                    start.putExtra("Mode","Top");
                    start.putExtra("Artiste",IdArtiste);
                    start.putExtra("NomArtiste",nomArtiste);
                    start.putExtra("Etat","Start");
                    start.putExtra("Email",Email);
                    startActivity(start);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
                if(pos==2){
                    Intent start=new Intent(getApplicationContext(),Game32.class);
                    start.putExtra("Mode","Top");
                    start.putExtra("Artiste",IdArtiste);
                    start.putExtra("NomArtiste",nomArtiste);
                    start.putExtra("Etat","Start");
                    start.putExtra("Email",Email);
                    startActivity(start);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }

            });
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), GroupList.class);
                //start.putExtra("Genre",genre);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
                finish();
            }
        });
    }

    protected void initialisation(){
        //Initialisation attributes
        random=(Button) findViewById(R.id.randomB);
        top=findViewById(R.id.topB);
        t8=(TextView) findViewById(R.id.t8);
        t16=(TextView) findViewById(R.id.t16);
        t32=(TextView) findViewById(R.id.t32);
        bar=(SeekBar) findViewById(R.id.seekbar);
        go=(Button) findViewById(R.id.go);
        nombre=(TextView) findViewById(R.id.ntitre);
        Back=(ImageView) findViewById(R.id.backstart);

        //Widget invisible
        t8.setVisibility(View.GONE);
        t16.setVisibility(View.GONE);
        t32.setVisibility(View.GONE);
        bar.setVisibility(View.GONE);
        go.setVisibility(View.GONE);
        nombre.setVisibility(View.GONE);
    }

    protected void visible(){
        t8.setVisibility(View.VISIBLE);
        t16.setVisibility(View.VISIBLE);
        t32.setVisibility(View.VISIBLE);
        bar.setVisibility(View.VISIBLE);
        go.setVisibility(View.VISIBLE);
        nombre.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start = new Intent(getApplicationContext(), GroupList.class);
        //start.putExtra("Genre",genre);
        start.putExtra("Email",Email);
        startActivity(start);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
        finish();
    }
}
