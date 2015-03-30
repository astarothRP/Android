package com.astaroth.juegoconecta4;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void contraMaquina(View v){
        arrancaJuego(0);
    }
    public void contraJugador(View v){
        arrancaJuego(1);
    }
    public void arrancaJuego(int usuario) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }
}
