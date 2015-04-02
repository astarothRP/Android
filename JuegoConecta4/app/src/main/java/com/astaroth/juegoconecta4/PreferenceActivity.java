package com.astaroth.juegoconecta4;

import android.app.Activity;
import android.os.Bundle;

public class PreferenceActivity extends Activity {
    public static String USER = "playerName";
    public static String COLOR = "color";
    public static String DIFICULTAD = "dificultad";
    public static String EMPIEZA_JUGADOR = "empJugador";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenceFragment())
                .commit();

    }




}
