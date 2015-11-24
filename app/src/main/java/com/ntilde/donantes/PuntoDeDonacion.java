package com.ntilde.donantes;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

        ic_margen_sup.post(new Runnable(){
            @Override
            public void run(){
                int valor=ic_margen_sup.getPHeight();
                logotipo.setPadding(valor,valor/2,valor,valor/2);
            }
        });

        borde_rojo_superior.post(() -> borde_rojo_inferior.getLayoutParams().height = borde_rojo_superior.getPHeight());

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Calendar pastYear = Calendar.getInstance();
        pastYear.add(Calendar.YEAR, -1);

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

            @Override
            public void onDateUnselected(Date date) {}
        });

        subtitulo.setText(getIntent().getExtras().getString("puntoNombre"));
        msg_direccion.setText(getIntent().getExtras().getString("puntoDireccion"));
        recuperarPuntosDonacion();

       cambiar ParseQuery<ParseObject> query1 = ParseQuery.getQuery("PuntosDeDonacion");
        query1.getInBackground(getIntent().getExtras().getString("puntoId"), (object, e1) -> {
                if (e1 == null) {
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("HorariosDeDonacion").whereEqualTo("PuntoDeDonacion",object);
                    query2.findInBackground((horarios, e2) -> {
                            if(e2 == null) {
                                for(ParseObject horario:horarios){
                                    Date inicio=horario.getDate("FechaInicio");
                                    Date fin=horario.getDate("FechaFin");
                                    String horas=horario.getString("Horario");
                                    Calendar cal = Calendar.getInstance();
                                    fechas=new HashMap<>();
                                    do{
                                        cal.setTime(inicio);
                                        cal.add(Calendar.DATE, 1);
                                        cal.set(Calendar.HOUR_OF_DAY,0);
                                        cal.set(Calendar.MINUTE,0);
                                        cal.set(Calendar.SECOND,0);
                                        cal.set(Calendar.MILLISECOND,0);
                                        inicio = cal.getTime();
                                        fechas.put(inicio,horas);
                                    }while(inicio.getTime()<fin.getTime());
                                    calendar.clearHighlightedDates();
                                    calendar.highlightDates(fechas.keySet());
                                }
                            }
                        });
                }
            });
    }


    public void recuperarPuntosDonacion(){
        String centroRegionalId = getIntent().getExtras().getString("puntoId");
        manager.getPuntosDonacion(centroRegionalId, true, this);
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
            puntoDonacionId = ((PuntosDonacion)result).getObjectId();
            manager.getHorarios(puntoDonacionId,true, PuntoDeDonacion.this);
            return;
        }

        if(type == ParseConstantes.QUERY_HORARIOS_DONACION){
            gestionarHorarios((List<HorariosDonacion>)result);
            return;
        }

    }

    private void gestionarHorarios(List<HorariosDonacion> horarios){

        for(HorariosDonacion horario:horarios){
            Date inicio=horario.getFechaInicio();
            Date fin=horario.getFechaFin();
            String horas=horario.getHorario();
            crearHorarios(inicio,fin,horas);

        }
    }

    private Calendar crearHorarios(Date inicio, Date fin, String horas){
        Calendar cal = Calendar.getInstance();
        fechas=new HashMap<>();
        do{
            cal.setTime(inicio);
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            inicio = cal.getTime();
            fechas.put(inicio,horas);
        }while(inicio.getTime()<fin.getTime());

        return cal;
    }

    @Override
    public void onError(int type, int message) {

        if(type == ParseConstantes.QUERY_PUNTO_DONACION){
            //Gestionar fallo reintento
            return;
        }

        if(type == ParseConstantes.QUERY_HORARIOS_DONACION){
            //Gestionar fallo reintento
            return;
        }
    }

    @Override
    public void onLocalError(int type, int message) {

        if(type == ParseConstantes.QUERY_PUNTO_DONACION){
            manager.getPuntosDonacion(getIntent().getExtras().getString("puntoId"),false, PuntoDeDonacion.this);
            return;
        }

        if(type == ParseConstantes.QUERY_HORARIOS_DONACION){
            manager.getHorarios(puntoDonacionId,false, PuntoDeDonacion.this);
            return;
        }


    }
}
