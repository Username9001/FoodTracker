package com.example.foodtracker;

import android.os.AsyncTask;

import com.example.foodtracker.ui.tools.ToolsFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class fetchData extends AsyncTask<Void,Void,Void> {

    // Set data string empty for initialization
    String data = "";
    // Set barcode empty
    String productEAN = "";

    @Override
    protected Void doInBackground(Void... voids) {
        // Put the call in an exception block, in case of blank URL or other errors

        try {
            // TODO Add barcode scanning result to URL

            // Set URL to API call, static for now
            URL url = new URL("https://nl.openfoodfacts.org/api/v0/product/" + productEAN + ".json?UserAgent=FoodTrackerApp_Android_Version0_1");

            // Open the URL connection
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            // Get inputstream from URL and convert bytes into chars
            InputStream inputStream = httpsURLConnection.getInputStream();

            // Create a buffer for the chars
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Append new lines to data String
            String line = "";
            while(line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            // TODO Fix Parsing the data object


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        ToolsFragment.data.setText(this.data);
    }
}
