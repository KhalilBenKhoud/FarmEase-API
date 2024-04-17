package com.pi.farmease.utils;

import com.pi.farmease.entities.Insurance;

public class EmailUtility {
    public static String getEmailMessage(Insurance i){
        return "Hello "+ i.getUser().getFirstname()+" "+i.getUser().getLastname()
                +"\n\n Your insurance has been created . \n\n"
                + "It starts on: "+i.getStart_date() +"\n"
                + "And its active until: "+i.getEnd_date()
                +"\n\nThe Farmease support Team";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/user?token="+token;
    }
}