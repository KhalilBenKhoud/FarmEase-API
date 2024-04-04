package com.pi.farmease.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


@Service
public class SmsServiceImpl implements ISms {

    public static final String ACCOUNT_SID = "AC24f291ee40f61998746d19f99db8c2d2";
    public static final String AUTH_TOKEN = "98f06b98af890a66f1cb84ecda437cf8";
    public static final String OUTGOING_SMS_NUMBER = "+12055946787";




    @PostConstruct
    private void setup() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public String sendSms(String smsNumber, String smsMessage) {
        Message message = Message.creator(
                        new PhoneNumber(smsNumber),
                        new PhoneNumber(OUTGOING_SMS_NUMBER),
                        smsMessage )
                .create();

        return message.getStatus().toString();
    }


}