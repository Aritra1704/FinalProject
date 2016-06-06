package com.udacity.gradle.builditbigger.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arpaul.jokesandroid.ShowJokesActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.BuildConfig;
import com.udacity.gradle.builditbigger.Listener.onJokeReceived;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.WebServices.EndpointsAsyncTask;


public class MainActivity extends AppCompatActivity {

    //https://github.com/shivasurya/P4-Build-it-Bigger
    private InterstitialAd mInterstitialAd;
    public MaterialDialog materialPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //beginPlayingGame();
            }
        });

        requestNewInterstitial();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view){

        if(BuildConfig.FLAVOR.equals("free")){
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        } else {
            showLoader("Please wait..","Loading your jokes..",false);
        }

        new EndpointsAsyncTask().execute(new onJokeReceived() {
            @Override
            public void OnJokeReceivedListener(String joke) {
                if(joke!=null) {
                    Log.d("log", joke);
                    /*if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {*/
                    /*******Closing Interstitial Ad**********/
                        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                        hideLoader();

                        Intent intent = new Intent(MainActivity.this,ShowJokesActivity.class);
                        intent.putExtra("jokes",joke);
                        startActivity(intent);
                    //}
                }
            }

        });

        //new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "Joking Aritra"));

//        Intent intent = new Intent(this,ShowJokesActivity.class);
//        intent.putExtra("jokes",new JokesClass().getJokes());
//        startActivity(intent);
        //Toast.makeText(this, new JokesClass().getJokes(), Toast.LENGTH_SHORT).show();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    /**
     * Shows Indefinite Progress Dialog.
     * @param title
     * @param message
     * @param isCancelable
     */
    public void showLoader(final String title,final String message, boolean isCancelable){
        runOnUiThread(new RunProgressDialog(title, message, isCancelable));
    }

    /**
     * Shows Indefinite Progress Dialog.
     * @param title
     * @param message
     * @param progress
     * @param isCancelable
     */
    public void showLoader(final String title,final String message, final int progress, boolean isCancelable){
        runOnUiThread(new RunProgressDialog(title, message, progress, isCancelable));
    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (materialPB != null && materialPB.isShowing())
                    materialPB.dismiss();
            }
        });
    }
    class RunProgressDialog implements Runnable {
        private String strTitle;// Title of the materialDialog
        private String strMessage;// Message to be shown in materialDialog
        private boolean isCancelable=false;
        private int progress;

        public RunProgressDialog(String strTitle,String strMessage, boolean isCancelable)
        {
            this.strTitle 		= strTitle;
            this.strMessage 	= strMessage;
            this.progress 	    = 0;
            this.isCancelable 	= isCancelable;
        }

        public RunProgressDialog(String strTitle,String strMessage, int progress, boolean isCancelable)
        {
            this.strTitle 		= strTitle;
            this.strMessage 	= strMessage;
            this.progress 	    = progress;
            this.isCancelable 	= isCancelable;
        }

        @Override
        public void run() {
            if (materialPB != null && materialPB.isShowing())
                materialPB.dismiss();

            MaterialDialog.Builder builder = new MaterialDialog.Builder(MainActivity.this)
                    .title(strTitle)
                    .content(strMessage)
                    .cancelable(isCancelable);

            builder
                    .progress(true, progress)
                    .progressIndeterminateStyle(false);

            try{
                if (materialPB == null || !materialPB.isShowing()){
                    materialPB = builder.build();
                    materialPB.show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
