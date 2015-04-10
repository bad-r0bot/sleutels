package com.sleutels.Models;

/**
 * Created by Jim.
 */

public class ModelsPersoonlijkInfo {


    private static ModelsPersoonlijkInfo _instance;
    private String userNaam;
    private String userTel;
    private String userAdres;
    private String userMail;

    public static ModelsPersoonlijkInfo getInstance() {
        if (_instance == null)
            _instance = new ModelsPersoonlijkInfo();

        return _instance;
    }

    public String getUsername() {
        return userNaam;
    }

    public void setUserNaam(String userNaam) {
        this.userNaam = userNaam;
    }

    public String getUserEmail() {
        return userMail;
    }

    public void setUserEmail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPhone() {
        return userTel;
    }

    public void setUserPhone(String userTel) {
        this.userTel = userTel;
    }

    public String getUserAddress() {
        return userAdres;
    }

    public void setUserAddress(String userAdres) {
        this.userAdres = userAdres;
    }





}
