package com.ntilde.donantes.models;

import com.parse.ParseFile;

/**
 * Created by Julio on 13/10/2015.
 */
public class CentroRegional {

    private String id;
    private String name;
    private ParseFile imagenCfg1;
    private ParseFile imagenCfg2;
    private int imagenCfg1Radius;
    private int imagenCfg2Radius;


    public CentroRegional(String id, String name, ParseFile imagenCfg1, ParseFile imagenCfg2, int imagenCfg1Radius, int imagenCfg2Radius) {
        this.id = id;
        this.name = name;
        this.imagenCfg1 = imagenCfg1;
        this.imagenCfg2 = imagenCfg2;
        this.imagenCfg1Radius = imagenCfg1Radius;
        this.imagenCfg2Radius = imagenCfg2Radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParseFile getImagenCfg1() {
        return imagenCfg1;
    }

    public void setImagenCfg1(ParseFile imagenCfg1) {
        this.imagenCfg1 = imagenCfg1;
    }

    public ParseFile getImagenCfg2() {
        return imagenCfg2;
    }

    public void setImagenCfg2(ParseFile imagenCfg2) {
        this.imagenCfg2 = imagenCfg2;
    }

    public Integer getImagenCfg1Radius() {
        return imagenCfg1Radius;
    }

    public void setImagenCfg1Radius(Integer imagenCfg1Radius) {
        this.imagenCfg1Radius = imagenCfg1Radius;
    }

    public Integer getImagenCfg2Radius() {
        return imagenCfg2Radius;
    }

    public void setImagenCfg2Radius(Integer imagenCfg2Radius) {
        this.imagenCfg2Radius = imagenCfg2Radius;
    }
}
