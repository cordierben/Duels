package fr.cordier.duels.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

import fr.cordier.duels.Class.DatabaseManager;
import fr.cordier.duels.Log.MainActivity;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;
import fr.cordier.duels.User.Password;

public class Settings extends AppCompatActivity {

    Button disconnect;
    GoogleSignInClient mGoogleSignInClient;
    Button password,country,delete;
    String Email;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutSettings);

        Intent start=getIntent();
        Email=start.getStringExtra("Email");
        back=findViewById(R.id.SettingsToMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
                startActivity(start);
                finish();
            }
        });

        disconnect=findViewById(R.id.SignOut);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        password=findViewById(R.id.passwordChangeSettings);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Password.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down );
                finish();
            }
        });

        country=findViewById(R.id.countryChangeSettings);
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating alert Dialog with one Button
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                // Get the layout inflater
                LayoutInflater inflater = Settings.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialogsettings, null);
                builder.setView(dialogView);

                Spinner spin= dialogView.findViewById(R.id.spinnerd);
                String[]locales = Locale.getISOCountries();
                ArrayList<String> liste=new ArrayList<>();
                for (String countryCode : locales) {
                    Locale obj = new Locale("", countryCode);
                    liste.add(obj.getDisplayCountry());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, liste);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);


                builder.setPositiveButton("Change!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseFirestore ff=FirebaseFirestore.getInstance();
                        ff.collection("Users")
                                .whereEqualTo("Email",Email)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot doc : task.getResult()) {
                                                DocumentReference document=ff.collection("Users").document(doc.getId());
                                                document.update("Country",spin.getSelectedItem().toString())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("******", "DocumentSnapshot successfully updated!");
                                                                Toast.makeText(getApplicationContext(), "Country Changed", Toast.LENGTH_LONG).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("******", "Error updating document", e);
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent start = new Intent(getApplicationContext(), GenreList.class);
                        start.putExtra("Email",Email);
                        startActivity(start);
                        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up );
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        delete=findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });

    }

    private void signOut() {
        //Log.i("****",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        /*if(FirebaseAuth.getInstance()!=null) {
            Log.i("********","ici");
            FirebaseAuth.getInstance().signOut();
            Intent start = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(start);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left );
            finish();
        } else {
            DatabaseManager db=new DatabaseManager(this);
            db.updateUser(0,Email);
            Intent start = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(start);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left );
            finish();
        }*/
        DatabaseManager db=new DatabaseManager(this);
        db.updateUser(0,Email);
        Intent start = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(start);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left );
        finish();
    }

    private void deleteAccount(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("Email",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                deleteFromFriends();
                                deleteFromRanking();
                                db.collection("Users").document(doc.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("******", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("******", "Error deleting document", e);
                                            }
                                        });
                                DatabaseManager db=new DatabaseManager(getApplicationContext());
                                db.DeleteUser(Email);
                                Intent start=new Intent(getApplicationContext(), MainActivity.class);
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                                startActivity(start);
                                finish();
                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });
    }

    private void deleteFromFriends(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Friends")
                .whereEqualTo("Email1",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                db.collection("Friends").document(doc.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("******", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("******", "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });
        db.collection("Friends")
                .whereEqualTo("Email2",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                db.collection("Friends").document(doc.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("******", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("******", "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });
    }

    private void deleteFromRanking(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Ranking")
                .whereEqualTo("User",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                db.collection("Ranking").document(doc.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("******", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("******", "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.i("******","REQUEST ERROR");
                        }
                    }
                });
    }

    private void popup(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Settings.this);

        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Delete Account");

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to delete your account?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        deleteAccount();
                        Toast.makeText(getApplicationContext(),"Account deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Toast.makeText(getApplicationContext(),"Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start=new Intent(getApplicationContext(), GenreList.class);
        start.putExtra("Email",Email);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
        startActivity(start);
        finish();
    }
}
