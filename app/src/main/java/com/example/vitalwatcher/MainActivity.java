package com.example.vitalwatcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize the AWSIotMqttManager with the configuration
        AWSIotMqttManager mqttManager = new AWSIotMqttManager(
                "51qb6pcerdkr196f4edigo8lqm",
                "a30kc1962iresi-ats.iot.ap-south-1.amazonaws.com");

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            public void onResult(UserStateDetails userStateDetails) {
                switch (userStateDetails.getUserState()) {
                    case SIGNED_IN:
                        break;
                    case SIGNED_OUT:
                        try {
                            AWSMobileClient.getInstance().showSignIn(MainActivity.this);
                        } catch (Exception e) {
                            Log.e("TAG", "", e);
                        }
                        break;
                    default:
                        Log.e("TAG", "Unhandled state see UserState for a list of states");
                        break;
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        try {

            mqttManager.connect(AWSMobileClient.getInstance(), new AWSIotMqttClientStatusCallback() {
                @Override
                public void onStatusChanged(final AWSIotMqttClientStatus status, final Throwable throwable) {
                    Log.d("LOG_TAG", "Connection Status: " + String.valueOf(status));
                }
            });
        } catch (final Exception e) {
            Log.e("LOG_TAG", "Connection error: ", e);
        }


//        try {
//            mqttManager.subscribeToTopic("myTopic", AWSIotMqttQos.QOS0 /* Quality of Service */,
//                    new AWSIotMqttNewMessageCallback() {
//                        @Override
//                        public void onMessageArrived(final String topic, final byte[] data) {
//                            try {
//                                String message = new String(data, "UTF-8");
//                                Log.d("LOG_TAG", "Message received: " + message);
//                            } catch (UnsupportedEncodingException e) {
//                                Log.e("LOG_TAG", "Message encoding error: ", e);
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            Log.e("LOG_TAG", "Subscription error: ", e);
//        }
//
//        try {
//            mqttManager.publishString("Hello to all subscribers!", "myTopic", AWSIotMqttQos.QOS0);
//        } catch (Exception e) {
//            Log.e("LOG_TAG", "Publish error: ", e);
//        }

    }

}