package fr.cordier.duels.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import fr.cordier.duels.Class.CircleTransform;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class Info extends AppCompatActivity {

    TextView username;
    TextView birth;
    TextView emailInfo;
    TextView back;
    Button password;
    Button ranking;
    ImageView pp,plus;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    String key="";
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout=findViewById(R.id.layoutI);
        AnimationDrawable animation= (AnimationDrawable) constraintlayout.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4000);
        animation.start();

        //Recup data
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");
        Log.i("*****",Email);

        //Init Widget
        pp=findViewById(R.id.pp);
        plus=findViewById(R.id.plus);
        username=(TextView) findViewById(R.id.usernameInfo);
        birth=(TextView) findViewById(R.id.birthInfo);
        emailInfo=(TextView) findViewById(R.id.emailInfo);

        password=(Button) findViewById(R.id.passwordChange);
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

        ranking=(Button) findViewById(R.id.previousRankings);
        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), UserRankings.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down );
                finish();
            }
        });

        back=findViewById(R.id.InfoToMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email",Email);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up);
                finish();
            }
        });

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("Users")
                .whereEqualTo("Email",Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                username.setText(doc.getString("Username"));
                                emailInfo.setText("Email : "+doc.getString("Email"));
                                birth.setText("Name : "+doc.getString("Firstname")+" "+doc.getString("Surname"));
                                FirebaseStorage storage=FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference();
                                storageRef.child("images/"+doc.getString("Image")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.i("*****",uri.toString());
                                        Picasso.get().load(uri).transform(new CircleTransform()).into(pp);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                                key=doc.getId();
                            }
                        }
                    }
                });
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pp.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String code=UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ code);
            Log.i("****",ref.toString());
            store(code);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Info.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            StorageReference storageRef=storage.getReference();
                            storageRef.child("images/"+code).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i("*****",uri.toString());
                                    Picasso.get().load(uri).transform(new CircleTransform()).into(pp);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Info.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void store(String code){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Users").document(key);
        ref.update("Image", code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("*****", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("*****", "Error updating document", e);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start = new Intent(getApplicationContext(), GenreList.class);
        start.putExtra("Email",Email);
        startActivity(start);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}
