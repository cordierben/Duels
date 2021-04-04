package fr.cordier.duels.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.cordier.duels.Class.Crypto;
import fr.cordier.duels.Class.DatabaseManager;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class Inscription extends AppCompatActivity {

    Button go;
    ImageView back,rgpd;
    EditText firstname,surname,email,username,password,password2,age;
    Spinner spinI;
    TextView erreur;
    boolean valide;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton google;
    private FirebaseAuth mAuth;
    CheckBox radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firstname=findViewById(R.id.editFirstname);
        surname=findViewById(R.id.editSurname);
        email=findViewById(R.id.editEmail);
        username=findViewById(R.id.editUsername);
        password=findViewById(R.id.editPassword);
        password2=findViewById(R.id.editConfirmPassword);
        age=findViewById(R.id.editAge);
        radio=findViewById(R.id.radiobutton);


        spinI= findViewById(R.id.spinneri);
        String[]locales = Locale.getISOCountries();
        ArrayList<String> liste=new ArrayList<>();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            liste.add(obj.getDisplayCountry());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, liste);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinI.setAdapter(adapter);


        //city=findViewById(R.id.editCity);
        erreur=findViewById(R.id.erreurInscription);

        back=findViewById(R.id.InscriptionToMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(start);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                finish();
            }
        });

        rgpd=findViewById(R.id.rgpd);
        rgpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), RGPD.class);
                startActivity(start);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                finish();
            }
        });

        //Connection classique
        go=findViewById(R.id.valide);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("*******","ok");
                valide();
            }
        });

        //Connetion via Google
        google=findViewById(R.id.sign_in_buttonInscription);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.clientId)).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
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
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("*******", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("*****", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            validationEmail(user.getEmail(),user.getDisplayName());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("****", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void validationEmail(String emailV,String name){
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
                                erreur.setText("This adress is already linked to an account. Please sign in.");
                            }
                            if(task.getResult().isEmpty()){
                                popup(emailV,name);
                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });

    }

    protected boolean valide(){
        valide=false;
        if(email.getText().toString().indexOf("@")!=-1){
            if(email.getText().toString().indexOf(".")!=-1){
                if(email.getText().toString().equals("")==false){
                    if(username.getText().toString().equals("")==false){
                        if(spinI.getSelectedItem().toString().equals("")==false){
                            if(radio.isChecked()){
                                if(password.getText().toString().equals(password2.getText().toString())){
                                    if(password.getText().toString().length()>=4){
                                        FirebaseFirestore ff=FirebaseFirestore.getInstance();
                                        ff.collection("Users")
                                                .whereEqualTo("Email",email.getText().toString())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        Log.i("*******","onComplete invoked");
                                                        if (task.isSuccessful()) {
                                                            int i=0;
                                                            for (DocumentSnapshot doc : task.getResult()) {
                                                                i=i+1;
                                                            }
                                                            if(i==0){
                                                                add();
                                                                valide=true;
                                                            } else erreur.setText("An account already exists with this mail adress");
                                                        } else {
                                                            Log.i("******","REQUEST ERROR");
                                                        }
                                                    }
                                                });
                                    } else erreur.setText("Password must be at least 4 characters long");
                                } else erreur.setText("Passwords are differents");
                            } else erreur.setText("You must accept the conditions");
                        } else erreur.setText("Country can't be empty");
                    } else erreur.setText("Username can't be empty");
                } else erreur.setText("Email can't be empty");
            } else erreur.setText("Email must be in the right format");
        } else erreur.setText("Email must be in the right format");
        return valide;
    }

    protected void add(){
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        Map<String,Object> ranking=new HashMap<>();
        Crypto crypto=new Crypto();
        String pass=crypto.getMd5(password.getText().toString());
        ranking.put("Firstname",firstname.getText().toString());
        ranking.put("Surname",surname.getText().toString());
        ranking.put("Email",email.getText().toString());
        ranking.put("Age",age.getText().toString());
        ranking.put("Username",username.getText().toString());
        ranking.put("Password",pass);
        ranking.put("Country",spinI.getSelectedItem().toString());
        ranking.put("Image","user.png");
        // Add a new document with a generated ID
        ff.collection("Users")
                .add(ranking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("******", "DocumentSnapshot added with ID: " + documentReference.getId());
                        DatabaseManager db=new DatabaseManager(getApplicationContext());
                        db.insertUser(1,email.getText().toString(),pass);
                        Intent start = new Intent(getApplicationContext(), Menu.class);
                        start.putExtra("Email",email.getText().toString());
                        startActivity(start);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("******", "Error adding document", e);
                    }
                });
    }

    protected void popup(String Email, String Name){
        // Creating alert Dialog with one Button
        AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.this);
        // Get the layout inflater
        LayoutInflater inflater = Inscription.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialoginscription, null);
        builder.setView(dialogView);
        final EditText usernamed=dialogView.findViewById(R.id.usernamed);
        final EditText passwordd=dialogView.findViewById(R.id.passwordd);
        final EditText aged=dialogView.findViewById(R.id.age);
        final CheckBox radio2=dialogView.findViewById(R.id.radiobutton2);

        Spinner spin= dialogView.findViewById(R.id.spinnerd);
        String[]locales = Locale.getISOCountries();
        ArrayList<String> liste=new ArrayList<>();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            liste.add(obj.getDisplayCountry());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, liste);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);


        builder.setPositiveButton("Sign Up!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(radio2.isChecked()){
                    String[] info={usernamed.getText().toString(),passwordd.getText().toString(),spin.getSelectedItem().toString(),Email,Name,aged.getText().toString()};
                    addinfo(info);
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                        Intent start = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(start);
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    protected void addinfo(String[] infos){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Map<String,Object> ranking=new HashMap<>();
        Crypto crypto=new Crypto();
        String pass=crypto.getMd5(infos[1]);
        ranking.put("Email",infos[3]);
        ranking.put("Firstname",infos[4]);
        ranking.put("Surname","");
        ranking.put("Password",pass);
        ranking.put("Age",infos[5]);
        ranking.put("Username",infos[0]);
        ranking.put("Country",infos[2]);
        ranking.put("Image","user.png");
        // Add a new document with a generated ID
        db.collection("Users")
                .add(ranking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("******", "DocumentSnapshot added with ID: " + documentReference.getId());
                        DatabaseManager db=new DatabaseManager(getApplicationContext());
                        db.insertUser(1,infos[3],pass);
                        Intent start = new Intent(getApplicationContext(), Menu.class);
                        start.putExtra("Email",infos[3]);
                        startActivity(start);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("******", "Error adding document", e);
                    }
                });
    }

    protected void logout(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.clientId))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        if(mGoogleSignInClient!=null) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent start = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(start);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(start);
        finish();
    }
}
