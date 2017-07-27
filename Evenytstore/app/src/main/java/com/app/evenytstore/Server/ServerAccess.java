package com.app.evenytstore.Server;

import android.util.Log;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import EvenytServer.EvenytStoreAPIClient;

/**
 * Created by Enrique on 24/07/2017.
 */

public class ServerAccess {
    private static EvenytStoreAPIClient client;

    public static EvenytStoreAPIClient getClient(){
        if (client == null) {
            try {
                ApiClientFactory factory = new ApiClientFactory();
                client = factory.build(EvenytStoreAPIClient.class);
            }catch(Exception e){
                Log.d("Error", e.toString());
            }
        }
        return client;
    }
}
