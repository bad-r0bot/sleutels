package com.sleutels.Models;

/**
 * Created by Jim
 */
public class ModelsSlotenInfoBeknopt {


    //singleton pattern
    private static ModelsSlotenInfoBeknopt _instance;

    public static ModelsSlotenInfoBeknopt getInstance() {
        if (_instance == null)
            _instance = new ModelsSlotenInfoBeknopt();

        return _instance;
    }
    private String shortInfoSloten;

    public String getShortInfoSloten() {
        return shortInfoSloten;
    }

    public void setShortInfoSloten(String shortInfoSloten) {
        this.shortInfoSloten = shortInfoSloten;
    }

    public void setShortInfoSlotenHardCoded(String string){

        this.shortInfoSloten = string;

    }
}
