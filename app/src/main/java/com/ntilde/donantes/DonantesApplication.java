package com.ntilde.donantes;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.ntilde.modelo.CentroRegional;
import com.ntilde.modelo.HorariosDonacion;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.modelo.UltimaActualizacion;
import com.ntilde.rest.ParseManager;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

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

        //Registramos clases pertenecientes al modelo local
        registrarModeloLocalParse();
        //Habilitamos almacenamieto local de datos
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9qm1kVlIwYlGQ8ZBvJiAj6GEj7mfBpfLmE2eGCh0", "tD7aDDlGmcd2InKOho2g2KQCfg1OWUQhIfOdsAre");
        PushService.setDefaultPushCallback(this, MenuPrincipal.class, R.drawable.ic_logotipo_blanco);
        Fabric.with(this, new Crashlytics());
    }

    public static DonantesApplication getInstance(){
        return sInstance;
    }

    public ParseManager getParseManager(){
        return new ParseManager();
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
