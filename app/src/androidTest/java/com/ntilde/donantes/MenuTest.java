package com.ntilde.donantes;

import android.test.ActivityInstrumentationTestCase2;

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
        solo.clickOnText(getActivity().getString(R.string.home_donde_donar_titulo));
        solo.assertCurrentActivity("Actividad incorrecta",Ubicacion.class);
        solo.goBack();
        solo.clickOnText(getActivity().getString(R.string.home_donde_donar_descripcion));
        solo.assertCurrentActivity("Actividad incorrecta",Ubicacion.class);
        solo.goBack();
    }

    public void testAgenda() throws Exception{
        solo.clickOnText(getActivity().getString(R.string.home_agenda_titulo));
        solo.assertCurrentActivity("Actividad incorrecta",Agenda.class);
        solo.goBack();
        solo.clickOnText(getActivity().getString(R.string.home_agenda_descripcion));
        solo.assertCurrentActivity("Actividad incorrecta",Agenda.class);
        solo.goBack();
    }

    @Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
