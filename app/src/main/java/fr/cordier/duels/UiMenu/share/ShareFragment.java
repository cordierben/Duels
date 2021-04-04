package fr.cordier.duels.UiMenu.share;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.cordier.duels.Class.Artist;
import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.Menu.Share;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;
import fr.cordier.duels.User.FriendList;


public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    String Email;
    Button invite;
    GridLayout grille;
    TextView back;
    EditText search;
    ImageView loupe;
    String message;
    Menu r;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel = ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        /*final TextView textView = root.findViewById(R.id.textView11);
        friendsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        r = (Menu) getActivity();

        //Récup data
        Intent intent = r.getIntent();
        Email = intent.getStringExtra("Email");
        Log.i("*****", Email);

        //Init widget
        invite = root.findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I'm using Duels, an app to compare tastes in music, try it for free!");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        grille = (GridLayout) root.findViewById(R.id.gridRankingShare);

        search = (EditText) root.findViewById(R.id.searchShare);
        loupe = (ImageView) root.findViewById(R.id.loupeShare);

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                search.setText("");
            }
        });

        loupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindArtist(search.getText().toString());
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameL = String.valueOf(search.getText());
                FindArtist(nameL);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SearchArtist();
        return root;
    }

    private void FindArtist(String artist){
        if(!artist.equals("")){
            List<Artist> data=new ArrayList<Artist>();
            try {
                FileInputStream fileInputStream = r.openFileInput("artist.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();

                String ligne;
                while ((ligne = bufferedReader.readLine()) != null) {
                    ligne = bufferedReader.readLine();
                    if(ligne!=null){
                        String[] values=ligne.split(";");
                        if(values[0].toLowerCase().contains(artist.toLowerCase())){
                            Artist artiste=new Artist(values[0],values[1],values[2]);
                            data.add(artiste);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(data.size()==0){
                DeezerConnect deezerConnect = new DeezerConnect("423942");
                String[] permissions = new String[]{Permissions.BASIC_ACCESS, Permissions.MANAGE_LIBRARY, Permissions.LISTENING_HISTORY};

                //Requete Deezer pour insertion en boucle
                RequestListener listener = new JsonRequestListener() {

                    public void onResult(Object result, Object requestId) {
                        List<com.deezer.sdk.model.Artist> artiste= (List<com.deezer.sdk.model.Artist>) result;
                        for(int i=0;i<artiste.size();i=i+1){
                            if(artiste.get(i).getNbFans()>3000){
                                Artist objet=new Artist(artiste.get(i).getName(),artiste.get(i).getMediumImageUrl(),String.valueOf(artiste.get(i).getId()));
                                data.add(objet);
                            }
                        }
                        ArtistList(data);
                    }
                    public void onUnparsedResult(String requestResponse, Object requestId) {

                    }
                    public void onException(Exception e, Object requestId) {

                    }
                };
                // create the request
                DeezerRequest request = DeezerRequestFactory.requestSearchArtists(artist);
                // set a requestId, that will be passed on the listener's callback methods
                request.setId("myRequest");
                // launch the request asynchronously
                deezerConnect.requestAsync(request, listener);
            } else{
                ArtistList(data);
            }
        } else {
            SearchArtist();
        }
    }

    private void SearchArtist(){
        List<Artist> data=new ArrayList<Artist>();
        try {
            FileInputStream fileInputStream = r.openFileInput("artist.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String ligne;
            while ((ligne = bufferedReader.readLine()) != null) {
                ligne = bufferedReader.readLine();
                if(ligne!=null){
                    String[] values=ligne.split(";");
                    Artist artiste=new Artist(values[0],values[1],values[2]);
                    data.add(artiste);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArtistList(data);
    }

    protected void ArtistList(List<Artist> artist) {
        grille.removeAllViews();
        for (int i = 0; i < artist.size(); i = i + 1) {
            final int id = i;

            ImageView im = new ImageView(r.getApplicationContext());
            Picasso.get().load(artist.get(i).getImage()).into(im);
            grille.addView(im);

            TextView txt = new TextView(r.getApplicationContext());
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            txt.setText(artist.get(i).getName());
            txt.setTextSize(22);
            grille.addView(txt);

            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareArtist(artist.get(id).getId());
                }
            });

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareArtist(artist.get(id).getId());
                }
            });
        }
    }

    protected void ShareArtist(String artiste){
        message="Here is my top "+artiste+"'s songs on Duels:";
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("Ranking")
                .whereEqualTo("User",Email)
                .whereEqualTo("Artist",artiste)
                .orderBy("Score", Query.Direction.DESCENDING)
                .limit(8)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.i("*******","onComplete invoked");
                        if (task.isSuccessful()) {
                            int i=1;
                            for (DocumentSnapshot doc : task.getResult()) {
                                message=message+"\n"+i+"/ "+doc.getString("Music");
                                i=i+1;
                                if(i==9){
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                                    sendIntent.setType("text/plain");
                                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                                    startActivity(shareIntent);
                                }
                            }
                            if(i==1){
                                Toast errorToast = Toast.makeText(r.getApplicationContext(), "Artist not attempted", Toast.LENGTH_SHORT);
                                errorToast.show();
                            }
                        } else {
                            Log.d("******","ERROR QUERY RANKING");
                        }
                    }
                });
    }
}