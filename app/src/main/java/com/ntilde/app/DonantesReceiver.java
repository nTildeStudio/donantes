package com.ntilde.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.ntilde.app.activities.Agenda;
import com.ntilde.exception.InvalidValueType;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 0011361 on 03/09/2015.
 */
public class DonantesReceiver extends ParsePushBroadcastReceiver{

    private static final String TAG = DonantesReceiver.class.getName();

    public static final String PARSE_DATA_KEY = "com.parse.Data";
    private DonantesPreferences preferences = DonantesApplication.getInstance().getPrefrences();


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        Log.i("XXX", "Notificación recibida");
        try {
            JSONObject data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));

            if(data.has("alert")){
                storeAlerts(data);

            }else{

                Calendar receivedDate = createDate(data);
                storeDonaciones(receivedDate,data);

                Intent notificationIntent = new Intent(context, Agenda.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        10, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

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

                    //For wearables -> Create the action and show donation date
                    String date = "";
                    if(data.has("fecha")) {
                        Date d = new Date(data.getLong("fecha"));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM");
                        date = sdf.format(d);
                    }
                    NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_agenda_white, date, contentIntent).build();

                    n = new NotificationCompat.Builder(context)
                            .setContentTitle("Calendario Donantes")
                            .setContentText("Se ha añadido una donación de " + data.getString("tipo"))
                            .setSmallIcon(R.drawable.ic_agenda)
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .extend(new android.support.v4.app.NotificationCompat.WearableExtender().addAction(action)) //Extra capabilities for Android Wear
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


    private void storeAlerts(JSONObject data) throws JSONException{
        try{
            Set<String> alertas=new HashSet<>(preferences.getAlertas());
            alertas.add(System.currentTimeMillis() + "::" + data.getString("alert"));
            preferences.put(Constantes.SP_ALERTAS, alertas);
            preferences.commit();

        }catch(InvalidValueType e){
            Log.e(TAG, "Error almacenando en preferencias");
        }
    }


    private Calendar createDate(JSONObject data) throws JSONException{

        Long received = Long.parseLong(data.getString("fecha"));
        Calendar receivedDate = Calendar.getInstance();
        receivedDate.setTimeInMillis(received);
        receivedDate.set(Calendar.HOUR, 0);
        receivedDate.set(Calendar.MINUTE, 0);
        receivedDate.set(Calendar.SECOND, 0);
        receivedDate.set(Calendar.MILLISECOND, 0);

        return  receivedDate;
    }

    private void storeDonaciones(Calendar receivedDate, JSONObject data) throws JSONException{

        try{
            Set<String> donacionesSet=new HashSet<>(preferences.getDonaciones());
            donacionesSet.add(receivedDate.getTimeInMillis() + "::" + data.getString("tipo"));
            preferences.put(Constantes.SP_DONACIONES, donacionesSet);
            preferences.commit();
        }catch (InvalidValueType e){
            Log.e(TAG,"Error almacenando donaciones");
        }
    }
}
