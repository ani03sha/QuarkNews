package org.anirudh.redquark.quarknews.application;

import android.app.Application;

import org.anirudh.redquark.quarknews.receiver.ConnectivityReceiver;

/**
 * Application class for this app - It represents the complete app's context.
 * Primarily used to declared global variables
 */
public class QuarkNewsApplication extends Application {

    /**
     * Instance of application
     */
    private static QuarkNewsApplication quarkNewsApplication;

    @Override
    public void onCreate(){
        super.onCreate();
        quarkNewsApplication = this;
    }

    public static synchronized QuarkNewsApplication getInstance(){
        return quarkNewsApplication;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
