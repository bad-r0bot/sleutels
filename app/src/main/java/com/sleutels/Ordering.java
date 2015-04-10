package com.sleutels;

/**
 * Created by Jim on 10/04/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sleutels.Helpers.HelpersConnection;
import com.sleutels.Models.ModelsPersoonlijkInfo;
import com.sleutels.Models.ModelsSettings;
import com.sleutels.Models.ModelsSlotList;
import com.sleutels.Models.ModelsSlotenInfoBeknopt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Ordering extends Activity {

    // Required variables
    TextView slotenTag, slotenInfo, headerText;
    EditText naam, adres, tel, mail;
    Button backButton;
    Typeface font1, font2;

    // Instances
    ModelsSettings modelsSettingsData = ModelsSettings.getInstance();
    ModelsSlotList modelsSlotList = ModelsSlotList.getInstance();
    ModelsSlotenInfoBeknopt modelsSlotenInfoBeknopt = ModelsSlotenInfoBeknopt.getInstance();
    ModelsPersoonlijkInfo modelsPersoonlijkInfo = ModelsPersoonlijkInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);

        font1 = Typeface.createFromAsset(getAssets(), "fonts/fontstyle2.ttf");
        font2 = Typeface.createFromAsset(getAssets(), "fonts/fontstyle.ttf");

        slotenTag = (TextView) findViewById(R.id.SlotenTextViewSlotenAanvraag);
        slotenInfo = (TextView) findViewById(R.id.slotenInfoAanvraag);
        headerText = (TextView) findViewById(R.id.headerTextAanvraag);
        headerText.setTypeface(font2);

        naam = (EditText) findViewById(R.id.naamEdit);
        tel = (EditText) findViewById(R.id.telEdit);
        adres = (EditText) findViewById(R.id.adresEdit);
        mail = (EditText) findViewById(R.id.mailEdit);
        backButton = (Button) findViewById(R.id.backButtonAanvraag);
        backButton.setTypeface(font1);
        backButton.setText(R.string.leftSide);
        slotenTag.setText(modelsSlotList.getSlotenLijst().get(modelsSlotList.getSelectedSloten()));
        slotenInfo.setText(modelsSlotenInfoBeknopt.getShortInfoSloten());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Get user information from input fields.
    public boolean getUserGegevens() {
        boolean compleet = true;
        if (naam.getText().length() > 1) { // Voor namen zoals Jo en Bo
            modelsPersoonlijkInfo.setUserNaam(String.valueOf(naam.getText()));
            compleet = true;
        } else {
            Toast.makeText(this, "Voer uw Naam in",
                    Toast.LENGTH_SHORT).show();
            compleet = false;
        }

        if (tel.getText().length() > 8) { // Nederlandse telefoon nummbers
            modelsPersoonlijkInfo.setUserPhone(String.valueOf(tel.getText()));
            compleet = true;
        } else {
            Toast.makeText(this, "Voer uw tel in",
                    Toast.LENGTH_SHORT).show();
            compleet = false;

        }

        if (adres.getText().length() > 2) {
            modelsPersoonlijkInfo.setUserAddress(String.valueOf(adres.getText()));
            compleet = true;
        } else {
            Toast.makeText(this, "Voer uw Adres in",
                    Toast.LENGTH_SHORT).show();
            compleet = false;

        }


        if (mail.getText().length() > 2) {
            modelsPersoonlijkInfo.setUserEmail(String.valueOf(mail.getText()));
            compleet = true;
        } else {
            Toast.makeText(this, "Voer uw Email in",
                    Toast.LENGTH_SHORT).show();
            compleet = false;
        }
        return compleet;
    }

    public void aanvraag(View view) {


        if (modelsSettingsData.getisOnline() == true && getUserGegevens() == true) {
            getOrders();
        } else {
            Toast.makeText(this, R.string.noConnectionForOrder,
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Send order to server.
    public void getOrders() {
        JSONObject sendObject = new JSONObject();
        JSONArray userInformationArray = new JSONArray();
        JSONObject slotJSONObject = new JSONObject();
        try {
            slotJSONObject.put("slotnaam", modelsSlotList.getSlotenLijst().get(modelsSlotList.getSelectedSloten()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userInformationArray.put(slotJSONObject);

        JSONObject koperObject = new JSONObject();
        try {
            koperObject.put("kopernaam", modelsPersoonlijkInfo.getUsername());
            koperObject.put("koperadres", modelsPersoonlijkInfo.getUserAddress());
            koperObject.put("kopertelnr", modelsPersoonlijkInfo.getUserPhone());
            koperObject.put("koperemail", modelsPersoonlijkInfo.getUserEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userInformationArray.put(koperObject);

        try {
            sendObject.put("aanvraag", userInformationArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reply = "reply";
        {
            try {
                reply = new HelpersConnection(this, modelsSettingsData.getIpAddress(), 4444, sendObject.toString()).execute().get();

                orderComplete(reply);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void orderComplete(String string) {
        Toast.makeText(this, R.string.orderAccepted,
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }

    public void prevPage(View view) {
        Intent intent = new Intent(this, Sloten.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Load prev page
        Intent intent = new Intent(this, Sloten.class);
        startActivity(intent);
        finish();
    }

}
