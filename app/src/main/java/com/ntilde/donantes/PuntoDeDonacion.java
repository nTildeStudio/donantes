package com.ntilde.donantes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntilde.percentagelayout.PLinearLayout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PuntoDeDonacion extends ActionBarActivity {

    @InjectView(R.id.iconos_margen_superior)PLinearLayout ic_margen_sup;
    @InjectView(R.id.punto_de_donacion_logotipo)ImageView logotipo;
    @InjectView(R.id.punto_de_donacion_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.punto_de_donacion_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectView(R.id.punto_de_donacion_subtitulo) TextView subtitulo;
    @InjectView(R.id.calendar_view) CalendarPickerView calendar;
    @InjectView(R.id.ubicacion_msg_seleccionar_fecha) TextView msg_horario;
    @InjectView(R.id.ubicacion_msg_direccion) TextView msg_direccion;

    private Map<Date,String> fechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_punto_de_donacion);

        ButterKnife.inject(this);

        ic_margen_sup.post(new Runnable(){
            @Override
            public void run(){
                int valor=ic_margen_sup.getPHeight();
                logotipo.setPadding(valor,valor/2,valor,valor/2);
            }
        });

        borde_rojo_superior.post(new Runnable(){
            @Override
            public void run(){
                borde_rojo_inferior.getLayoutParams().height=borde_rojo_superior.getPHeight();
            }
        });

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        calendar.init(today, nextYear.getTime()).withSelectedDate(today);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                if(fechas !=null && fechas.containsKey(date)){
                    msg_horario.setText("Horario: "+fechas.get(date));
                }
                else{
                    msg_horario.setText("Cerrado");
                }
            }

            @Override
            public void onDateUnselected(Date date) {}
        });

        subtitulo.setText(getIntent().getExtras().getString("puntoNombre"));
        msg_direccion.setText(getIntent().getExtras().getString("puntoDireccion"));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("PuntosDeDonacion");
        query.getInBackground(getIntent().getExtras().getString("puntoId"), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("HorariosDeDonacion").whereEqualTo("PuntoDeDonacion",object);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> horarios, ParseException e) {
                            if(e == null) {
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
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
}
