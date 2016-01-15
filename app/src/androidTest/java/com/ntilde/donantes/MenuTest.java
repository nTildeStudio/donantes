package com.ntilde.donantes;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Created by Developer001 on 17/04/2015.
 */
public class MenuTest extends ActivityInstrumentationTestCase2{

    private Solo solo;

    public MenuTest(){
        super(MenuPrincipal.class);
    }

    public void setUp() throws Exception{
        solo=new Solo(getInstrumentation(), getActivity());
    }

    public void testDondeDonar() throws Exception{
        solo.clickOnText(getActivity().getString(R.string.home_donde_donar_title));
        solo.assertCurrentActivity("Actividad incorrecta", Ubicacion.class);
        solo.goBack();
        solo.clickOnText(getActivity().getString(R.string.home_donde_donar_description));
        solo.assertCurrentActivity("Actividad incorrecta", Ubicacion.class);
        solo.goBack();
        View imageDondeDonar = solo.getView("icono_ubicacion");
        solo.clickOnView(imageDondeDonar);
        solo.assertCurrentActivity("Actividad incorrecta", Ubicacion.class);
        solo.goBack();
    }

    public void testAgenda() throws Exception{
        solo.clickOnText(getActivity().getString(R.string.home_agenda_titulo));
        solo.assertCurrentActivity("Actividad incorrecta",Agenda.class);
        solo.goBack();
        solo.clickOnText(getActivity().getString(R.string.home_agenda_descripcion));
        solo.assertCurrentActivity("Actividad incorrecta",Agenda.class);
        solo.goBack();
        View imageAgenda = solo.getView("icono_agenda");
        solo.clickOnView(imageAgenda);
        solo.assertCurrentActivity("Actividad incorrecta", Agenda.class);
        solo.goBack();
    }

    public void testInfo() throws Exception{
        solo.clickOnText(getActivity().getString(R.string.home_info_title));
        solo.assertCurrentActivity("Actividad incorrecta",Informacion.class);
        solo.goBack();
        solo.clickOnText(getActivity().getString(R.string.home_info_description));
        solo.assertCurrentActivity("Actividad incorrecta",Informacion.class);
        solo.goBack();
        View imageInfo = solo.getView("icono_informacion");
        solo.clickOnView(imageInfo);
        solo.assertCurrentActivity("Actividad incorrecta", Informacion.class);
        solo.goBack();
    }

    public void testMensajes() throws Exception{
        solo.clickOnText(getActivity().getString(R.string.home_messages_title));
        solo.assertCurrentActivity("Actividad incorrecta",Mensajes.class);
        solo.goBack();
        //La descripción cambia según el Centro seleccionado
        TextView tvDescripcionMensajes = (TextView) solo.getView("tv_info_mensajes_description");
        solo.clickOnText(tvDescripcionMensajes.getText().toString());
        solo.assertCurrentActivity("Actividad incorrecta",Mensajes.class);
        solo.goBack();
        View imageMensajes = solo.getView("icono_mensajes");
        solo.clickOnView(imageMensajes);
        solo.assertCurrentActivity("Actividad incorrecta", Mensajes.class);
        solo.goBack();
    }

    public void testConfiguracion() throws Exception{
        solo.clickOnText(getActivity().getString(R.string.home_config_title));
        solo.assertCurrentActivity("Actividad incorrecta",Configuracion.class);
        solo.goBack();
        solo.clickOnText(getActivity().getString(R.string.home_config_description));
        solo.assertCurrentActivity("Actividad incorrecta",Configuracion.class);
        solo.goBack();
        View imageConfiguracion = solo.getView("icono_configuracion");
        solo.clickOnView(imageConfiguracion);
        solo.assertCurrentActivity("Actividad incorrecta", Configuracion.class);
        solo.goBack();
    }

    @Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
