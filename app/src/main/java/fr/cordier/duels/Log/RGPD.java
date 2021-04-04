package fr.cordier.duels.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import fr.cordier.duels.R;

public class RGPD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgpd);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView back=findViewById(R.id.rgpdtosignup);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Inscription.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                startActivity(start);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), Inscription.class);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
        startActivity(start);
        finish();
    }
}
