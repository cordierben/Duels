package fr.cordier.duels.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fr.cordier.duels.Class.Crypto;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class Password extends AppCompatActivity {

    EditText oldp;
    EditText newp;
    EditText newp2;
    Button confirm;
    String Email;
    String password;
    String key;
    TextView erreur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutPassword);

        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");

        oldp=(EditText) findViewById(R.id.oldPassword);
        newp=(EditText) findViewById(R.id.newPassword);
        newp2=(EditText) findViewById(R.id.newPassword2);
        confirm=(Button) findViewById(R.id.confirmPassword);
        erreur=(TextView) findViewById(R.id.erreurPassword);

        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("Users")
                .whereEqualTo("Email",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                password=doc.getString("Password");
                                key=doc.getId();
                            }
                        }
                    }
                });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crypto crypto=new Crypto();
                String oldPassword=crypto.getMd5(oldp.getText().toString());
                if(oldPassword.equals(password)){
                    if(newp.getText().toString().equals(newp2.getText().toString())){
                        if(newp.getText().toString().equals("")==false || newp.getText().toString().length()<=4){
                            erreur.setText("Password changed!");
                            String pass=crypto.getMd5(newp.getText().toString());
                            DocumentReference document=ff.collection("Users").document(key);
                            document.update("Password",pass)
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
                        } else {
                            erreur.setText("Password must be at least 4 characters long");
                        }
                    } else {
                        erreur.setText("New passwords don't match");
                    }
                } else {
                    erreur.setText("The old password is incorrect");
                }
            }
        });

        TextView back= findViewById(R.id.PasswordToInfo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up );
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start = new Intent(getApplicationContext(), Menu.class);
        start.putExtra("Email",Email);
        startActivity(start);
        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up );
        finish();
    }
}
