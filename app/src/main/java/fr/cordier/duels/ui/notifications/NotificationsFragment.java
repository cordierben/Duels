package fr.cordier.duels.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blongho.country_data.World;

import java.util.ArrayList;
import java.util.Locale;

import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;
import fr.cordier.duels.Game.Rankings;
import fr.cordier.duels.UiMenu.Menu;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private TextView nomArtiste;
    private LinearLayout ranking;
    private ImageView flag;
    private Spinner spinI;
    private Button menu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.textView11);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Rankings r=(Rankings) getActivity();
        nomArtiste=root.findViewById(R.id.textView12);
        ranking=root.findViewById(R.id.WorldRankingLayout);

        flag=root.findViewById(R.id.flag);
        spinI= root.findViewById(R.id.spinnercountry);
        String[]locales = Locale.getISOCountries();
        ArrayList<String> liste=new ArrayList<>();
        liste.add("World");
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            liste.add(obj.getDisplayCountry());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.custom_spinner, liste);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinI.setAdapter(adapter);
        spinI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ranking.removeAllViews();
                World.init(getContext());
                final int flagnum = World.getFlagOf(spinI.getSelectedItem().toString());
                flag.setImageResource(flagnum);
                r.GlobalResult(ranking,spinI.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ranking.removeAllViews();
                r.GlobalResult(ranking,"World");
            }

        });

        nomArtiste.setText(r.getArtist());

        menu=root.findViewById(R.id.RankingToMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(r.getApplicationContext(), Menu.class);
                start.putExtra("Email",String.valueOf(r.getUserEmail()));
                startActivity(start);
                r.finish();
            }
        });
        return root;
    }
}