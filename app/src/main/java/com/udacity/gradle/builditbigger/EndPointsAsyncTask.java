package com.udacity.gradle.builditbigger;

import android.content.Context;
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
    private static MyApi myApiService = null;
    public AsyncResponse delegate;

    public EndPointsAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

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
        Random random = new Random();
        int randomList = random.nextInt(4);
        String joke;
        if (result !=null) {
            joke = result.get(randomList);
        } else {
            joke = "GCM is Offline";
        }
        delegate.processFinish(joke);
    }
}