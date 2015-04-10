package com.sleutels;

/**
 * Created by Jim
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sleutels.Helpers.HelpersConnection;
import com.sleutels.Models.ModelsSettings;
import com.sleutels.Models.ModelsSlotList;
import com.sleutels.Models.ModelsSlotenInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Sloten extends Activity {

    // variablen
    TextView slotenTag, slotenInfo, headerText;

    // instances
    ModelsSettings modelsSettingsData = ModelsSettings.getInstance();
    ModelsSlotList modelsSlotList = ModelsSlotList.getInstance();
    ModelsSlotenInfo modelsSlotenInfo = ModelsSlotenInfo.getInstance();

    Typeface opensans, font2;
    Button backButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_info);

        // setup
        opensans = Typeface.createFromAsset(getAssets(), "fonts/fontstyle.ttf");
        font2 = Typeface.createFromAsset(getAssets(), "fonts/fontstyle2.ttf");

        slotenTag = (TextView) findViewById(R.id.slotenTextViewsloten);
        slotenInfo = (TextView) findViewById(R.id.SlotenInfoLong);
        headerText = (TextView) findViewById(R.id.slotenhead);
        backButton = (Button) findViewById(R.id.backButtonsloten);
        nextButton = (Button) findViewById(R.id.nextButtonsloten);

        headerText.setTypeface(opensans);
        backButton.setTypeface(font2);
        nextButton.setTypeface(font2);

        backButton.setText(R.string.leftSide); // Tiny left/right arrows
        nextButton.setText(R.string.rightSide); // Tiny left/right arrows


        headerText.setTypeface(opensans);

        // controleert of er live verbinding is met de server, zo niet dan worden de gegevens uit de app gehaald. LET OP data kan verouderd zijn.
        if (modelsSettingsData.getisOnline() == true) {
            getSlotenInfoLong();
        } else {
            //
            String serverCache[] = new String[3];
            serverCache[0] = "De meest eenvoudige vorm van beveiliging zijn sloten die per sleutel geopend kunnen worden. Dit is een relatief goedkoop systeem maar brengt enige veiligheidsproblemen met zich mee. Voor beveiliging van ruimtes die weinig waardevolle zaken bevatten is dit systeem een uitstekende uitkomst.";

            serverCache[1] = "RFID authenticatie is met de huidige techniek snel en gemakkelijk te implementeren om elke ruimte af te sluiten. Dankzij de digitalisering kunnen gebruikers getraceerd worden en bestaat de mogelijkheid een geschiedenis van ruimtegebruik aan te leggen. Dit resulteert in verhoogde beveiliging.";

            serverCache[2] = "Biometrische authenticatie en authorizatie is de meest geavanceerde vorm van beveiliging. Dit systeem werkt nagenoeg feilloos en garandeerd een zeer hoge veiligheid van uw waardevolle bezittingen. Biometrische authorizatie kan op verschillende niveaus worden toegepast, van vingerafdruk tot irisscan.";

            modelsSlotenInfo.setInfoSlotenHardCoded(serverCache[modelsSlotList.getSelectedSloten()]);

            /*
            slotInfo.put( "Sleutel slotsystemen", "De meest eenvoudige vorm van beveiliging zijn sloten die per sleutel geopend kunnen worden. Dit is een relatief goedkoop systeem maar brengt enige veiligheidsproblemen met zich mee. Voor beveiliging van ruimtes die weinig waardevolle zaken bevatten is dit systeem een uitstekende uitkomst." );
            slotInfo.put( "RFID slotsystemen", "RFID authenticatie is met de huidige techniek snel en gemakkelijk te implementeren om elke ruimte af te sluiten. Dankzij de digitalisering kunnen gebruikers getraceerd worden en bestaat de mogelijkheid een geschiedenis van ruimtegebruik aan te leggen. Dit resulteert in verhoogde beveiliging." );
            slotInfo.put( "Biometrische slotsystemen", "Biometrische authenticatie en authorizatie is de meest geavanceerde vorm van beveiliging. Dit systeem werkt nagenoeg feilloos en garandeerd een zeer hoge veiligheid van uw waardevolle bezittingen. Biometrische authorizatie kan op verschillende niveaus worden toegepast, van vingerafdruk tot irisscan." );

            slotInfoBeknopt.put( "Sleutel slotsystemen", "Eenvoudig, snel en goedkoop uw bezit achter slot en grendel!" );
            slotInfoBeknopt.put( "RFID slotsystemen", "Digitale beveiliging voor 100% zekerheid!" );
            slotInfoBeknopt.put( "Biometrische slotsystemen", "Authenticatie en authorizatie op persoonlijk niveau!" );
            */
        }

        slotenInfo.setText(modelsSlotenInfo.getinfoSloten());
        slotenTag.setText(modelsSlotList.getSlotenLijst().get(modelsSlotList.getSelectedSloten()));
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

    // Try to retrieve full information for selected lock.
    public void getSlotenInfoLong() {
        JSONObject infoObject = new JSONObject();

        try {
            infoObject.put("informatie", (modelsSlotList.getSlotenLijst().get(modelsSlotList.getSelectedSloten())));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reactie = "string";
        {
            try {
                reactie = new HelpersConnection(this, modelsSettingsData.getIpAddress(), 4444, infoObject.toString()).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            try {
                JSONObject info = new JSONObject(reactie);
                String infoString = info.getString("informatie");
                modelsSlotenInfo.setinfoSloten(infoString);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextPage(View view) {
        // Next page
        Intent intent = new Intent(this, Ordering.class);
        startActivity(intent);
        finish();
    }

    public void prevPage(View view) {
        // Previous page
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Back button action
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }
}
