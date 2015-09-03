package com.ntilde.donantes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class SplashScreen extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, SplashScreen.MODE_PRIVATE);
                boolean ok=!"vacio".equals(prefs.getString(Constantes.SP_CENTRO,"vacio"));
                ok=ok&&!"vacio".equals(prefs.getString(Constantes.SP_GRUPO,"vacio"));

                if(ok){
                    startActivity(new Intent(SplashScreen.this, MenuPrincipal.class));
                }
                else {
                    startActivity(new Intent(SplashScreen.this, PrimerInicio.class));
                }
            }
        }, 1500);
    }

}
