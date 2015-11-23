package com.ntilde.donantes;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntilde.donantes.views.DonantesCalendarEvent;
import com.ntilde.donantes.views.DonantesCalendarRange;
import com.ntilde.donantes.views.DonantesCalendarView;
import com.ntilde.percentagelayout.PLinearLayout;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;


public class Agenda extends AppCompatActivity {

    @InjectView(R.id.iconos_margen_superior) PLinearLayout ic_margen_sup;
    @InjectView(R.id.configuracion_logotipo)ImageView logotipo;
    @InjectView(R.id.configuracion_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.configuracion_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectView(R.id.calendar_view) DonantesCalendarView calendar;
    @InjectView(R.id.agenda_msg_seleccionar_fecha) TextView msgSeleccioneFecha;
    @InjectViews({R.id.agenda_no_done, R.id.agenda_done_plaquetas, R.id.agenda_done_plasma, R.id.agenda_done_sangre}) List<Button> tiposDonaciones;

    private Map<Date,String> donacionesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_agenda);

        ButterKnife.inject(this);

        ic_margen_sup.post(() -> {
                int valor = ic_margen_sup.getPHeight();
                logotipo.setPadding(valor, valor / 2, valor, valor / 2);
            });

        borde_rojo_superior.post(() -> borde_rojo_inferior.getLayoutParams().height = borde_rojo_superior.getPHeight());

        Date firstDate = null, lastDate = null;

        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Agenda.MODE_PRIVATE);
        Set<String> donaciones = new HashSet<>(prefs.getStringSet(Constantes.SP_DONACIONES, new HashSet<>()));

        for(String donacion:donaciones){
            Date donacionDate=new Date(Long.parseLong(donacion.split("::")[0]));
            String tipo=donacion.split("::")[1];
            DonantesCalendarRange evento = new DonantesCalendarRange(donacionDate, 1, DonantesCalendarRange.UNITS.DAYS, Color.rgb(0, 128, 0));
            DonantesCalendarRange restriccion1 = new DonantesCalendarRange(3, DonantesCalendarRange.UNITS.MONTHS, Color.rgb(255, 221, 85), "No puedes donar sangre");
            DonantesCalendarRange restriccion2 = new DonantesCalendarRange(1, DonantesCalendarRange.UNITS.MONTHS, Color.rgb(212, 0, 0), "No puedes donar");
            calendar.addEvent(new DonantesCalendarEvent(tipo, evento, restriccion1, restriccion2));
        }

        calendar.setOnSelectedDateChangeListener((selectedDate, event, range) -> {
                if(selectedDate!=null){
                    if(event!=null){
                        msgSeleccioneFecha.setText(String.format(getString(R.string.agenda_dono),event.getEventInfo()));
                    }
                    else if(range!=null){
                        msgSeleccioneFecha.setText(range.getMessage());
                    }
                    else{
                        msgSeleccioneFecha.setText(getString(R.string.agenda_no_dono));
                    }
                }
                else{
                    msgSeleccioneFecha.setText(getString(R.string.agenda_seleccione_fecha));
                }
            }
        );


    }

    @OnClick({R.id.agenda_no_done, R.id.agenda_done_plaquetas, R.id.agenda_done_plasma, R.id.agenda_done_sangre})
    public void marcarTipoDonacion(View view){
        String tipo=view.getId()==R.id.agenda_no_done?null:((Button)view).getText().toString();

        Date fecha=calendar.getSelectedDate();

        if(tipo==null && donacionesMap.containsKey(fecha)){
            donacionesMap.remove(fecha);
        }
        else{
            donacionesMap.put(fecha,tipo);
        }

        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, Agenda.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> donacionesSet=new HashSet<String>();
        for(Map.Entry<Date,String> donacion:donacionesMap.entrySet()){
            donacionesSet.add(donacion.getKey().getTime()+"::"+donacion.getValue());
        }
        editor.putStringSet(Constantes.SP_DONACIONES, donacionesSet);
        editor.commit();
//        calendar.clearHighlightedDates();
//        calendar.highlightDates(donacionesMap.keySet());

        for(Button donacion:tiposDonaciones){
            donacion.setTextColor(Color.BLACK);
        }
        ((Button)view).setTextColor(getResources().getColor(R.color.rojo));
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
