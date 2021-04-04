package fr.cordier.duels.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import fr.cordier.duels.Class.Crypto;
import fr.cordier.duels.Class.DatabaseManager;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;


public class Connect extends AppCompatActivity {

    Toast errorToast;
    Button connect;
    EditText mail,mdp;
    TextView erreur;
    String Email="";
    GoogleSignInClient mGoogleSignInClient;
    SignInButton google;
    ImageView back;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
/*
        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutConnect);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();*/

        //Initialisation Widget
        connect=(Button) findViewById(R.id.ConnectToMenu);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mail.getText().toString();
                String password=mdp.getText().toString();
                valide(email,password);
            }
        });

        back=(ImageView) findViewById(R.id.ConnectToLoading);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                startActivity(start);
                finish();
            }
        });
        mail=(EditText) findViewById(R.id.mail);
        mdp=(EditText) findViewById(R.id.mdp);
        erreur=(TextView) findViewById(R.id.erreur);

        //Connetion via Google
        google=findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.clientId))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //if(currentUser!=null) validation(currentUser.getEmail());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("*******", "firebaseAuthWithGoogle:" + account.getId()+" "+account.getEmail());
                firebaseAuthWithGoogle(account.getIdToken());
                //validation(account.getEmail());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("*******", "Google sign in failed", e);
                errorToast = Toast.makeText(Connect.this, e+" ", Toast.LENGTH_SHORT);
                errorToast.show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Log.i("*****",credential.toString());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("*****", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            validation(user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("****", "signInWithCredential:failure", task.getException());
                            errorToast = Toast.makeText(Connect.this, "Erreur firebaseAuth", Toast.LENGTH_SHORT);
                            errorToast.show();
                        }
                    }
                });
    }

    public void validation(String emailV){
        Log.i("*******","valide invoked");
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("Email",emailV)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Intent start = new Intent(getApplicationContext(), Menu.class);
                                start.putExtra("Email",emailV);
                                //Set current user to connected
                                DatabaseManager db=new DatabaseManager(getApplicationContext());
                                List<String> info=db.selectUserId(emailV);
                                if(info.size()>0) db.updateUser(1,emailV);
                                else db.insertUser(1,emailV,"");
                                startActivity(start);
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                                finish();
                            }
                            if(task.getResult().isEmpty()){
                                erreur.setText("This account doesn't exist. Please sign up.");
                                FirebaseAuth.getInstance().signOut();

                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });
    }

    public void valide(String emailV,String password){
        Log.i("*******","valide invoked");
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("Email",emailV)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Email=doc.getString("Email");
                                Crypto crypto=new Crypto();
                                String pass=crypto.getMd5(password);
                                if(pass.equals(doc.getString("Password"))){
                                    Intent start = new Intent(getApplicationContext(), Menu.class);
                                    //Set current user to connected
                                    DatabaseManager db=new DatabaseManager(getApplicationContext());
                                    List<String> info=db.selectUserId(emailV);
                                    if(info.size()>0) db.updateUser(1,emailV);
                                    else db.insertUser(1,emailV,pass);
                                    start.putExtra("Email",Email);
                                    startActivity(start);
                                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                                    finish();
                                } else {
                                    erreur.setText("Wrong password");
                                }
                            }
                            if(task.getResult().isEmpty()){
                                erreur.setText("This account doesn't exist. Please sign up.");
                                FirebaseAuth.getInstance().signOut();
                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(start);
        finish();
    }
}
