package com.ntilde.donantes;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ntilde.Techniques.MenuIn;
import com.ntilde.modelo.CentroRegional;
import com.ntilde.percentagelayout.PImageView;
import com.ntilde.percentagelayout.PLinearLayout;
import com.ntilde.percentagelayout.PTextView;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.ParseQueryFactory;
import com.ntilde.rest.ParseResponse;
import com.ntilde.utils.ParseConstantes;
import com.parse.ParseAnalytics;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;


public class MenuPrincipal extends ActionBarActivity implements ParseResponse {

    @InjectViews({
            R.id.icono_ubicacion,
            R.id.icono_agenda,
            R.id.icono_informacion,
            R.id.icono_mensajes,
            R.id.icono_configuracion
    })
    List<PImageView> iconos;
    private int margenIzquierdo;

    @InjectView(R.id.iconos_margen_izquierdo) LinearLayout ic_margen_izq;
    @InjectView(R.id.iconos_margen_superior) PLinearLayout ic_margen_sup;
    @InjectView(R.id.home_cabecera) PLinearLayout home_cabecera;
    @InjectView(R.id.home_logotipo)ImageView logotipo;
    @InjectView(R.id.home_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.home_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectView(R.id.menu_principal_nombre_centro) PTextView nombre_centro;
    @InjectViews({
            R.id.info_ubicacion,
            R.id.info_agenda,
            R.id.info_informacion,
            R.id.info_mensajes,
            R.id.info_configuracion
    })
    List<PLinearLayout> mensajes;

    private DonantesPreferences prefs = DonantesApplication.getInstance().getPrefrences();
    private ParseManager manager = DonantesApplication.getInstance().getParseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);

        ic_margen_sup.post(() -> {
                int valor=ic_margen_sup.getPHeight();
                margenIzquierdo=valor;
                logotipo.setPadding(valor,valor/2,valor,valor/2);
                for(PLinearLayout mensaje:mensajes) {
                    mensaje.setPadding(valor * 2, 0, 0, 0);
                }
            });
        borde_rojo_superior.post(() -> borde_rojo_inferior.getLayoutParams().height=borde_rojo_superior.getPHeight());
        int delay=0;
        for(PImageView icono:iconos){
            icono.post(new MenuAnimatorRunnable(icono,delay));
            delay+=150;
        }
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(home_cabecera);


    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseQuery<CentroRegional> query = ParseQueryFactory.centroRegionalQuery(prefs.getIdCentroRegional());
        manager.recuperar(ParseConstantes.QUERY_CENTRO_REGIONAL, query, true, this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(0, R.anim.application_close);
    }


    @Override
    public void onSuccess(int type, List result) {
        CentroRegional centroRegional = ((List<CentroRegional>) result).get(0);
        nombre_centro.setText(centroRegional.getDescripcion());
        ((TextView)mensajes.get(3).getChildAt(0)).setText("Mensajes del "+ centroRegional.getNombre());
        ((TextView)mensajes.get(3).getChildAt(1)).setText("Vuelve a leer los mensajes del " + centroRegional.getNombre());

    }

    @Override
    public void onError(int message) {
        //TODO manage error
    }

    private class MenuAnimatorRunnable implements Runnable{

        private int delay=0;
        private PImageView image;

        public MenuAnimatorRunnable(PImageView i,int d){
            image=i;
            delay=d;
        }

        @Override
        public void run(){
            Display display=getWindowManager().getDefaultDisplay();
            Point size=new Point();
            display.getSize(size);
            MenuIn mi=new MenuIn();
            mi.setPivot(image.getPHeight()/2,image.getPWith()/2);
            mi.setDelay(delay);
            mi.setWindowWidth(size.x);
            mi.setLeftMargin(margenIzquierdo);
            YoYo.with(mi).playOn(image);
        }

    }

    @Override
    public void onBackPressed() {
    }

    @OnClick({
            R.id.icono_ubicacion,R.id.icono_agenda,R.id.icono_informacion,R.id.icono_mensajes,R.id.icono_configuracion,
            R.id.info_ubicacion,R.id.info_agenda,R.id.info_informacion,R.id.info_mensajes,R.id.info_configuracion
            })
    public void clickMenu(View elemento){


        Map<String, String> parameters = new HashMap<>();
        parameters.put("menuOption", ((String)elemento.getTag()).split("_")[0]);
        ParseAnalytics.trackEventInBackground("click", parameters);

        switch(((String)elemento.getTag()).split("_")[0]){
            case "ubicacion":
                startActivity(new Intent(this,Ubicacion.class));
                break;
            case "agenda":
                startActivity(new Intent(this,Agenda.class));
                break;
            case "informacion":
                startActivity(new Intent(this,Informacion.class));
                break;
            case "mensajes":
                startActivity(new Intent(this,Mensajes.class));
                break;
            case "configuracion":
                startActivity(new Intent(this, Configuracion.class));
                break;
        }
    }
}
