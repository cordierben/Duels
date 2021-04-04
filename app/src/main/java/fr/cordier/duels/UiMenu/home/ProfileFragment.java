package fr.cordier.duels.UiMenu.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.cordier.duels.Class.CircleTransform;
import fr.cordier.duels.Game.Rankings;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;
import fr.cordier.duels.User.FriendList;
import fr.cordier.duels.User.Info;
import fr.cordier.duels.User.Password;
import fr.cordier.duels.User.UserRankings;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
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
    Menu r;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        /*final TextView textView = root.findViewById(R.id.textView11);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        r=(Menu) getActivity();

        //Recup data
        Intent intent=r.getIntent();
        Email=intent.getStringExtra("Email");
        Log.i("*****",Email);

        //Init Widget
        pp=root.findViewById(R.id.pp);
        plus=root.findViewById(R.id.plus);
        username=(TextView) root.findViewById(R.id.usernameInfo);
        birth=(TextView) root.findViewById(R.id.birthInfo);
        emailInfo=(TextView) root.findViewById(R.id.emailInfo);

        password=(Button) root.findViewById(R.id.passwordChange);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(r.getApplicationContext(), Password.class);
                start.putExtra("Email",Email);
                startActivity(start);
                r.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down );
                r.finish();
            }
        });

        ranking=(Button) root.findViewById(R.id.previousRankings);
        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(r.getApplicationContext(), UserRankings.class);
                start.putExtra("Email",Email);
                startActivity(start);
                r.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down );
                r.finish();
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
        return root;
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == r.RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(r.getContentResolver(), filePath);
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
            final ProgressDialog progressDialog = new ProgressDialog(r.getApplicationContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String code= UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ code);
            Log.i("****",ref.toString());
            store(code);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(r.getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(r.getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
}