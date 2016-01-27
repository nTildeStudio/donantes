package com.ntilde.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ntilde.app.Constantes;
import com.ntilde.app.R;
import com.ntilde.app.views.percentagelayout.PLinearLayout;
import com.parse.ParseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Mensajes extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @InjectView(R.id.iconos_margen_superior) PLinearLayout ic_margen_sup;
    @InjectView(R.id.mensajes_logotipo)ImageView logotipo;
    @InjectView(R.id.mensajes_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.mensajes_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectView(R.id.mensajes_list) ListView lvMensajes;
    @InjectView(R.id.mensajes_no_hay) TextView tvNoHayMensajes;
    ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_mensajes);

        SharedPreferences prefs = getSharedPreferences(Constantes.SP_KEY, MODE_PRIVATE);
        Set<String> alertas=new HashSet<>(prefs.getStringSet(Constantes.SP_ALERTAS,new HashSet<String>()));

        ButterKnife.inject(this);

        ic_margen_sup.post(() -> {
                int valor = ic_margen_sup.getPHeight();
                logotipo.setPadding(valor, valor / 2, valor, valor / 2);
            });

        borde_rojo_superior.post(() -> borde_rojo_inferior.getLayoutParams().height=borde_rojo_superior.getPHeight());

        mAdapter = new ListViewAdapter(alertas, this);
        lvMensajes.setAdapter(mAdapter);
        lvMensajes.setOnItemClickListener(this);
        lvMensajes.setVisibility(alertas.size() > 0 ? View.VISIBLE : View.GONE);
        tvNoHayMensajes.setVisibility(alertas.size() > 0 ? View.GONE : View.VISIBLE);

        Log.e("XXX","Alertas: "+alertas.size());
        for(String alerta:alertas){
            Log.e("XXX", "Alerta (" + alerta.split("::")[0] + ") " + alerta.split("::")[1]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mensajes, menu);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("messages", "text");
        parameters.put("messageText", mAdapter.getItem(position).split("::")[1]);
        ParseAnalytics.trackEventInBackground("click", parameters);
    }


    public class ListViewAdapter extends BaseAdapter{

        class ViewHolder{
            TextView tvAlert;
            TextView tvDate;
            ImageView ivShare;
        }

        Set<String> alertas;
        Context context;

        public ListViewAdapter(Set<String> alertas, Context context){
            this.alertas = alertas;
            this.context = context;
        }

        @Override
        public int getCount() {
            return alertas.size();
        }

        @Override
        public String getItem(int position) {
            return (String) alertas.toArray()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder vh;

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_row_mensajes, null);
                vh = new ViewHolder();
                vh.tvAlert = (TextView) convertView.findViewById(R.id.listview_row_mensajes_tv_alert);
                vh.tvDate = (TextView) convertView.findViewById(R.id.listview_row_mensajes_tv_date);
                vh.ivShare = (ImageView) convertView.findViewById(R.id.listview_row_mensajes_iv_share);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }

            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final Calendar calendar = new GregorianCalendar();
            Log.i("XXX", "Tiempo del mensaje: " + Long.valueOf(getItem(position).split("::")[0]));
            calendar.setTimeInMillis(System.currentTimeMillis());

            vh.tvDate.setText(sdf.format(calendar.getTime()));
            vh.tvAlert.setText(getItem(position).split("::")[1]);

            vh.ivShare.setOnClickListener(v -> {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("messages", "share");
                    parameters.put("messageText", getItem(position).split("::")[1]);
                    ParseAnalytics.trackEventInBackground("click", parameters);

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Donantes");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getItem(position).split("::")[1] + " (" + sdf.format(calendar.getTime()) + ")");
                    startActivity(Intent.createChooser(sharingIntent, "Compartir"));
                });

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("messages", "onBack");
        ParseAnalytics.trackEventInBackground("click", parameters);
        super.onBackPressed();
    }
}
