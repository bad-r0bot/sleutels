package com.sleutels;

/**
 * Created by Jim on 10/04/2015.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.sleutels.Helpers.HelpersConnection;
import com.sleutels.Models.ModelsSettings;
import com.sleutels.Models.ModelsSlotList;
import com.sleutels.Models.ModelsSlotenInfoBeknopt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainMenu extends Activity implements AdapterView.OnItemSelectedListener {

    public static final String preferences = "preference";
    public static final String position = "position";
    // Required variables
    TextView bedrijfInformatie, slotTitle, slotInformatieKort, titleText;
    Button buttonNext;
    Spinner spinnerMenu;
    Typeface fontstyle2, fontstyle;
    // Interface for accessing and modifying preference data returned by getSharedPreferences(String, int)
    SharedPreferences sharedpreferences;
    // Instances
    ModelsSettings modelsSettingsData = ModelsSettings.getInstance();
    ModelsSlotList modelsSlotList = ModelsSlotList.getInstance();
    ModelsSlotenInfoBeknopt beknoptSlotInfo = ModelsSlotenInfoBeknopt.getInstance();
    int i = 0; // Set i to 0.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Save instance
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Required setting of views, fonts, and texts.
        bedrijfInformatie = (TextView) findViewById(R.id.bedrijfInformatieTextView);
        slotTitle = (TextView) findViewById(R.id.slotTitleTextView);
        slotInformatieKort = (TextView) findViewById(R.id.slotenInformatieKort);
        titleText = (TextView) findViewById(R.id.titleText);
        buttonNext = (Button) findViewById(R.id.nextButton);
        spinnerMenu = (Spinner) findViewById(R.id.spinner);
        sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        fontstyle2 = Typeface.createFromAsset(getAssets(), "fonts/fontstyle.ttf");
        fontstyle = Typeface.createFromAsset(getAssets(), "fonts/fontstyle2.ttf");
        titleText.setTypeface(fontstyle2);
        buttonNext.setTypeface(fontstyle);
        buttonNext.setText(R.string.rightSide);

        // This gets slotenlijst.
        ArrayList<String> list;

        list = modelsSlotList.getSlotenLijst();

        // Fill spinner with items in list above.
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinnerusage, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMenu.setAdapter(dataAdapter);
        addListenerOnSpinnerItemSelection();
    }

    // Add data to the spinner.
    public void addListenerOnSpinnerItemSelection() {
        spinnerMenu.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate main_menu menu.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int positionLocation, long id) {

        int savedPosition = sharedpreferences.getInt(position, 0);

        // Called at the start of activity to load saved position from SharedPreference.
        if (i < 1) {
            i++;
            spinnerMenu.setSelection(savedPosition);
            positionLocation = savedPosition;
        }
        // Gebeurd na de eerste keer laden
        else {
            setSelectedSlot();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(position, positionLocation);
            editor.commit();
        }

        modelsSlotList.setSelectedSloten(positionLocation);
        setSelectedSlot();
    }

    public void setSelectedSlot() {

        if (modelsSettingsData.getisOnline() == true) {
            getSlotInfoBeknopt();
        } else {
            String hardCoded[] = new String[3];
            hardCoded[0] = "Eenvoudig, snel en goedkoop uw bezit achter slot en grendel!";
            hardCoded[1] = "Digitale beveiliging voor 100% zekerheid!";
            hardCoded[2] = "Authenticatie en authorizatie op persoonlijk niveau!";

            beknoptSlotInfo.setShortInfoSlotenFromCache(hardCoded[modelsSlotList.getSelectedSloten()]);

            /*
             slotInfo = new HashMap<String,String>();
             slotInfoBeknopt = new HashMap<String,String>();

             slotInfo.put( "Sleutel slotsystemen", "De meest eenvoudige vorm van beveiliging zijn sloten die per sleutel geopend kunnen worden. Dit is een relatief goedkoop systeem maar brengt enige veiligheidsproblemen met zich mee. Voor beveiliging van ruimtes die weinig waardevolle zaken bevatten is dit systeem een uitstekende uitkomst." );
             slotInfo.put( "RFID slotsystemen", "RFID authenticatie is met de huidige techniek snel en gemakkelijk te implementeren om elke ruimte af te sluiten. Dankzij de digitalisering kunnen gebruikers getraceerd worden en bestaat de mogelijkheid een geschiedenis van ruimtegebruik aan te leggen. Dit resulteert in verhoogde beveiliging." );
             slotInfo.put( "Biometrische slotsystemen", "Biometrische authenticatie en authorizatie is de meest geavanceerde vorm van beveiliging. Dit systeem werkt nagenoeg feilloos en garandeerd een zeer hoge veiligheid van uw waardevolle bezittingen. Biometrische authorizatie kan op verschillende niveaus worden toegepast, van vingerafdruk tot irisscan." );

             slotInfoBeknopt.put( "Sleutel slotsystemen", "Eenvoudig, snel en goedkoop uw bezit achter slot en grendel!" );
             slotInfoBeknopt.put( "RFID slotsystemen", "Digitale beveiliging voor 100% zekerheid!" );
             slotInfoBeknopt.put( "Biometrische slotsystemen", "Authenticatie en authorizatie op persoonlijk niveau!" );

             */
        }

        // Set text to title and information of slot #.
        slotTitle.setText(modelsSlotList.getSlotenLijst().get(modelsSlotList.getSelectedSloten()));

        slotInformatieKort.setText(beknoptSlotInfo.getShortInfoSloten());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // Get beknopt informatie for slot #.
    public void getSlotInfoBeknopt() {

        JSONObject infoObject = new JSONObject();

        try {
            infoObject.put("informatiebeknopt", (modelsSlotList.getSlotenLijst().get(modelsSlotList.getSelectedSloten())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reply = "reply";
        {
            try {
                reply = new HelpersConnection(this, modelsSettingsData.getIpAddress(), 4444, infoObject.toString()).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            try {
                JSONObject shortInfo = new JSONObject(reply);

                String shortInfoString = shortInfo.getString("informatiebeknopt");

                beknoptSlotInfo.setShortInfoSloten(shortInfoString);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Go to next page.
    public void goToSlotenPage(View view) {

        Intent intent = new Intent(this, Sloten.class);
        startActivity(intent);
        finish();
    }

    // Load previous page.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Sloten.class);
        startActivity(intent);
        finish();
    }
}
