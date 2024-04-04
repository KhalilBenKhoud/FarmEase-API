package com.pi.farmease.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SmsServiceImpl implements ISms {
@Value("${ACCOUNT_SID}")
    public  String ACCOUNT_SID ;

@Value("${AUTH_TOKEN}")
    public  String AUTH_TOKEN ;

@Value("${OUTGOING_SMS_NUMBER}")
    public  String OUTGOING_SMS_NUMBER ;




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