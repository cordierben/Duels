package fr.cordier.duels.UiMenu.notifications;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blongho.country_data.World;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.cordier.duels.Game.Rankings;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;
import fr.cordier.duels.UiMenu.home.ProfileViewModel;
import fr.cordier.duels.User.FriendList;


public class FriendsFragment extends Fragment {

    private FriendsViewModel friendsViewModel;
    EditText friendId;
    Button Add;
    TextView erreur;
    ImageView friends;
    TextView back;
    GridLayout layout;
    String Email;
    FirebaseFirestore ff=FirebaseFirestore.getInstance();
    Menu r;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friends, container, false);
        /*final TextView textView = root.findViewById(R.id.textView11);
        friendsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        r=(Menu) getActivity();

        //Initialisation
        friendId=(EditText) root.findViewById(R.id.friendId);
        Add=(Button) root.findViewById(R.id.AddFriend);
        erreur=(TextView) root.findViewById(R.id.friendErreur);
        friends=(ImageView) root.findViewById(R.id.friends);
        layout=(GridLayout) root.findViewById(R.id.gridLayout2);

        //Récup data
        Intent intent=r.getIntent();
        Email=intent.getStringExtra("Email");

        friendId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                friendId.setText("");
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=friendId.getText().toString();
                if(mail.equals(Email)==false){
                    selectUser(mail);
                } else {
                    erreur.setText("Sadly, you can't be your own friend...");
                }
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(r.getApplicationContext(), FriendList.class);
                start.putExtra("Email",Email);
                startActivity(start);
                r.overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                r.finish();
            }
        });


        FirebaseFirestore db=FirebaseFirestore.getInstance();
        ff.collection("Friends")
                .whereEqualTo("Email2",Email)
                .whereEqualTo("State","Pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                ff.collection("Users")
                                        .whereEqualTo("Email",doc.getString("Email1"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                Log.i("*******","onComplete invoked");
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot doc2 : task.getResult()) {

                                                        //Création zone de texte
                                                        TextView username=new TextView(r.getApplicationContext());
                                                        username.setText(doc2.getString("Username"));
                                                        username.setTextSize(25);
                                                        username.setTextColor(getResources().getColor(R.color.White));
                                                        username.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                        GridLayout.LayoutParams param= new GridLayout.LayoutParams(GridLayout.spec(
                                                                GridLayout.UNDEFINED,GridLayout.FILL,1f),
                                                                GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
                                                        param.height = 0;
                                                        param.width = 0;
                                                        username.setLayoutParams(param);
                                                        layout.addView(username);

                                                        //Création bouton Oui/Non
                                                        Button oui=new Button(r.getApplicationContext());
                                                        oui.setText("Accept");
                                                        oui.setTextColor(getResources().getColor(R.color.White));
                                                        oui.setBackground(getResources().getDrawable(R.drawable.custom_button));
                                                        oui.setGravity(Gravity.CENTER);
                                                        layout.addView(oui);

                                                        Button non=new Button(r.getApplicationContext());
                                                        non.setText("Refuse");
                                                        non.setTextColor(getResources().getColor(R.color.White));
                                                        non.setBackground(getResources().getDrawable(R.drawable.custom_button));
                                                        non.setGravity(Gravity.CENTER);
                                                        layout.addView(non);

                                                        oui.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                String etat="Accepted";
                                                                username.setVisibility(View.GONE);
                                                                oui.setVisibility(View.GONE);
                                                                non.setVisibility(View.GONE);
                                                                DocumentReference document=db.collection("Friends").document(doc.getId());
                                                                document.update("State",etat)
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
                                                            }
                                                        });

                                                        non.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                username.setVisibility(View.GONE);
                                                                oui.setVisibility(View.GONE);
                                                                non.setVisibility(View.GONE);
                                                                db.collection("Friends").document(doc.getId())
                                                                        .delete()
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d("*******", "DocumentSnapshot successfully deleted!");
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w("******", "Error deleting document", e);
                                                                            }
                                                                        });
                                                            }
                                                        });
                                                    }

                                                } else {
                                                    Log.d("******","ERROR QUERY RANKING");
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d("******","ERROR QUERY RANKING");
                        }
                    }
                });

        return root;
    }

    protected void add(String mail){
        Map<String,Object> friend=new HashMap<>();
        friend.put("Email1",Email);
        friend.put("Email2",mail);
        friend.put("State","Pending");
        friend.put("Type","Newbie");
        // Add a new document with a generated ID
        ff.collection("Friends")
                .add(friend)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("******", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("******", "Error adding document", e);
                    }
                });
    }

    protected void select(String mail1,String mail2,int passage){
        ff.collection("Friends")
                .whereEqualTo("Email1", mail1)
                .whereEqualTo("Email2",mail2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                erreur.setText("This user is already your friend, or a request has already been send");
                            }
                            if(task.getResult().isEmpty()){
                                ff.collection("Friends")
                                        .whereEqualTo("Email1", mail2)
                                        .whereEqualTo("Email2",mail1)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                Log.i("*******","onComplete invoked");
                                                if (task.isSuccessful()) {
                                                    int i=0;
                                                    for (DocumentSnapshot doc : task.getResult()) {
                                                        i=i+1;
                                                        Log.i("******","nope");
                                                        erreur.setText("This user is already your friend, or a request has already been send");
                                                    }
                                                    if(i==0){
                                                        if(passage==2){
                                                            if(mail1!=Email){
                                                                add(mail1);
                                                            } else {
                                                                add(mail2);
                                                            }
                                                            erreur.setText("Request sent!");
                                                        } else{
                                                            select(mail2,mail1,2);
                                                        }
                                                    }
                                                } else {
                                                    Log.d("******","ERROR QUERY RANKING");
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d("******","ERROR QUERY RANKING");
                        }
                    }
                });
    }

    protected void selectUser(String mail){
        ff.collection("Users")
                .whereEqualTo("Email",mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                select(Email,mail,1);
                            }
                            if(task.getResult().isEmpty()){
                                erreur.setText("This user doesn't exist");
                            }
                        } else {
                            Log.d("******","ERROR QUERY RANKING");
                        }
                    }
                });

    }
}