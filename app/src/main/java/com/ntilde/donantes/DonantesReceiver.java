package com.ntilde.donantes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
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
        try {
            JSONObject data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));

            if(data.has("alert")){
                SharedPreferences prefs = context.getSharedPreferences(Constantes.SP_KEY, Configuracion.MODE_PRIVATE);
                Set<String> alertas=new HashSet<>(prefs.getStringSet(Constantes.SP_ALERTAS, new HashSet<String>()));
                alertas.add(System.currentTimeMillis() + "::" + data.getString("alert"));

                SharedPreferences.Editor editor = prefs.edit();
                editor.putStringSet(Constantes.SP_ALERTAS, alertas);
                editor.commit();
            }else{
                Log.i("XXX", "Tipo: " + data.getString("tipo"));
                Log.i("XXX", "Fecha: " + data.getString("fecha"));

                Long received = Long.parseLong(data.getString("fecha"));
                Calendar receivedDate = Calendar.getInstance();
                receivedDate.setTimeInMillis(received);
                receivedDate.set(Calendar.HOUR, 0);
                receivedDate.set(Calendar.MINUTE, 0);
                receivedDate.set(Calendar.SECOND, 0);
                receivedDate.set(Calendar.MILLISECOND, 0);

                SharedPreferences prefs = context.getSharedPreferences(Constantes.SP_KEY, Agenda.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Set<String> donacionesSet=new HashSet<>(prefs.getStringSet(Constantes.SP_DONACIONES, new HashSet<String>()));
                donacionesSet.add(receivedDate.getTimeInMillis() + "::" + data.getString("tipo"));
                editor.putStringSet(Constantes.SP_DONACIONES, donacionesSet);
                editor.commit();

                Intent notificationIntent = new Intent(context, Agenda.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        10, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                Notification n;
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {

                    n = new Notification.Builder(context)
                            .setContentTitle("Calendario").setContentText("Se ha añadido una donación de " + data.getString("tipo"))
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setSound(alarmSound)
                            .getNotification();
                }else {
                    // build notification
                    // the addAction re-use the same intent to keep the example short
                    n = new Notification.Builder(context)
                            .setContentTitle("Calendario Donantes")
                            .setContentText("Se ha añadido una donación de " + data.getString("tipo"))
                            .setSmallIcon(R.drawable.ic_agenda)
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .setSound(alarmSound)
                            .build();
                }

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, n);



            }

        } catch (JSONException e) {
            Log.i("XXX", "Error al parsear la notificacion");
        }
    }

}
