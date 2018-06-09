package com.example.me1438.yoyo3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by me1438 on 2018-06-09.
 */

public class SMSReceiver extends BroadcastReceiver {
    String temp = "";
    static String str = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            StringBuilder sms = new StringBuilder();    // SMS문자를 저장할 곳
            Bundle bundle = intent.getExtras();         // Bundle객체에 문자를 받아온다

            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for (int i = 0; i < pdusObj.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    str = messages[i].getMessageBody().toString();
                }

                for (SmsMessage smsMessage : messages) {
                    sms.append(smsMessage.getMessageBody());
                }

                sms.toString(); // StringBuilder 객체 sms를 String으로 변환
                Toast.makeText(context, "yes" + str, Toast.LENGTH_SHORT).show();
                setString(str);

                Log.d("sms", sms.toString());
            }
        }
    }

    public void setString(String message) {
        temp = message;
    }

    public String getString() {
        return temp;
    }
}
