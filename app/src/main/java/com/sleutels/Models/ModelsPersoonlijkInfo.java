package com.sleutels.Models;

/**
 * Created by Jim.
 */

public class ModelsPersoonlijkInfo {


    //singleton pattern
    private static ModelsPersoonlijkInfo _instance;

    public static ModelsPersoonlijkInfo getInstance() {
        if (_instance == null)
            _instance = new ModelsPersoonlijkInfo();

        return _instance;
    }
    private String userNaam;
    private String userTel;
    private String userAdres;
    private String userMail;

    public String getUserMail() {
        return userMail;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserAdres() {
        return userAdres;
    }

    public void setUserAdres(String userAdres) {
        this.userAdres = userAdres;
    }

    public String getUserNaam() {
        return userNaam;
    }

    public void setUserNaam(String userNaam) {
        this.userNaam = userNaam;
    }



}
