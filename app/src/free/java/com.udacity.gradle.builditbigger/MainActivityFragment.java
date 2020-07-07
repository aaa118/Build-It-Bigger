package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jokelibrary.JokeActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.udacity.gradle.builditbigger.MainActivity.JOKE;
import static com.udacity.gradle.builditbigger.MainActivity.MANFRED;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AsyncResponse {
    String joke;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        Button button = root.findViewById(R.id.bt_tellJoke);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndPointsAsyncTask endPointsAsyncTask = new EndPointsAsyncTask(new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        Intent intent = new Intent(getContext(), JokeActivity.class);
                        intent.putExtra(JOKE, output);
                        startActivity(intent);
                    }
                });

                endPointsAsyncTask.execute(new Pair<>(getContext(), MANFRED));
            }
        });
        return root;
    }

    @Override
    public void processFinish(String output) {
        joke = output;
    }
}
