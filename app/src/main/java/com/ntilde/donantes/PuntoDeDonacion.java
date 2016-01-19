package com.ntilde.donantes;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntilde.donantes.views.DonantesCalendarEvent;
import com.ntilde.donantes.views.DonantesCalendarRange;
import com.ntilde.donantes.views.DonantesCalendarView;
import com.ntilde.modelo.HorariosDonacion;
import com.ntilde.modelo.PuntosDonacion;
import com.ntilde.percentagelayout.PLinearLayout;
import com.ntilde.rest.ParseManager;
import com.ntilde.rest.response.ParseResponse;
import com.ntilde.utils.ParseConstantes;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PuntoDeDonacion extends ActionBarActivity implements ParseResponse{

    @InjectView(R.id.iconos_margen_superior)PLinearLayout ic_margen_sup;
    @InjectView(R.id.punto_de_donacion_logotipo)ImageView logotipo;
    @InjectView(R.id.punto_de_donacion_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.punto_de_donacion_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectView(R.id.punto_de_donacion_subtitulo) TextView subtitulo;
    @InjectView(R.id.calendar_view) DonantesCalendarView calendar;
    @InjectView(R.id.ubicacion_msg_seleccionar_fecha) TextView msg_horario;
    @InjectView(R.id.ubicacion_msg_direccion) TextView msg_direccion;

    private Map<Date,String> fechas;

    private ParseManager manager = DonantesApplication.getInstance().getParseManager();
    private String puntoDonacionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_punto_de_donacion);

        ButterKnife.inject(this);

        ic_margen_sup.post(new Runnable() {
            @Override
            public void run() {
                int valor = ic_margen_sup.getPHeight();
                logotipo.setPadding(valor, valor / 2, valor, valor / 2);
            }
        });

        borde_rojo_superior.post(() -> borde_rojo_inferior.getLayoutParams().height = borde_rojo_superior.getPHeight());

        calendar.setOnSelectedDateChangeListener(
                (selectedDate, event, range) -> {
                    if (selectedDate != null) {
                        if (event != null) {
                            msg_horario.setText("Horario: " + event.getEventInfo());
                        } else {
                            msg_horario.setText("Cerrado");
                        }
                    } else {
                        msg_horario.setText("Seleccione una fecha");
                    }
                }
        );

        subtitulo.setText(getIntent().getExtras().getString("puntoNombre"));
        msg_direccion.setText(getIntent().getExtras().getString("puntoDireccion"));
        recuperarPuntosDonacion();
    }


    public void recuperarPuntosDonacion(){
        Log.e("XXX","rpd1");
        String centroRegionalId = getIntent().getExtras().getString("puntoId");
        Log.e("XXX","rpd2:"+getIntent().getExtras().getString("puntoId"));
        manager.getPuntosDonacion(centroRegionalId, true, this);
        Log.e("XXX","rpd3");
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_punto_de_donacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ubicacion_como_llegar,R.id.ubicacion_llamar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ubicacion_como_llegar:
                break;
            case R.id.ubicacion_llamar:
                Intent llamar = new Intent("android.intent.action.DIAL");
                llamar.setData(Uri.parse("tel:" + getIntent().getExtras().getString("puntoTelefono")));
                startActivity(llamar);
                break;
        }
    }

    @Override
    public void onSuccess(int type, List result) {

        if(type == ParseConstantes.QUERY_PUNTO_DONACION){
            Log.e("XXX","Sucess punto");
            puntoDonacionId = ((PuntosDonacion)result).getObjectId();
            manager.getHorarios(puntoDonacionId,true, PuntoDeDonacion.this);
            return;
        }

        if(type == ParseConstantes.QUERY_HORARIOS_DONACION){
            Log.e("XXX","Sucess horarios");
            gestionarHorarios((List<HorariosDonacion>)result);
            return;
        }

    }

    private void gestionarHorarios(List<HorariosDonacion> horarios){
        for(HorariosDonacion horario:horarios){
            Log.e("XXX","Horario :)");
            Date inicio=horario.getFechaInicio();
            Date fin=horario.getFechaFin();
            String horas=horario.getHorario();
            Calendar inicioCal = createCalendar(inicio);
            Calendar finCal = createCalendar(fin);

            int duracionEnDias=(int)((finCal.getTimeInMillis()-inicioCal.getTimeInMillis())/(24*60*60*1000))+1;
            DonantesCalendarRange evento=new DonantesCalendarRange(inicioCal.getTime(), duracionEnDias, DonantesCalendarRange.UNITS.DAYS, Color.rgb(0, 128, 0));
            calendar.addEvent(new DonantesCalendarEvent(horas, evento));

        }
    }

    private Calendar createCalendar(Date date){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        resetTime(c1);
        return c1;
    }

    private void resetTime(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void onError(int type, int message) {

        if(type == ParseConstantes.QUERY_PUNTO_DONACION){
            //Gestionar fallo reintento
            Log.e("XXX","E1");
            return;
        }

        if(type == ParseConstantes.QUERY_HORARIOS_DONACION){
            //Gestionar fallo reintento
            Log.e("XXX","E2");
            return;
        }
    }

    @Override
    public void onLocalError(int type, int message) {

        if(type == ParseConstantes.QUERY_PUNTO_DONACION){
            manager.getPuntosDonacion(getIntent().getExtras().getString("puntoId"),false, PuntoDeDonacion.this);
            Log.e("XXX","E3");
            return;
        }

        if(type == ParseConstantes.QUERY_HORARIOS_DONACION){
            manager.getHorarios(puntoDonacionId,false, PuntoDeDonacion.this);
            Log.e("XXX","E4");
            return;
        }


    }
}
