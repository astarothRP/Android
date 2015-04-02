package com.astaroth.juegoconecta4;

import android.os.Bundle;

/**
 * Created by Astaroth on 01/04/2015.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment{
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
