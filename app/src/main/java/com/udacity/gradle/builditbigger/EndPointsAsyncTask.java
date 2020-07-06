package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class EndPointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, ArrayList<String>> {
    public static final String JOKES_LIST = "JOKES_LIST";
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected ArrayList<String> doInBackground(Pair<Context, String>... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;
        ArrayList<String> jokeslist = null;

        try {
            jokeslist = (ArrayList<String>) myApiService.getJokes().execute().getJokesList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jokeslist;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        SharedPreferences sharedPref = context.getSharedPreferences("JokesPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Random random = new Random();
        int randomList = random.nextInt(4);
        editor.putString(JOKES_LIST, result.get(randomList));
        editor.apply();
    }
}