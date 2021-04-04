package fr.cordier.duels.UiMenu.dashboard;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.deezer.sdk.model.Artist;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import fr.cordier.duels.Game.GroupList;
import fr.cordier.duels.Menu.GameMode;
import fr.cordier.duels.Menu.Settings;
import fr.cordier.duels.R;
import fr.cordier.duels.UiMenu.Menu;

public class MenuFragment extends Fragment {

    private MenuViewModel MenuViewModel;
    TextView profile,titre,friends,share,custom,artistName;
    ImageView play,settings,vinyle,artistImage;
    LinearLayout prop;
    Menu r;
    private Handler handler = new Handler();
    String Email;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MenuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        root = inflater.inflate(R.layout.fragment_menu, container, false);
        /*final TextView textView = root.findViewById(R.id.textView11);
        MenuViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/


        //Récup data
        r=(Menu) getActivity();
        Intent intent=r.getIntent();
        Email=intent.getStringExtra("Email");
        Log.i("*****",Email);

        //Initialisation Widgets

        vinyle=(ImageView) root.findViewById(R.id.imageView3);

        play= root.findViewById(R.id.playfragment);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(r.getApplicationContext(), GroupList.class);
                Animation anim= AnimationUtils.loadAnimation(r.getApplicationContext(),R.anim.rotate);
                vinyle.startAnimation(anim);
                start.putExtra("Email",Email);
                startActivity(start);
                r.overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                r.finish();
            }
        });
        play.setVisibility(View.GONE);

        titre=root.findViewById(R.id.titreMenu);

        artistName=root.findViewById(R.id.textArtistMenu);
        artistImage=root.findViewById(R.id.imageArtistMenu);

        prop=root.findViewById(R.id.prop);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            prop.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        }

        settings= root.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(r.getApplicationContext(), Settings.class);
                start.putExtra("Email",Email);
                startActivity(start);
                r.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left );
                r.finish();
            }
        });

        custom= root.findViewById(R.id.customduels);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(r.getApplicationContext(), GameMode.class);
                start.putExtra("Email",Email);
                startActivity(start);
                r.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                r.finish();
            }
        });


        handler.postDelayed(runnable,10);


        AnimationButton();

        return root;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // The method you want to call every now and then.
            ArtistProp();
            handler.postDelayed(this,10000); // 2000 = 2 seconds. This time is in millis.
        }
    };

    protected void AnimationButton(){
        play.setVisibility(View.VISIBLE);
        Animation slide= AnimationUtils.loadAnimation(r.getApplicationContext(), R.anim.slide_button);
        play.startAnimation(slide);
    }

    protected void ArtistProp(){
        Random r=new Random();
        long id=(long)r.nextInt(300);
        DeezerConnect deezerConnect = new DeezerConnect("423942");
        String[] permissions = new String[]{Permissions.BASIC_ACCESS, Permissions.MANAGE_LIBRARY, Permissions.LISTENING_HISTORY};
        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                Artist artiste= (com.deezer.sdk.model.Artist) result;
                TextView txt=new TextView(getContext());
                txt.setTextColor(Color.parseColor("#FFFFFF"));
                txt.setTextSize(20);
                txt.setGravity(Gravity.CENTER_VERTICAL);
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent start=new Intent(getContext(), fr.cordier.duels.Game.start.class);
                        start.putExtra("Email",Email);
                        start.putExtra("NomArtiste",artiste.getName());
                        start.putExtra("IdArtiste",String.valueOf(artiste.getId()));
                        startActivity(start);
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });

                ImageView im=new ImageView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(180,180);
                layoutParams.setMargins(30,0,30,0);
                im.setLayoutParams(layoutParams);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent start=new Intent(getContext(), fr.cordier.duels.Game.start.class);
                        start.putExtra("Email",Email);
                        start.putExtra("NomArtiste",artiste.getName());
                        start.putExtra("IdArtiste",String.valueOf(artiste.getId()));
                        startActivity(start);
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });

                Picasso.get().load(artiste.getMediumImageUrl()).into(im, new Callback() {
                    @Override
                    public void onSuccess() {

                        RequestListener listener = new JsonRequestListener() {

                            public void onResult(Object result, Object requestId) {
                                List<Track> tracks= (List<com.deezer.sdk.model.Track>) result;
                                if(tracks.size()>=2){
                                    if(tracks.get(0).getShortTitle().equals(tracks.get(1).getShortTitle())){
                                        txt.setText(artiste.getName()+"\n"+tracks.get(0).getShortTitle()+","+tracks.get(2).getShortTitle()+",...");
                                    }
                                    else{
                                        txt.setText(artiste.getName()+"\n"+tracks.get(0).getShortTitle()+","+tracks.get(1).getShortTitle()+",...");
                                    }
                                    prop.removeAllViews();
                                    prop.addView(im);
                                    prop.addView(txt);
                                }
                            }
                            public void onUnparsedResult(String requestResponse, Object requestId) {

                            }
                            public void onException(Exception e, Object requestId) {

                            }
                        };
                        // create the request
                        DeezerRequest request = DeezerRequestFactory.requestArtistTopTracks(id);
                        // set a requestId, that will be passed on the listener's callback methods
                        request.setId("myRequest");
                        // launch the request asynchronously
                        deezerConnect.requestAsync(request, listener);

                    }

                    @Override
                    public void onError(Exception ex) {

                    }
                });




                /*
                Animation slide= AnimationUtils.loadAnimation(getContext(), R.anim.slide_menu);
                artistNametemp.setAnimation(slide);
                artistImagetemp.setAnimation(slide);
                artistNametemp.animate()
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                artistName.setText(artiste.getName());

                            }
                        });
                artistImagetemp.animate()
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get().load(artiste.getMediumImageUrl()).into(artistImage);
                            }
                        });

                 */

            }
            public void onUnparsedResult(String requestResponse, Object requestId) {

            }
            public void onException(Exception e, Object requestId) {

            }
        };
        // create the request
        DeezerRequest request = DeezerRequestFactory.requestArtist(id);
        // set a requestId, that will be passed on the listener's callback methods
        request.setId("myRequest");
        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);

    }
}