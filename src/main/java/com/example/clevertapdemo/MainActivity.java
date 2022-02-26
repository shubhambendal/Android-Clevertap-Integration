package com.example.clevertapdemo;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CTInboxListener,DisplayUnitListener {
    Button createu,pushpbt,appinbox,getmsg,inappnotif,nativedisp,cleardisp;
    CardView c;
    TextView  text1,titlem,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
        CleverTapAPI.setDebugLevel(3);
        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();
        }
        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this);
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"245","ClevertapShubham","YourChannel", NotificationManager.IMPORTANCE_MAX,true);
        createu = findViewById(R.id.createuser);
        pushpbt = findViewById(R.id.pushnotification);
        text1 = findViewById(R.id.textv);
        appinbox = findViewById(R.id.appinbox);
        getmsg = findViewById(R.id.getmsg);
        inappnotif = findViewById(R.id.inappnotif);
        nativedisp = findViewById(R.id.nativedisp);
        c=findViewById(R.id.c1);
        titlem = findViewById(R.id.titlem);
        msg = findViewById(R.id.msg);
        cleardisp = findViewById(R.id.Clearnativedisplay);
        cleardisp.setOnClickListener(v -> {
            titlem.setText("");
            msg.setText("");
        });
        appinbox.setOnClickListener(v->{

                    ArrayList<String> tabs = new ArrayList<>();
                    tabs.add("Promotions");
                    tabs.add("Offers");//We support upto 2 tabs only. Additional tabs will be ignored

                    CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
                    styleConfig.setFirstTabTitle("First Tab");
                    styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
                    styleConfig.setTabBackgroundColor("#FF0000");
                    styleConfig.setSelectedTabIndicatorColor("#0000FF");
                    styleConfig.setSelectedTabColor("#0000FF");
                    styleConfig.setUnselectedTabColor("#FFFFFF");
                    styleConfig.setBackButtonColor("#FF0000");
                    styleConfig.setNavBarTitleColor("#FF0000");
                    styleConfig.setNavBarTitle("MY INBOX");
                    styleConfig.setNavBarColor("#FFFFFF");
                    styleConfig.setInboxBackgroundColor("#ADD8E6");
                    if (cleverTapDefaultInstance != null) {
                        cleverTapDefaultInstance.showAppInbox(styleConfig); //With Tabs
                    }
                    //ct.showAppInbox();//Opens Activity with default style configs
                });
        getmsg.setOnClickListener(v -> { clevertapDefaultInstance.pushEvent("shubhinbox"); });
        pushpbt.setOnClickListener(v -> { clevertapDefaultInstance.pushEvent("dashboardshub"); });
        inappnotif.setOnClickListener(v->{clevertapDefaultInstance.pushEvent("shubhinapnotif");});
        nativedisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clevertapDefaultInstance.pushEvent("shubhnativedisp");
            }
        });
        createu.setOnClickListener(view -> {
            // each of the below mentioned fields are optional
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", "Shubham Bendal");    // String
            //profileUpdate.put("Identity", 61026034);      // String or number
            profileUpdate.put("Email", "shubham@abc.com"); // Email address of the user
            profileUpdate.put("Phone", "+918451063899");   // Phone (with the country code, starting with +)
            profileUpdate.put("Gender", "M");             // Can be either M or F
            profileUpdate.put("DOB", new Date());         // Date of Birth. Set the Date object to the appropriate value first
            // optional fields. controls whether the user will be sent email, push etc.

            profileUpdate.put("MSG-email", true);        // Disable email notifications
            profileUpdate.put("MSG-push", true);          // Enable push notifications
            profileUpdate.put("MSG-sms", true);          // Disable SMS notifications
            profileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications
            ArrayList<String> stuff = new ArrayList<String>();
            stuff.add("bag");
            stuff.add("shoes");
            profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings
            String[] otherStuff = {"Jeans","Perfume"};
            profileUpdate.put("MyStuff", otherStuff);                   //String Array
            clevertapDefaultInstance.onUserLogin(profileUpdate);
            clevertapDefaultInstance.pushEvent("Product viewed");
            text1.setText("User Created");
        });
    }


    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        for (int i = 0; i <units.size() ; i++) {
            CleverTapDisplayUnit unit = units.get(i);
            prepareDisplayView(unit);
        }
    }

    private void prepareDisplayView(CleverTapDisplayUnit unit) {
        for (CleverTapDisplayUnitContent i:unit.getContents()) {
            titlem.setText(i.getTitle());
            msg.setText(i.getMessage());
            //Notification Viewed Event
            CleverTapAPI.getDefaultInstance(this).pushDisplayUnitViewedEventForID(unit.getUnitID());

            //Notification Clicked Event
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CleverTapAPI.getDefaultInstance(getApplicationContext()).pushDisplayUnitClickedEventForID(unit.getUnitID());

                }
            });
        }
    }

    @Override
    public void inboxDidInitialize() {
    }

    @Override
    public void inboxMessagesDidUpdate() {

    }
}