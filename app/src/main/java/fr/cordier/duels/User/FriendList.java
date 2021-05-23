package fr.cordier.duels.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.cordier.duels.Class.CircleTransform;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class FriendList extends AppCompatActivity {

    GridLayout liste;
    EditText Artist;
    ImageView loupe;
    ScrollView liste2;
    LinearLayout rankingLayout;
    TextView back;
    TextView txtFriendRanking;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animated background
        ConstraintLayout constraintlayout = findViewById(R.id.layoutFriend);

        //Récup data
        Intent intent = getIntent();
        Email = intent.getStringExtra("Email");
        liste = findViewById(R.id.friendsList);
        liste2=(ScrollView) findViewById(R.id.friendsListRanking);
        rankingLayout=(LinearLayout) findViewById(R.id.FriendRankingLayout);
        loupe=(ImageView) findViewById(R.id.loupef);
        loupe.setVisibility(View.GONE);
        Artist=(EditText) findViewById(R.id.searchf);
        Artist.setVisibility(View.GONE);
        back = (TextView) findViewById(R.id.FriendsToProfile);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(getApplicationContext(), Menu.class);
                start.putExtra("Email", Email);
                startActivity(start);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        selectEmail1();
    }

    protected void selectUser(String mail,FirebaseFirestore ff){
        ff.collection("Users")
                .whereEqualTo("Email",mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******", "onComplete invoked");
                        Log.i("*******","cc3");
                        if (task.isSuccessful()) {
                            int i=0;
                            for (DocumentSnapshot doc : task.getResult()) {
                                appendFriend(doc,i);
                                i=i+1;
                            }
                        } else {
                            Log.d("******", "ERROR QUERY RANKING");
                        }
                    }
                });
    }

    protected void selectEmail1(){
        List<String> FriendList=new ArrayList<>();
        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.collection("Friends")
                .whereEqualTo("Email1",Email)
                .whereEqualTo("State","Accepted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******", "onComplete invoked");
                        Log.i("*******","cc1");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                FriendList.add(doc.getString("Email2"));
                            }
                            selectEmail2(FriendList,ff);
                        } else {
                            Log.d("******", "ERROR QUERY RANKING");
                        }
                    }
                });
    }

    protected void selectEmail2(List<String> FriendList,FirebaseFirestore ff){
        ff.collection("Friends")
                .whereEqualTo("Email2",Email)
                .whereEqualTo("State","Accepted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******", "onComplete invoked");
                        Log.i("*******","cc2");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                    selectUser(doc.getString("Email1"),ff);
                            }
                            for(int i=0;i<FriendList.size();i=i+1){
                                selectUser(FriendList.get(i),ff);
                            }
                        } else {
                            Log.d("******", "ERROR QUERY RANKING");
                        }
                    }
                });
    }

    protected void appendFriend(DocumentSnapshot doc,int i){
        ImageView im=new ImageView(getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        im.setLayoutParams(params);
        im.getLayoutParams().height = 100;
        im.getLayoutParams().width = 100;
        im.requestLayout();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/"+doc.getString("Image")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("*******","cc5");
                Picasso.get().load(uri).transform(new CircleTransform()).into(im);
                liste.addView(im);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankingLayout.removeAllViews();
                        friendsRanking(doc.getString("Email"));
                    }
                });

                TextView txt=new TextView(getApplicationContext());
                txt.setTextColor(Color.parseColor("#FFFFFF"));
                txt.setText(doc.getString("Username"));
                txt.setTextSize(25);
                liste.addView(txt);
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankingLayout.removeAllViews();
                        friendsRanking(doc.getString("Email"));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                TextView txt=new TextView(getApplicationContext());
                txt.setTextColor(Color.parseColor("#FFFFFF"));
                txt.setText(doc.getString("Username"));
                txt.setTextSize(25);
                liste.addView(txt);
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendsRanking(doc.getString("Email"));
                    }
                });
            }
        });



    }

    protected void friendsRanking(String Email){
        loupe.setVisibility(View.VISIBLE);
        Artist.setVisibility(View.VISIBLE);
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("Ranking")
                .whereEqualTo("User",Email)
                .orderBy("Artist", Query.Direction.ASCENDING)
                .orderBy("Score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******", "onComplete invoked");
                        if (task.isSuccessful()) {
                            String art="";
                            for (DocumentSnapshot doc : task.getResult()) {
                                if(doc.getString("Artist").equals(art)==false){
                                    txtFriendRanking=new TextView(getApplicationContext());
                                    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    lp.setMargins(50,20,50,20);
                                    txtFriendRanking.setLayoutParams(lp);
                                    txtFriendRanking.setAlpha(0.8f);
                                    txtFriendRanking.setTextColor(Color.parseColor("#FFFFFF"));
                                    txtFriendRanking.setBackgroundResource(R.drawable.round_corner);
                                    txtFriendRanking.setBackgroundColor(Color.parseColor("#6F27E5"));
                                    txtFriendRanking.append(doc.getString("Artist")+":\n");
                                    rankingLayout.addView(txtFriendRanking);
                                    art=doc.getString("Artist");
                                }
                                txtFriendRanking.append(doc.getString("Music")+"\n");
                            }
                        } else {
                            Log.d("******", "ERROR QUERY RANKING");
                        }
                    }
                });
        Artist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Artist.setText("");
            }
        });
        loupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artiste=Artist.getText().toString();
                Log.i("*****","Good");
                rankingLayout.removeAllViews();
                if(artiste.equals("")){
                    ff.collection("Ranking")
                            .whereEqualTo("User",Email)
                            .orderBy("Artist", Query.Direction.ASCENDING)
                            .orderBy("Score", Query.Direction.DESCENDING)
                            .limit(5)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Log.i("*******", "onComplete invoked");
                                    if (task.isSuccessful()) {
                                        String art="";
                                        for (DocumentSnapshot doc : task.getResult()) {
                                            if(doc.getString("Artist").equals(art)==false){
                                                txtFriendRanking=new TextView(getApplicationContext());
                                                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                lp.setMargins(50,20,50,20);
                                                txtFriendRanking.setLayoutParams(lp);
                                                txtFriendRanking.setAlpha(0.8f);
                                                txtFriendRanking.setTextColor(Color.parseColor("#FFFFFF"));
                                                txtFriendRanking.setBackgroundResource(R.drawable.round_corner);
                                                txtFriendRanking.setBackgroundColor(Color.parseColor("#6F27E5"));
                                                txtFriendRanking.append(doc.getString("Artist")+":\n");
                                                rankingLayout.addView(txtFriendRanking);
                                                art=doc.getString("Artist");
                                            }
                                            txtFriendRanking.append(doc.getString("Music")+"\n");
                                        }
                                    } else {
                                        Log.d("******", "ERROR QUERY RANKING");
                                    }
                                }
                            });
                } else {
                    ff.collection("Ranking")
                            .whereEqualTo("User",Email)
                            .whereEqualTo("Artist",artiste)
                            .orderBy("Score", Query.Direction.DESCENDING)
                            .limit(10)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Log.i("*******", "onComplete invoked");
                                    if (task.isSuccessful()) {
                                        int i=0;
                                        for (DocumentSnapshot doc : task.getResult()) {
                                            if(i==0){
                                                txtFriendRanking=new TextView(getApplicationContext());
                                                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                lp.setMargins(50,20,50,20);
                                                txtFriendRanking.setLayoutParams(lp);
                                                txtFriendRanking.setAlpha(0.8f);
                                                txtFriendRanking.setTextColor(Color.parseColor("#FFFFFF"));
                                                txtFriendRanking.setBackgroundResource(R.drawable.round_corner);
                                                txtFriendRanking.setBackgroundColor(Color.parseColor("#6F27E5"));
                                                txtFriendRanking.append(doc.getString("Artist")+":\n");
                                                rankingLayout.addView(txtFriendRanking);
                                            }
                                            txtFriendRanking.append(doc.getString("Music")+"\n");
                                            i=i+1;
                                        }
                                    } else {
                                        Log.d("******", "ERROR QUERY RANKING");
                                    }
                                }
                            });
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent start = new Intent(getApplicationContext(), Menu.class);
        start.putExtra("Email", Email);
        startActivity(start);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}

