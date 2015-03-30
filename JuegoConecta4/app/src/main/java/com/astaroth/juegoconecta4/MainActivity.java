package com.astaroth.juegoconecta4;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
    private int jugadorActual; // 0(contraMaquina) / 1(1) / 2(2)
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jugadorActual = getIntent().getExtras().getInt("usuario");
        setContentView(R.layout.activity_main);
        game = new Game();
        ((TextView)findViewById(R.id.lblGanador)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restart();
                ((TextView)v).setText("");
            }
        });
        for(int f=0; f<Game.SIZE; f++){
            for(int c=0; c<Game.SIZE; c++){
                ((ImageButton)findViewById(ids[f][c])).setOnClickListener(this);
            }
        }
        dibujarTablero();
    }

    public void restart(){
        game = new Game();
        //((TextView) findViewById(R.id.lblGanador)).setText("");
        dibujarTablero();
    }

    private void dibujarTablero(){
        int drawable;
        for(int f=0; f<Game.SIZE; f++){
            for(int c=0; c<Game.SIZE; c++){
                ImageButton image = ((ImageButton)findViewById(ids[f][c]));
                if (game.isLibre(f, c)) drawable = R.drawable.vacio;
                else if (game.isJugador1(f, c)) drawable = R.drawable.jug1;
                else drawable = R.drawable.jug2;
                image.setImageDrawable(getResources().getDrawable(drawable));
            }
        }
    }

    private String getTextoGanador(int jugador){
        if (jugador==Game.MAQUINA) {
            return getString(R.string.ganaMaquina);
        } else if (jugador==Game.JUG1) {
            return getString(R.string.ganaRojo);
        } else if (jugador==Game.JUG2) {
            return getString(R.string.ganaVerde);
        }
        return "";
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
