package com.ntilde.donantes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 0011361 on 03/09/2015.
 */
public class DonantesReceiver extends ParsePushBroadcastReceiver{

    public static final String PARSE_DATA_KEY = "com.parse.Data";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        Log.i("XXX", "Notificación recibida");
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));

            SharedPreferences prefs = context.getSharedPreferences(Constantes.SP_KEY, Configuracion.MODE_PRIVATE);

            Set<String> alertas=new HashSet<>(prefs.getStringSet(Constantes.SP_ALERTAS, new HashSet<String>()));

            alertas.add(System.currentTimeMillis() + "::" + data.getString("alert"));

            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(Constantes.SP_ALERTAS, alertas);
            editor.commit();

        } catch (JSONException e) {
            Log.i("XXX", "Error al parsear la notificacion");
        }
    }

}
