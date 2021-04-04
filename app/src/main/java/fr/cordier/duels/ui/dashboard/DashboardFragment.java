package fr.cordier.duels.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import fr.cordier.duels.Menu.GenreList;
import fr.cordier.duels.R;
import fr.cordier.duels.Game.Rankings;
import fr.cordier.duels.UiMenu.Menu;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextView nomArtiste;
    private TextView ranking;
    private Button menu;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.textView11);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        nomArtiste=root.findViewById(R.id.textView12);
        ranking=root.findViewById(R.id.songList);
        Rankings r=(Rankings) getActivity();
        nomArtiste.setText(r.getArtist());
        r.FriendResult(ranking);
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