package com.ntilde.donantes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.listener.OnStateChangeListener;
import com.ntilde.donantes.utils.DefaultConfig;
import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashScreen extends ActionBarActivity{


    String svgPathBlack ="M 34.01,156.37\n" +
            "           C 32.81,156.37 31.90,155.70 31.34,154.68\n" +
            "             29.85,151.97 32.30,148.63 28.35,143.01\n" +
            "             23.13,135.60 10.89,131.40 13.39,120.00\n" +
            "             14.39,115.44 18.91,109.66 21.80,106.00\n" +
            "             21.80,106.00 40.91,84.00 40.91,84.00\n" +
            "             40.91,84.00 59.09,59.00 59.09,59.00\n" +
            "             71.82,43.32 84.89,33.00 103.00,24.26\n" +
            "             117.88,17.07 156.46,5.39 154.96,34.00\n" +
            "             153.91,53.97 121.34,52.14 123.11,34.00\n" +
            "             123.89,26.00 133.71,23.72 139.81,27.45\n" +
            "             143.35,29.65 142.81,33.38 139.81,34.19\n" +
            "             138.90,34.46 128.06,32.02 131.74,37.63\n" +
            "             134.64,42.04 145.46,42.55 147.55,34.00\n" +
            "             149.13,27.57 143.95,23.38 138.00,23.04\n" +
            "             129.56,22.56 122.93,25.78 115.00,27.00\n" +
            "             118.88,35.96 125.58,45.56 130.81,54.00\n" +
            "             143.11,73.85 162.72,102.04 163.00,126.00\n" +
            "             163.00,126.00 163.00,137.00 163.00,137.00\n" +
            "             162.88,147.15 158.79,157.54 151.91,165.00\n" +
            "             148.15,169.07 144.86,172.00 140.00,174.69\n" +
            "             133.54,178.27 128.41,180.38 121.00,181.00\n" +
            "             121.00,181.00 129.65,202.69 129.65,202.69\n" +
            "             133.52,206.89 139.66,203.25 141.87,207.30\n" +
            "             143.22,209.77 141.35,211.55 138.89,211.70\n" +
            "             135.79,211.89 126.88,210.78 124.70,208.49\n" +
            "             124.70,208.49 116.82,191.00 116.82,191.00\n" +
            "             115.88,188.69 114.98,185.38 112.73,184.01\n" +
            "             110.40,182.65 104.14,183.99 101.00,184.01\n" +
            "             95.12,183.99 85.67,182.64 80.00,181.00\n" +
            "             80.00,181.00 74.40,194.00 74.40,194.00\n" +
            "             73.54,196.10 72.40,199.67 70.50,200.98\n" +
            "             69.50,201.67 54.57,204.71 54.61,199.92\n" +
            "             54.64,195.42 63.41,197.07 66.35,194.40\n" +
            "             68.54,192.42 71.81,181.43 73.00,178.00\n" +
            "             64.59,175.54 54.02,167.70 48.46,161.00\n" +
            "             48.46,161.00 39.00,148.00 39.00,148.00\n" +
            "             38.70,150.83 37.90,156.37 34.01,156.37 Z\n" +
            "           M 68.59,58.00\n" +
            "           C 53.01,76.44 40.97,97.17 41.00,122.00\n" +
            "             41.04,153.56 66.99,176.95 98.00,177.00\n" +
            "             98.00,177.00 105.00,177.00 105.00,177.00\n" +
            "             124.96,176.97 147.84,168.57 154.64,148.00\n" +
            "             157.00,140.88 157.08,136.33 157.00,129.00\n" +
            "             156.85,116.11 151.91,104.33 146.25,93.00\n" +
            "             146.25,93.00 109.00,30.00 109.00,30.00\n" +
            "             96.27,32.39 76.82,48.26 68.59,58.00 Z\n" +
            "           M 122.60,91.72\n" +
            "           C 121.22,94.80 115.78,92.07 118.58,88.96\n" +
            "             120.16,87.16 123.93,88.89 122.60,91.72 Z\n" +
            "           M 99.04,96.78\n" +
            "           C 96.89,98.36 92.82,95.91 95.60,93.03\n" +
            "             98.93,90.27 100.91,95.18 99.04,96.78 Z\n" +
            "           M 24.16,115.00\n" +
            "           C 22.33,117.55 19.92,120.66 20.84,124.00\n" +
            "             22.03,128.28 28.59,132.39 31.82,135.43\n" +
            "             31.82,135.43 37.00,141.00 37.00,141.00\n" +
            "             37.00,141.00 35.00,126.00 35.00,126.00\n" +
            "             34.82,110.80 35.13,113.25 38.00,99.00\n" +
            "             33.27,102.90 27.79,109.95 24.16,115.00 Z\n" +
            "           M 77.00,148.07\n" +
            "           C 80.63,150.25 84.89,151.80 89.00,152.76\n" +
            "             93.99,153.94 103.87,153.31 101.75,157.74\n" +
            "             100.23,160.92 94.68,159.51 92.00,159.13\n" +
            "             82.17,157.75 74.74,155.14 67.00,148.68\n" +
            "             63.89,146.08 57.81,141.42 60.62,137.17\n" +
            "             65.34,136.65 72.40,145.31 77.00,148.07 Z";

    String svgPathRed = "M 157.00,134.00\n" +
            "           C 156.94,138.62 156.31,142.57 154.96,147.00\n" +
            "             147.07,172.81 120.16,177.27 97.00,177.00\n" +
            "             66.70,176.64 41.36,153.89 41.00,123.00\n" +
            "             40.72,98.57 51.42,78.46 66.61,60.00\n" +
            "             76.33,48.19 93.80,33.57 109.00,30.00\n" +
            "             109.00,30.00 132.40,69.00 132.40,69.00\n" +
            "             132.40,69.00 141.98,85.80 141.98,85.80\n" +
            "             136.61,79.11 129.22,77.00 121.00,80.35\n" +
            "             118.22,81.48 113.34,84.13 110.94,85.88\n" +
            "             108.66,84.78 103.16,82.35 100.00,81.49\n" +
            "             84.31,76.88 73.71,86.13 75.19,96.19\n" +
            "             77.00,107.13 91.17,109.92 99.00,112.28\n" +
            "             99.00,112.28 118.94,118.81 118.94,118.81\n" +
            "             122.00,116.56 123.81,115.16 126.00,113.52\n" +
            "             130.90,109.88 140.00,102.96 142.25,97.34\n" +
            "             143.60,93.97 143.16,87.72 141.97,85.78\n" +
            "             151.16,101.92 157.22,115.18 157.00,134.00 Z\n" +
            "           M 79.17,149.39\n" +
            "           C 72.01,145.32 65.65,136.61 60.57,137.13\n" +
            "             56.13,143.26 75.57,156.78 87.00,158.33\n" +
            "             94.57,159.52 102.17,161.70 102.09,156.17\n" +
            "             101.17,152.65 89.87,154.70 79.17,149.39 Z";

    String svgPathGris = "M 108.00,222.00\n" +
            "           C 86.22,222.10 68.38,222.34 47.00,216.29\n" +
            "             40.09,214.33 31.99,211.95 26.00,207.90\n" +
            "             22.62,205.61 19.37,202.52 20.33,198.02\n" +
            "             21.16,194.15 24.64,192.39 28.00,191.07\n" +
            "             34.93,188.35 45.54,187.01 53.00,187.00\n" +
            "             53.00,187.00 70.00,187.00 70.00,187.00\n" +
            "             72.65,187.17 74.30,187.05 79.00,187.35\n" +
            "             84.00,187.53 102.25,190.56 107.00,191.65\n" +
            "             109.78,192.29 115.57,193.56 117.78,195.09\n" +
            "             121.50,197.06 144.19,204.63 139.00,212.00\n" +
            "             132.33,220.33 118.14,221.95 108.00,222.00 Z";

    @InjectView(R.id.fillableLoader) FillableLoader fillableLoader;
    @InjectView(R.id.loaderRed) FillableLoader loaderRed;
    @InjectView(R.id.loaderGris) FillableLoader loaderGris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getParseConfig();

        ButterKnife.inject(this);

        fillableLoader.setSvgPath(svgPathBlack);
        loaderRed.setSvgPath(svgPathRed);
        loaderGris.setSvgPath(svgPathGris);

        fillableLoader.reset();
        fillableLoader.start();

        fillableLoader.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == 2) {
                    loaderRed.reset();
                    loaderRed.start();
                }
            }
        });

        loaderRed.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if(state == 3) {
                    loaderGris.reset();
                    loaderGris.start();
                }
            }
        });

        loaderGris.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == 3){
                    startActivity(new Intent(SplashScreen.this, FirstConfig.class));
                    SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, SplashScreen.MODE_PRIVATE);
                    boolean ok=!"vacio".equals(prefs.getString(Constantes.SP_CENTRO,"vacio"));
                    ok=ok&&!"vacio".equals(prefs.getString(Constantes.SP_GRUPO,"vacio"));
                    if(!ok){
                        startActivity(new Intent(SplashScreen.this, MenuPrincipal.class));
                    }
                    else {
                        startActivity(new Intent(SplashScreen.this, FirstConfig.class));
                    }
                }
            }
        });


    }

    private void getParseConfig() {
        Log.i("XXX", "Vamos a pedir las settings");

        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, ParseException e) {
                if(e == null){
                    Log.i("XXX", "Recogida la configuración por defecto!");
                    DefaultConfig.ImgCfg1 = parseConfig.getParseFile("ImagenCfg1"); if(DefaultConfig.ImgCfg1==null){Log.i("XXX", "Null1");}else{Log.i("XXX", "1: " + DefaultConfig.ImgCfg1.getUrl());}
                    DefaultConfig.ImgCfg2 = parseConfig.getParseFile("ImagenCfg2"); if(DefaultConfig.ImgCfg2==null){Log.i("XXX", "Null2");}else{Log.i("XXX", "1: " + DefaultConfig.ImgCfg2.getUrl());}
                    DefaultConfig.ImgCfg1Radius = parseConfig.getInt("ImagenCfg1Radio"); if(DefaultConfig.ImgCfg1Radius==0){Log.i("XXX", "Null3");}else{Log.i("XXX", "1: " + DefaultConfig.ImgCfg1Radius);}
                    DefaultConfig.ImgCfg2Radius = parseConfig.getInt("ImagenCfg2Radio"); if(DefaultConfig.ImgCfg2Radius==0){Log.i("XXX", "Null4");}else{Log.i("XXX", "1: " + DefaultConfig.ImgCfg2Radius);}
                }else{
                    Log.e("XXX", "Error al obtener la configuración de parse");
                }
            }
        });
    }
}
