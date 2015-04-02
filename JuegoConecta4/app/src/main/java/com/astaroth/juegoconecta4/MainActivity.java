package com.astaroth.juegoconecta4;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private final int ids[][] = {
            {R.id.button00, R.id.button01, R.id.button02, R.id.button03, R.id.button04, R.id.button05, R.id.button06},
            {R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16},
            {R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26},
            {R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34, R.id.button35, R.id.button36},
            {R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44, R.id.button45, R.id.button46},
            {R.id.button50, R.id.button51, R.id.button52, R.id.button53, R.id.button54, R.id.button55, R.id.button56},
            {R.id.button60, R.id.button61, R.id.button62, R.id.button63, R.id.button64, R.id.button65, R.id.button66},
        };
    private Game game;
    // EL JUGADOR PRINCIPAL ES EL JUGADOR 2 (COLOR DE PREFERENCES, POR DEFECTO AMARILLO)
    private int jugadorActual, jugadorInicial; // 0(contraMaquina) / 1(1) / 2(2)
    private TextView lblGanador;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jugadorActual = getIntent().getExtras().getInt("usuario");
        setContentView(R.layout.activity_main);

        lblGanador = (TextView)findViewById(R.id.lblGanador);
        lblGanador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });
        for(int f=0; f<Game.SIZE; f++){
            for(int c=0; c<Game.SIZE; c++){
                ((ImageButton)findViewById(ids[f][c])).setOnClickListener(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ayuda:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dsAyuda);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.action_preferences:
                startActivity(new Intent(this, PreferenceActivity.class));
                break;
            case R.id.action_reiniciar:
                restart();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        restart();
    }

    public void restart(){
        lblGanador.setText("");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int dificultad = Integer.parseInt(sharedPref.getString(PreferenceActivity.DIFICULTAD, Game.DIFICIL));
        boolean empiezaJugador = sharedPref.getBoolean(PreferenceActivity.EMPIEZA_JUGADOR, true);
        game = new Game(dificultad);
        if (jugadorActual==Game.MAQUINA && !empiezaJugador) game.juegaMaquina();
        else if (jugadorActual!=Game.MAQUINA) {
            if (empiezaJugador) jugadorActual = Game.JUG2;
            else jugadorActual = Game.JUG1;
        }
        dibujarTablero();
    }
    // EL COLOR DE PREFERENCES ES PARA EL JUG2. POR DEFECTO ES JUG2 (AMARILLO)
    private void dibujarTablero(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int color = Integer.parseInt(sharedPref.getString(PreferenceActivity.COLOR, Game.AMARILLO));
        int drawable;
        for(int f=0; f<Game.SIZE; f++){
            for(int c=0; c<Game.SIZE; c++){
                ImageButton image = ((ImageButton)findViewById(ids[f][c]));
                if (game.isLibre(f, c)) drawable = R.drawable.vacio;
                else if ((game.isJugador1(f, c) && color==Integer.parseInt(Game.AMARILLO)) ||
                        (game.isJugador2(f, c) && color!=Integer.parseInt(Game.AMARILLO))) drawable = R.drawable.jug1;
                else drawable = R.drawable.jug2;
                image.setImageDrawable(getResources().getDrawable(drawable));
            }
        }
    }

    private String getTextoGanador(int jugador){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPref.getString(PreferenceActivity.USER, "");
        int color = Integer.parseInt(sharedPref.getString(PreferenceActivity.COLOR, Game.AMARILLO));
        if (jugador==Game.MAQUINA) {
            return getString(R.string.ganaMaquina);
        } else if (!"".equals(name) && jugador==Game.JUG2) {
            return getString(R.string.gana) + " " + name;
        } else if ((jugador==Game.JUG2 && color==Integer.parseInt(Game.ROJO))
                    || (jugador==Game.JUG1 && color==Integer.parseInt(Game.AMARILLO))) {
            return getString(R.string.ganaRojo);
        } else {
            return getString(R.string.ganaAmarillo);
        }
    }

    @Override
    public void onClick(View v) {
        if (game.getState()==game.JUGANDO) {
            int id = v.getId();
            int jugador;
            for (int f = 0; f < Game.SIZE; f++) {
                for (int g = 0; g < Game.SIZE; g++) {
                    if (ids[f][g] == id) {
                        if (game.sePuedeColocarFicha(f, g)) {
                            jugador = (jugadorActual==Game.MAQUINA?game.JUG2:jugadorActual);
                            game.juegaJugador(jugador, f, g);
                            if (game.comprobarCuatro(jugador)) {
                                ((TextView) findViewById(R.id.lblGanador)).setText(getTextoGanador(jugador));
                            } else {
                                if (jugadorActual==Game.MAQUINA) {
                                    game.juegaMaquina();
                                    if (game.comprobarCuatro(game.JUG1)) {
                                        ((TextView) findViewById(R.id.lblGanador)).setText(getTextoGanador(Game.MAQUINA));
                                    }
                                } else if (jugadorActual==Game.JUG1) {
                                    jugadorActual=Game.JUG2;
                                } else if (jugadorActual==Game.JUG2) {
                                    jugadorActual=Game.JUG1;
                                }
                            }
                            if (game.isFill()) ((TextView) findViewById(R.id.lblGanador)).setText("Empate");
                            dibujarTablero();
                        } else {
                            Toast.makeText(this, "No se puede ahí", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
        }
    }
}
