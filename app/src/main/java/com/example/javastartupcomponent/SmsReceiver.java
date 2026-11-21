package com.example.javastartupcomponent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus != null) {
                        for (Object pdu : pdus) {
                            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                            String sender = smsMessage.getDisplayOriginatingAddress();
                            String messageBody = smsMessage.getMessageBody();

                            // Automatically reply to the summary message / incoming SMS with an appropriate preparedness message
                            String replyMessage = "Earthquake Quiz Bot: Thank you for your message! We received your earthquake quiz summary response: \"" + messageBody + "\". Stay prepared, anchor heavy furniture, and always Drop, Cover, and Hold On!";

                            // Send auto-reply SMS back to sender
                            SmsManager smsManager;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                smsManager = context.getSystemService(SmsManager.class);
                            } else {
                                smsManager = SmsManager.getDefault();
                            }
                            smsManager.sendTextMessage(sender, null, replyMessage, null, null);

                            Toast.makeText(context, "Auto-replied to summary message from " + sender, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
