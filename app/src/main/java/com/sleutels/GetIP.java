package com.sleutels;

/**
 * Created by Jim on 10/04/2015.
 * Thanks to Raphael.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sleutels.Helpers.HelpersConnection;
import com.sleutels.Models.ModelsSettings;
import com.sleutels.Models.ModelsSlotList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class GetIP extends Activity {

    // Required Variables
    EditText ipTextEdit;
    TextView ipTextView, loginArea;
    Button loginButton;
    Typeface fontstyle;

    // Instances
    ModelsSettings savedModelsSettings = ModelsSettings.getInstance();
    ModelsSlotList modelsSlotList = ModelsSlotList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        ipTextEdit = (EditText) findViewById(R.id.ipTextEdit);
        ipTextView = (TextView) findViewById(R.id.ipTextView);
        loginArea = (TextView) findViewById(R.id.loginArea);
        loginButton = (Button) findViewById(R.id.loginButton);

        fontstyle = Typeface.createFromAsset(getAssets(), "fonts/fontstyle.ttf");
        loginArea.setTypeface(fontstyle);
        ipTextEdit.setText(savedModelsSettings.getIpAddress());
    }

    public void checkConnection(View view) {

        // Check connection using IP address from device.
        String ip = String.valueOf(ipTextEdit.getText());
        savedModelsSettings.isConnected(true);
        savedModelsSettings.setIp4Adress(ip);

        boolean connected = tryConnection();
        if (connected == false) {
            modelsSlotList.setCache();
            noConnectionToServer();
        } else if (connected == true) {
            getSloten();
            connectedToServer();
        }
        // Go to main menu screen.
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);

        finish();
    }

    // Get data from server with ip address and port. (Thanks to Raphael for helping.)
    public void getSloten() {

        JSONObject slotenJSONobject = new JSONObject();

        try {
            slotenJSONobject.put("slotenlijst", ""); // Try to find sloten json object.
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reply = "replying";
        {
            try {
                reply = new HelpersConnection(this, savedModelsSettings.getIpAddress(), 4444, slotenJSONobject.toString()).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            JSONArray slotenArray = null;
            try {
                slotenArray = new JSONArray(reply);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // Clear array from any previous use.
            modelsSlotList.clearSloten();

            // Fill array with data received from server.
            for (int i = 0; i < slotenArray.length(); i++) {
                try {
                    JSONObject value = slotenArray.getJSONObject(i);

                    String valueString = value.getString("naam");
                    modelsSlotList.addSloten(valueString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean tryConnection() {

        JSONObject slotenJSONObjectBool = new JSONObject();

        try {
            slotenJSONObjectBool.put("slotenlijst", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        {
            try {
                String reply;
                reply = new HelpersConnection(this, savedModelsSettings.getIpAddress(), 4444, slotenJSONObjectBool.toString()).execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();

            } catch (ExecutionException e) {
                e.printStackTrace();

            }

        }

        return savedModelsSettings.getisOnline();
    }

    public void setCacheSloten() {

        // Data from server as "cache". Was not possible to connect to server for actual data.
        ArrayList<String> sloten = new ArrayList<String>();
        sloten.add("Sleutel slotsystemen"); // Slot 1
        sloten.add("RFID slotsystemen"); // Slot 2
        sloten.add("Biometrische slotsystemen"); // Slot 3

        modelsSlotList.clearSloten();

        modelsSlotList.setSlotenLijst(sloten);
    }

    // Connection to the server. Logging success.
    public void connectedToServer() {
        Toast.makeText(this, R.string.connected,
                Toast.LENGTH_LONG).show();
    }

    // No connection to the server. Connection error.
    public void noConnectionToServer() {
        Toast.makeText(this, R.string.noConnectionFound,
                Toast.LENGTH_LONG).show();
    }


}



