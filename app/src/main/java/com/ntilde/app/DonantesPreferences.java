package com.ntilde.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.ntilde.exception.InvalidValueType;
import com.ntilde.utils.DateUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Emanuel on 19/11/2015.
 */
public class DonantesPreferences {

    private static DonantesPreferences donantesPreferences  = null;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private DonantesPreferences(Context context){
        preferences = context.getSharedPreferences(Constantes.SP_KEY, context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static DonantesPreferences getPreferences(Context context){
        if(donantesPreferences == null){
            donantesPreferences = new DonantesPreferences(context);
        }

        return donantesPreferences;
    }

    public void put(String key, Object value) throws InvalidValueType{
        if(value == null) return;

        if(value instanceof String){
            putString(editor, key, (String) value);
        }

        if(value instanceof Integer){
            putInteger(editor, key, (Integer) value);
        }

        if(value instanceof Float){
            putFloat(editor, key, (Float) value);
        }

        if(value instanceof Boolean){
            putBoolean(editor, key, (Boolean) value);
        }

        if(value instanceof Long){
            putLong(editor, key, (Long) value);
        }


        if(isStringSet(value)){
            putStringSet(editor, key, (Set<String>)value);

        }

        if(!isValidType(value)){
            throw new InvalidValueType("Valor con tipo no almacenable en preferencias");
        }

    }

    public boolean isValidType(Object value){
        boolean success = false;
        success = success || value instanceof String;
        success = success || value instanceof Integer;
        success = success || value instanceof Float;
        success = success || value instanceof Boolean;
        success = success || value instanceof Long;
        success = success || isStringSet(value);

        return success;
    }

    private boolean isStringSet(Object value){
        return value instanceof Set && ((Set<Object>)value).iterator().hasNext() && ((Set<Object>)value).iterator().next() instanceof String;
    }

    public @Nullable Date getFechaActualizacion() throws InvalidValueType {
        return DateUtils.convertStringToDate(retrieveString());

    }

    private @Nullable String retrieveString() {
        return preferences.getString(Constantes.SP_ULTIMA_ACTUALIZACION, "");
    }

    public @Nullable String getIdCentroRegional() {
        return preferences.getString(Constantes.SP_CENTRO,"");
    }

    public @Nullable String getGrupoSanguineo(){
        return preferences.getString(Constantes.SP_GRUPO,"");
    }

    public @Nullable String getNumeroDonante(){
        return preferences.getString(Constantes.SP_NUMERO_DONANTE,"");
    }

    public boolean getNotificaciones(){
        return preferences.getBoolean(Constantes.SP_NOTIFICACIONES, true);
    }

    public @Nullable String getSexo(){
        return preferences.getString(Constantes.SP_SEXO, "");
    }

    public Set<String> getAlertas(){
        return preferences.getStringSet(Constantes.SP_ALERTAS, new HashSet<String>());
    }

    public Set<String> getDonaciones(){
        return preferences.getStringSet(Constantes.SP_DONACIONES, new HashSet<String>());
    }

    private void putString(SharedPreferences.Editor editor, String key, String value){
        editor.putString(key, value);
    }

    private void putInteger(SharedPreferences.Editor editor, String key, Integer value) {
        editor.putInt(key, value);
    }

    private void putFloat(SharedPreferences.Editor editor, String key, Float value) {
        editor.putFloat(key, value);
    }

    private void putBoolean(SharedPreferences.Editor editor, String key, Boolean value) {
        editor.putBoolean(key, value);
    }

    private void putLong(SharedPreferences.Editor editor, String key, Long value) {
        editor.putLong(key,value);
    }



    private void putStringSet(SharedPreferences.Editor editor, String key, Set<String> value) {
       editor.putStringSet(key,value);
    }

    public void commit(){
        editor.commit();
    }
    public void apply(){
        editor.apply();
    }
}
