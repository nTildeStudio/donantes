package com.ntilde.donantes;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.PushService;

import io.fabric.sdk.android.Fabric;

/**
 * Created by 0011361 on 03/09/2015.
 */
public class DonantesApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Crash Reporting
        //ParseCrashReporting.enable(this);

        //Setup Parse
        Parse.initialize(this, "9qm1kVlIwYlGQ8ZBvJiAj6GEj7mfBpfLmE2eGCh0", "tD7aDDlGmcd2InKOho2g2KQCfg1OWUQhIfOdsAre");
        PushService.setDefaultPushCallback(this, MenuPrincipal.class, R.drawable.ic_logotipo_blanco);
        Fabric.with(this, new Crashlytics());
    }

}
