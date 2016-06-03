/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Aritra.myapplication.backend;

import com.example.JokesClass;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "jokesApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.myapplication.Aritra.example.com",
    ownerName = "backend.myapplication.Aritra.example.com",
    packagePath=""
  )
)
public class JokesEndpoint {
    private static final Logger logger = Logger.getLogger(JokesEndpoint.class.getName());

    /**
     *
     * @return The object with the answer
     */
    @ApiMethod(name = "getJokes")
    public JokesBean getJokes() {
        logger.info("Calling giveMejoke ");
        JokesBean jokesBean = new JokesBean();
        JokesClass joke = new JokesClass();
        jokesBean.setData(joke.getRandomJoke());
        return jokesBean;
    }

}
