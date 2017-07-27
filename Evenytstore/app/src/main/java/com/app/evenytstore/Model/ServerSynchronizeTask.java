package com.app.evenytstore.Model;

import android.os.AsyncTask;

import com.app.evenytstore.Server.ServerAccess;

import java.util.ArrayList;
import java.util.List;

import EvenytServer.EvenytStoreAPIClient;
import EvenytServer.model.Brand;

/**
 * Created by Enrique on 25/07/2017.
 */

public class ServerSynchronizeTask extends AsyncTask<DatabaseAccess, Void, Void> {
    @Override
    protected Void doInBackground(DatabaseAccess... params) {

        EvenytStoreAPIClient client = ServerAccess.getClient();

        DatabaseAccess access = params[0];

        List<Brand> newBrands = new ArrayList<>();
        List<Brand> updatedBrands = new ArrayList<>();
        List<Brand> deletedBrands = new ArrayList<>();

        List<Brand> serverBrands = client.brandsGet();
        for(Brand b : serverBrands){
            if(Shelf.getHashBrands().containsKey(b.getCode())){
                Brand b2 = Shelf.getHashBrands().get(b.getCode());
                if(!b.getDescription().equals(b2.getDescription()) || !b.getName().equals(b2.getName()))
                    updatedBrands.add(b);
            }
            else
                newBrands.add(b);
        }

        for(Brand b : Shelf.getHashBrands().values()){
            boolean found = false;

            for(Brand b2 : serverBrands){
                if(b2.getCode().equals(b.getCode())){
                    found = true;
                    break;
                }
            }

            if(!found)
                deletedBrands.add(b);
        }

        access.open();
        access.insertBrands(newBrands);
        access.updateBrands(updatedBrands);
        access.deleteBrands(deletedBrands);
        access.close();
        newBrands = updatedBrands = deletedBrands = null;

        return null;
    }
}