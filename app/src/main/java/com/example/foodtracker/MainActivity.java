package com.example.foodtracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

// own imports

public class MainActivity extends AppCompatActivity  {

    // Generate button
    Button click;

    Intent intent;

    private AppBarConfiguration mAppBarConfiguration;

    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_results, R.id.nav_overview,
                R.id.nav_tools, R.id.nav_profile, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void takePicture(View view) {
        // create intent to start an activity
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);

            // Asynchronous function to scan barcodes
            // and connect to Nutriotonix API
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // TODO GET BARCODE
                    // Get Barcode from camera image with Google API
                    String scannedEAN = "5410013107279"; // temporary nr. - Spa Water

                    // Create URL
                    URL apiEndpoint = null;
                    try {
                        // API connection
                         apiEndpoint = new URL("https://nl.openfoodfacts.org/api/v0/product/" + scannedEAN + ".json?UserAgent=FoodTrackerApp_Android_Version0_1" );
//                        githubEndpoint = new URL(""); // TEMPORARY FAKE URL TO LIMIT CALLS
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    // Create connection
                    HttpsURLConnection myConnection = null;
                    try {

                        myConnection = (HttpsURLConnection) apiEndpoint.openConnection();
                        Log.i("Success", "connection succes for EAN code code: " + scannedEAN);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Set request properties (optional, as the necessary ones are already in the URL above)
                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

                    // Test Response COMMENTED OUT BECAUSE OF RATE LIMIT OF 50
                    try {
                        if (myConnection.getResponseCode() == 200) {
                            // Success -> Get response
                            // TEMPORARILY COMMENTED BECAUSE THERE'S A LIMIT OF 50 PER DAY!
                            InputStream responseBody = myConnection.getInputStream();
                            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                            // Parse response
                            JsonReader jsonReader = new JsonReader(responseBodyReader);

                            jsonReader.beginObject(); // Start processing the JSON object
                            while (jsonReader.hasNext()) { // Loop through all keys
                                String key = jsonReader.nextName(); // Fetch the next key
                                // TODO Loop through the different values and store them
                                if (key.equals("pnns_groups_2")) { // Check if desired key
                                    // Fetch the value as a String
                                    String value = jsonReader.nextString();

                                    // Do something with the value. Logging for now
                                    Log.i("Success", "The product is called: " + value );
                                    // Try displaying it
//                                    TextView responseView = (TextView) findViewById(R.id.responseView);
//                                    responseView.setText("SUCCESS");

                                    break; // Break out of the loop
                                } else {
                                    jsonReader.skipValue(); // Skip values of other keys
                                }
                            }
                            Log.i("Succes", "The product is called:"+ "name value" );

                            // TODO Log the values in a database for the user to retrieve

                            // TODO Display the desired values in the app

                        } else {
                            // Error handling code goes here
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK) {
//            YOUTUBE TUTORIAL
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//            CANVAS SLIDES
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            ImageView imgCapture = (ImageView) findViewById(R.id.imageView);
            imgCapture.setImageBitmap(bp);
        } else if ( resultCode == RESULT_CANCELED ) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }

    }
}
