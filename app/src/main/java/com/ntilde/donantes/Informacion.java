package com.ntilde.donantes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ntilde.listaexpandible.CustomArrayAdapter;
import com.ntilde.listaexpandible.ExpandableListItem;
import com.ntilde.listaexpandible.SlideExpandableListAdapter;
import com.ntilde.percentagelayout.PLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Informacion extends AppCompatActivity {

    private final int CELL_DEFAULT_HEIGHT = 125;

    private CustomArrayAdapter mAdapter;

    @InjectView(R.id.iconos_margen_superior) PLinearLayout ic_margen_sup;
    @InjectView(R.id.configuracion_logotipo)ImageView logotipo;
    @InjectView(R.id.configuracion_borde_rojo_superior) PLinearLayout borde_rojo_superior;
    @InjectView(R.id.configuracion_borde_rojo_inferior) LinearLayout borde_rojo_inferior;
    @InjectView(R.id.main_list_view)ListView mListView;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_informacion);

        ButterKnife.inject(this);

        ic_margen_sup.post(new Runnable() {
            @Override
            public void run() {
                int valor = ic_margen_sup.getPHeight();
                logotipo.setPadding(valor, valor / 2, valor, valor / 2);
            }
        });

        borde_rojo_superior.post(new Runnable() {
            @Override
            public void run() {
                borde_rojo_inferior.getLayoutParams().height = borde_rojo_superior.getPHeight();
            }
        });

        final List<ExpandableListItem> listItems = new ArrayList<ExpandableListItem>();

        String[] titles = getResources().getStringArray(R.array.titulos_informacion);
        String[] imageIds = getResources().getStringArray(R.array.imagenes_informacion);

        for(int position = 0; position < titles.length;position++){
            ExpandableListItem newItem = new ExpandableListItem(titles[position],getResourceId(imageIds[position]),CELL_DEFAULT_HEIGHT);
            listItems.add(newItem);
        }

        mAdapter = new CustomArrayAdapter(Informacion.this,R.layout.list_view_item,listItems);
        mListView.setAdapter(new SlideExpandableListAdapter(mAdapter, R.id.item_linear_layout, R.id.expanding_layout));

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }


    public int getResourceId(String resName){
        return getResources().getIdentifier(resName, "drawable", getPackageName());
    }
}
