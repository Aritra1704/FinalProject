package com.udacity.gradle.builditbigger.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arpaul.jokesandroid.ShowJokesActivity;
import com.udacity.gradle.builditbigger.Listener.onJokeReceived;
import com.udacity.gradle.builditbigger.WebServices.EndpointsAsyncTask;
import com.udacity.gradle.builditbigger.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MaterialDialog materialPB;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnTellJoke = (Button) root.findViewById(R.id.btnTellJoke);
        btnTellJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tellJoke(v);
            }
        });
        return root;
    }

    public void tellJoke(View view){

        showLoader("Please wait..","Loading your jokes..",false);

        new EndpointsAsyncTask().execute(new onJokeReceived() {
            @Override
            public void OnJokeReceivedListener(String joke) {
                if(joke!=null) {
                    Log.d("log", joke);
                    /*******Closing Interstitial Ad**********/
                    //dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                    hideLoader();

                    Intent intent = new Intent(getActivity(),ShowJokesActivity.class);
                    intent.putExtra("jokes",joke);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Shows Indefinite Progress Dialog.
     * @param title
     * @param message
     * @param isCancelable
     */
    public void showLoader(final String title,final String message, boolean isCancelable){
        getActivity().runOnUiThread(new RunProgressDialog(title, message, isCancelable));
    }

    /**
     * Shows Indefinite Progress Dialog.
     * @param title
     * @param message
     * @param progress
     * @param isCancelable
     */
    public void showLoader(final String title,final String message, final int progress, boolean isCancelable){
        getActivity().runOnUiThread(new RunProgressDialog(title, message, progress, isCancelable));
    }

    public void hideLoader() {
        getActivity().runOnUiThread(new Runnable() {
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

            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
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
