package com.sleutels.Models;

/**
 * Created by Jim
 */
import java.util.ArrayList;


public class ModelsSlotList {

    //singleton pattern
    private static ModelsSlotList _instance;

    public static ModelsSlotList getInstance() {
        if (_instance == null)
            _instance = new ModelsSlotList();

        return _instance;
    }

    private ArrayList<String> slotenLijst = new ArrayList<String>();

    private int selectedSloten;

    public void setSlotenLijst(ArrayList<String> slotenLijst) {
        this.slotenLijst = slotenLijst;
    }

    public ArrayList<String> getSlotenLijst() {
        return slotenLijst;
    }

    public void addSloten(String string){
        slotenLijst.add(string);
    }

    public void clearSloten(){
        slotenLijst.clear();
    }

    public void setCache(){
        slotenLijst.clear();
        slotenLijst.add("Sleutel slotsystemen");
        slotenLijst.add("RFID slotsystemen");
        slotenLijst.add("Biometrische slotsystemen");    }

    public int getSelectedSloten() {
        return selectedSloten;
    }

    public void setSelectedSloten(int selectedSloten) {
        this.selectedSloten = selectedSloten;
    }
}