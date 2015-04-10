package com.sleutels.Models;

/**
* Created by Jim
* Thanks to Raphael.
*/

public class ModelsSettings {


    private static ModelsSettings _instance;
    private boolean online = true;
    //Crashes if any text is present. Requires notification of that.
    private String ip4Adress = "127.0.0.1";

    public static ModelsSettings getInstance()
    {
        if( _instance == null )
            _instance = new ModelsSettings();

        return _instance;
    }

    public String getIpAddress()
    {
        return ip4Adress;
    }

    public void setIp4Adress(String ip4Adress) {
        this.ip4Adress = ip4Adress;
    }

    public boolean getisOnline() {
        return online;
    }

    public void isConnected(boolean online) {
        this.online = online;
    }

}
