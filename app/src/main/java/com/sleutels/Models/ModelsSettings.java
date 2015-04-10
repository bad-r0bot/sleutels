package com.sleutels.Models;

/**
* Created by Jim
* Thanks to Raphael.
*/

public class ModelsSettings {


    //singleton pattern
    private static ModelsSettings _instance;
    public static ModelsSettings getInstance()
    {
        if( _instance == null )
            _instance = new ModelsSettings();

        return _instance;
    }

    private boolean online = true;
    private String ip4Adress = "127.0.0.1"; //Crashes if any text is present. Requires notification of that.

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
