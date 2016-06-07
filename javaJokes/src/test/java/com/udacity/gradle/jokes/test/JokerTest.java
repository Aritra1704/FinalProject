package com.udacity.gradle.jokes.test;

import com.udacity.gradle.jokes.Joker;
import org.junit.Test;

public class JokerTest {
    @Test
    public void test() {
        /*Joker joker = new Joker();
        assert joker.getJoke().length() != 0;*/

        new EndpointsAsyncTask().execute(new onJokeReceived() {
            @Override
            public void OnJokeReceivedListener(String joke) {
                if(joke!=null) {
                    Log.d("log", joke);
                    assert joke.length() != 0;
                }
            }
        });
    }
}