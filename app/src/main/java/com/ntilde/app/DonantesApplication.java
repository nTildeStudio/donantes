package com.ntilde.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.ntilde.app.activities.MenuPrincipal;
import com.ntilde.domain.CentroRegional;
import com.ntilde.domain.HorariosDonacion;
import com.ntilde.domain.PuntosDonacion;
import com.ntilde.domain.UltimaActualizacion;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.ParseManagerImpl;
import com.parse.Parse;
import com.parse.ParseObject;

import io.fabric.sdk.android.Fabric;

/**
 * Created by 0011361 on 03/09/2015.
 */
public class DonantesApplication extends Application{

    private static DonantesApplication sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        registrarModeloLocalParse();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9qm1kVlIwYlGQ8ZBvJiAj6GEj7mfBpfLmE2eGCh0")
                .clientKey(null)
                .server("http://angeldearbo.ddns.net:9999/donantes")
                .enableLocalDataStore()
                .build());
//        PushService.setDefaultPushCallback(this, MenuPrincipal.class, R.drawable.ic_logotipo_blanco);
        Fabric.with(this, new Crashlytics());
    }

    public static DonantesApplication getInstance(){
        return sInstance;
    }

    public ParseManager getParseManager(){
        return new ParseManagerImpl();
    }

    public DonantesPreferences getPrefrences(){
        return DonantesPreferences.getPreferences(this);
    }

    /**
     * Método que se encarga de registrar las clases que serán utilizadas
     * para crear la copia local de los objetos de parse en dispositivo
     */
    public void registrarModeloLocalParse(){
        ParseObject.registerSubclass(UltimaActualizacion.class);
        ParseObject.registerSubclass(CentroRegional.class);
        ParseObject.registerSubclass(HorariosDonacion.class);
        ParseObject.registerSubclass(PuntosDonacion.class);
    }

}
