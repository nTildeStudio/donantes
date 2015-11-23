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
import com.ntilde.percentagelayout.PLinearLayout;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
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
    @InjectView(R.id.calendar_view) DonantesCalendarView calendar;
    @InjectView(R.id.ubicacion_msg_seleccionar_fecha) TextView msg_horario;
    @InjectView(R.id.ubicacion_msg_direccion) TextView msg_direccion;

    private Map<Date,String> fechas;

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

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("PuntosDeDonacion");
        query1.getInBackground(getIntent().getExtras().getString("puntoId"), (object, e1) -> {
                if (e1 == null) {
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("HorariosDeDonacion").whereEqualTo("PuntoDeDonacion",object);
                    query2.findInBackground((horarios, e2) -> {
                            if(e2 == null) {
                                for(ParseObject horario:horarios){
                                    Date inicio=horario.getDate("FechaInicio");
                                    Date fin=horario.getDate("FechaFin");
                                    String horas=horario.getString("Horario");
                                    Calendar c1 = Calendar.getInstance();
                                    c1.setTime(inicio);
                                    c1.set(Calendar.HOUR_OF_DAY, 0);
                                    c1.set(Calendar.MINUTE, 0);
                                    c1.set(Calendar.SECOND, 0);
                                    c1.set(Calendar.MILLISECOND, 0);
                                    Calendar c2 = Calendar.getInstance();
                                    c2.setTime(fin);
                                    c2.set(Calendar.HOUR_OF_DAY,0);
                                    c2.set(Calendar.MINUTE, 0);
                                    c2.set(Calendar.SECOND,0);
                                    c2.set(Calendar.MILLISECOND,0);
                                    int duracionEnDias=(int)((c2.getTimeInMillis()-c1.getTimeInMillis())/(24*60*60*1000))+1;
                                    DonantesCalendarRange evento=new DonantesCalendarRange(c1.getTime(), duracionEnDias, DonantesCalendarRange.UNITS.DAYS, Color.rgb(0, 128, 0));
                                    calendar.addEvent(new DonantesCalendarEvent(horas, evento));
                                }
                            }
                        });
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
