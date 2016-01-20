package com.ntilde.rest.response;

import com.ntilde.app.R;
import com.ntilde.utils.ParseConstantes;

/**
 * Created by emanuel on 23/11/15.
 */
public class ParseError {

    public static int crearMensajeError(int type, boolean storing) {

        switch (type){
            case ParseConstantes.QUERY_CENTRO_REGIONAL:
                return storing ? R.string.error_saving_centro_regional : R.string.error_retrieving_centro_regional;

            case ParseConstantes.QUERY_CENTROS_REGIONALES:
                return storing ? R.string.error_saving_centros_regionales : R.string.error_retrieving_centros_regionales;

            case ParseConstantes.QUERY_PUNTO_DONACION:
                return storing ? R.string.error_saving_puntos_donacion : R.string.error_retrieving_puntosdonacion;

            case ParseConstantes.QUERY_ULTIMA_ACTUALIZACION:
                return storing ? R.string.error_saving_last_updated_date : R.string.error_retrieving_last_updated_date;

            default:
                return storing ? R.string.error_saving_data : R.string.error_retrieving_data;
        }

    }
}
