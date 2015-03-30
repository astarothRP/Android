package com.astaroth.juegosolitario;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Game game;

    private static int ids[][] = {
        {0, 0, R.id.rad13, R.id.rad14, R.id.rad15, 0, 0},
        {0, 0, R.id.rad23, R.id.rad24, R.id.rad25, 0, 0},
        {R.id.rad31, R.id.rad32, R.id.rad33, R.id.rad34, R.id.rad35, R.id.rad36, R.id.rad37},
        {R.id.rad41, R.id.rad42, R.id.rad43, R.id.rad44, R.id.rad45, R.id.rad46, R.id.rad47},
        {R.id.rad51, R.id.rad52, R.id.rad53, R.id.rad54, R.id.rad55, R.id.rad56, R.id.rad57},
        {0, 0, R.id.rad63, R.id.rad64, R.id.rad65, 0, 0},
        {0, 0, R.id.rad73, R.id.rad74, R.id.rad75, 0, 0},
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new Game();
        loadListeners();
        loadGrid();

    }

    private void loadListeners() {
        for (int f = 0; f < Game.SIZE; f++) {
            for (int g = 0; g < Game.SIZE; g++) {
                if (ids[f][g]!=0) ((RadioButton) findViewById(ids[f][g])).setOnClickListener(this);
            }
        }
    }

    private void loadGrid(){
        RadioButton radio;
        for (int f = 0; f < Game.SIZE; f++) {
            for (int g = 0; g < Game.SIZE; g++) {
                if (ids[f][g]!=0) {
                    radio = ((RadioButton) findViewById(ids[f][g]));
                    int value = game.getGrid(f, g);
                    if (value==1) radio.setChecked(true);
                    else  radio.setChecked(false);
                    if (game.isPicked(f, g)) radio.setBackgroundColor(Color.RED);
                    else radio.setBackgroundColor(Color.GREEN);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = ((RadioButton)v).getId();

        for (int f = 0; f < Game.SIZE; f++) {
            for (int g = 0; g < Game.SIZE; g++) {
                if (ids[f][g]==id) {
                    game.play(f, g);
                    break;
                }
            }
        }
        loadGrid();
        if (game.isGameFinished()){
            Toast.makeText(this, "Juego terminado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
