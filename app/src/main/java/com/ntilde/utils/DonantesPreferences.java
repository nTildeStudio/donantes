package com.ntilde.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ntilde.donantes.Constantes;
import com.ntilde.exception.InvalidValueType;
import com.ntilde.exception.NonStoredValue;

import java.util.Date;
import java.util.Set;

/**
 * Created by Emanuel on 19/11/2015.
 */
public class DonantesPreferences {

    private static DonantesPreferences donantesPreferences  = null;

    private SharedPreferences preferences;

    private DonantesPreferences(Context context){
        preferences = context.getSharedPreferences(Constantes.SP_KEY, context.MODE_PRIVATE);
    }

    public static DonantesPreferences getPreferences(Context context){
        if(donantesPreferences == null){
            donantesPreferences = new DonantesPreferences(context);
        }

        return donantesPreferences;
    }

    public void put(String key, Object value) throws InvalidValueType{

        SharedPreferences.Editor editor = preferences.edit();

        if(isString(value)){
            putString(editor, key, (String) value);
        }

        if(isInteger(value)){
            putInteger(editor, key, (Integer) value);
        }

        if(isFloat(value)){
            putFloat(editor, key, (Float) value);
        }

        if(isBoolean(value)){
            putBoolean(editor, key, (Boolean) value);
        }

        if(isLong(value)){
            putLong(editor, key, (Long) value);
        }

        if(isStringSet(value)){
            putStringSet(editor, key, (Set<String>)value);

        }else{
            throw new InvalidValueType("Valor con tipo no almacenable en preferencias");
        }

        commit(editor);

    }

    public Date getFechaActualizacion() throws NonStoredValue, InvalidValueType {

        Date fecha = Utils.convertStringToDate(retrieveString());

        if(fecha == null){
            throw new InvalidValueType("El tipo pasado no es una fecha");
        }

        return fecha;
    }

    private String retrieveString() throws NonStoredValue {
        String fechaString = preferences.getString(Constantes.SP_ULTIMA_ACTUALIZACION, "");

        if(fechaString == null){
            throw new NonStoredValue("El valor no está almacenado");
        }

        return fechaString;
    }

    public String getIdCentroRegional() throws NonStoredValue{

        String idCentroRegional = preferences.getString(Constantes.SP_CENTRO,"");

        if(idCentroRegional == null){
            throw  new NonStoredValue("No hay centro regional almacenado");
        }

        return idCentroRegional;
    }


    private boolean isString(Object value){
        return value instanceof String;
    }

    private void putString(SharedPreferences.Editor editor, String key, String value){
        editor.putString(key, value);
    }

    private boolean isInteger(Object value){
        return value instanceof Integer;
    }

    private void putInteger(SharedPreferences.Editor editor, String key, Integer value) {
        editor.putInt(key,value);
    }

    private boolean isFloat(Object value){
        return value instanceof Float;
    }

    private void putFloat(SharedPreferences.Editor editor, String key, Float value) {
        editor.putFloat(key,value);
    }


    private boolean isBoolean(Object value){
        return value instanceof Boolean;
    }

    private void putBoolean(SharedPreferences.Editor editor, String key, Boolean value) {
        editor.putBoolean(key,value);
    }

    private boolean isLong(Object value){
        return value instanceof Long;
    }

    private void putLong(SharedPreferences.Editor editor, String key, Long value) {
        editor.putLong(key,value);
    }

    private boolean isStringSet(Object value){
        return value instanceof Set && ((Set<Object>)value).iterator().hasNext() && ((Set<Object>)value).iterator().next() instanceof String;
    }


    private void putStringSet(SharedPreferences.Editor editor, String key, Set<String> value) {
       editor.putStringSet(key,value);
    }

    private void commit(SharedPreferences.Editor editor){
        editor.commit();
    }
}
