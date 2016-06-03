package com.udacity.gradle.builditbigger.WebServices;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.example.aritra.myapplication.backend.jokesApi.JokesApi;
import com.example.aritra.myapplication.backend.jokesApi.model.JokesBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.Listener.onJokeReceived;

import java.io.IOException;

/**
 * Created by Aritra on 5/19/2016.
 */
public class EndpointsAsyncTask extends AsyncTask<onJokeReceived, Void, String> {
    private static JokesApi jokesApiService = null;
    private onJokeReceived listener;

    private static final String ENDPOINT_APP_NAME = "final-project-udacity";
    public static final String URL_ENDPOINT_PROD = "https://" + ENDPOINT_APP_NAME+".appspot.com/_ah/api/";
    public static final String URL_ENDPOINT_PROD_V2 = "https://2-dot-" + ENDPOINT_APP_NAME+".appspot.com/_ah/api/";
    public static final String URL_ENDPOINT_SVIL = "http://10.0.2.2:8080/_ah/api/";

    //https://final-project-udacity.appspot.com/
    //https://alessiopapazzoni.com/2015/11/11/deploying-a-google-endpoint-with-google-cloud-platform/comment-page-1/#comment-75
    //https://github.com/AlessioPz/EndpointSample

    @Override
    protected String doInBackground(onJokeReceived... params) {
        if(jokesApiService == null) {  // Only do this once
            JokesApi.Builder jokesBuilder = new JokesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setApplicationName("Final Project Udacity")
                    .setRootUrl(URL_ENDPOINT_PROD/*"https://final-project-udacity.appspot.com/_ah/api/"*/)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                            request.setDisableGZipContent(false);
                        }
                    });
            jokesApiService = jokesBuilder.build();
        }

        listener = params[0];

        try {
            //return jokesApiService.sayHi(name).execute().getData();

            String jokesBean = jokesApiService.getJokes().execute().getData();
            return jokesBean;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        listener.OnJokeReceivedListener(result);
    }
}
