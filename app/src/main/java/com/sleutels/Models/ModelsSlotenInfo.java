package com.sleutels.Models;

/**
 * Created by Jim
 */
public class ModelsSlotenInfo {
    //singleton pattern
    private static ModelsSlotenInfo _instance;

    public static ModelsSlotenInfo getInstance() {
        if (_instance == null)
            _instance = new ModelsSlotenInfo();

        return _instance;
    }
    private String infoSloten;

    public String getinfoSloten() {
        return infoSloten;
    }

    public void setinfoSloten(String infoSloten) {
        this.infoSloten = infoSloten;
    }

    public void setInfoSlotenHardCoded(String string){

        this.infoSloten = string;
    }
}