package com.ntilde.donantes;

import android.app.Application;
import android.util.Log;
import android.view.Menu;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by 0011361 on 03/09/2015.
 */
public class DonantesApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "9qm1kVlIwYlGQ8ZBvJiAj6GEj7mfBpfLmE2eGCh0", "tD7aDDlGmcd2InKOho2g2KQCfg1OWUQhIfOdsAre");
        PushService.setDefaultPushCallback(this, MenuPrincipal.class);
    }

}
