package com.astaroth.juegoconecta4;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rperez on 10/03/2015.
 */
public class Game {
    public static final int SIZE = 7, LIBRE=0, JUG1=1, JUG2=2, MAXCONECTADO=4, MAQUINA=0;
    public static final int JUGANDO = 1, FINALIZADO = 2;
    public static final String ROJO = "1", AMARILLO = "2";
    public static final String DIFICIL = "1", MEDIO = "2", FACIL = "3";
    private int tablero[][];
    private int state;
    private int dificultad;

    public Game(int dificultad) {
        tablero = new int[SIZE][SIZE];
        state = JUGANDO;
        for(int f=0; f<SIZE; f++){
            for(int c=0; c<SIZE; c++){
                tablero[f][c]=0;
            }
        }
        this.dificultad = dificultad;
    }
    public boolean sePuedeColocarFicha(int f, int c){
        if (tablero[f][c] != LIBRE) return false;
        for (int g=0; g<f; g++) {
            if (tablero[g][c] == LIBRE) {
                return false;
            }
        }
        return true;
    }

    public void juegaMaquina(){
        int[] positionesJugables = getLibres();
        boolean jugado = false;
        // COMPRUEBO QUE CON 1 FICHA NO GANO
        if (!jugado) jugado = juegaParaGanar(positionesJugables, MAXCONECTADO);
        // EVITAR QUE GANE EL JUGADOR
        if (!jugado && dificultad==Integer.parseInt(DIFICIL)) {
            // COMPRUEBA QUE NO HACE 4
            for (int c = 0; c < positionesJugables.length; c++) {
                if (positionesJugables[c] != -1) {
                    tablero[positionesJugables[c]][c] = JUG2;
                    if (comprobarCuatro(JUG2, false, MAXCONECTADO, null)) {
                        tablero[positionesJugables[c]][c] = JUG1;
                        jugado = true;
                        break;
                    }
                    tablero[positionesJugables[c]][c] = LIBRE;
                }
            }
        }
        if (!jugado && dificultad==Integer.parseInt(DIFICIL)) {
            // COMPRUEBA QUE NO HACE 3 EN HORIZONTAL
            for (int c = 0; c < positionesJugables.length; c++) {
                if (positionesJugables[c] != -1) {
                    tablero[positionesJugables[c]][c] = JUG2;
                    if (comprobarFilas(JUG2, 3, null)!=null) {
                        tablero[positionesJugables[c]][c] = JUG1;
                        jugado = true;
                        break;
                    }
                    tablero[positionesJugables[c]][c] = LIBRE;
                }
            }
        }
        // REVISO MEJOR JUGADA JUNTANDO DE MÁS A MENOS (DESDE 4 A 2)
        int contMax = MAXCONECTADO-1;
        while (!jugado && contMax>=2 && dificultad!=Integer.parseInt(FACIL)){
            jugado = juegaParaGanar(positionesJugables, contMax);
            contMax--;
        }
        // JUEGO AL AZAR PORQUE NO HA ENCONTRADO NADA MEJOR
        if (!jugado){
            int c, cont=-1;
            do {
                cont++;
                c = new Random().nextInt(SIZE);
            } while (positionesJugables[c]==-1 && cont<SIZE);
            if (cont<SIZE){
                tablero[positionesJugables[c]][c] = JUG1;
            }
        }
    }
    private boolean juegaParaGanar(int[] positionesJugables, int contMax){
        int c, cont=-1;
        ArrayList<Integer> buscados = new ArrayList<Integer>();
        while (buscados.size()<positionesJugables.length) {
            do {
                cont++;
                c = new Random().nextInt(SIZE);
                if (!buscados.contains(new Integer(c))) buscados.add(new Integer(c));
            } while (positionesJugables[c] == -1 && buscados.size()<positionesJugables.length);
            if (positionesJugables[c] != -1) {
                tablero[positionesJugables[c]][c] = JUG1;
                if (comprobarCuatro(JUG1, (contMax == MAXCONECTADO), contMax, new Position(positionesJugables[c], c))) {
                    return true;
                }
                tablero[positionesJugables[c]][c] = LIBRE;
            }
        }
        return false;
    }

    private int[] getLibres(){
        int[] positionesJugables = new int[SIZE];
        for (int c = 0; c < SIZE; c++) {
            positionesJugables[c] = getFilaLibreDeCol(c);
        }
        return positionesJugables;
    }
    private int getFilaLibreDeCol(int col){
        for (int f = 0; f < SIZE; f++) {
            if (tablero[f][col] == LIBRE) {
                return f;
            }
        }
        return -1;
    }

    private Position comprobarFilas(int turno, int maxConectado, Position paraPosicion) {
        int contador = 0;
        boolean isComprobacion = false;
        for(int f=0; f<SIZE; f++){
            contador = 0;

            for(int c=0; c<SIZE; c++){
                if (tablero[f][c]==turno){
                    contador++;
                    if (paraPosicion!=null && paraPosicion.columna==c && paraPosicion.fila==f) isComprobacion = true;
                } else {
                    contador = 0;
                    isComprobacion = false;
                }
                if (contador==maxConectado &&
                    (paraPosicion==null || (paraPosicion!=null && isComprobacion))) {
                    return new Position(f, c);
                }
            }
        }
        return null;
    }

    private Position comprobarColumnas(int turno, int maxConectado, Position paraPosicion) {
        int contador = 0;
        boolean isComprobacion = false;
        for(int c=0; c<SIZE; c++){
            contador = 0;
            for(int f=0; f<SIZE; f++){
                if (tablero[f][c]==turno){
                    contador++;
                    if (paraPosicion!=null && paraPosicion.columna==c && paraPosicion.fila==f) isComprobacion = true;
                } else {
                    contador = 0;
                    isComprobacion = false;
                }
                if (contador==maxConectado &&
                        (paraPosicion==null || (paraPosicion!=null && isComprobacion))) {
                    return new Position(f, c);
                }
            }
        }
        return null;
    }

    private Position comprobarDiagonalSuperior(int turno, int maxConectado, Position paraPosicion){
        int contador = 0;
        int f, c;
        boolean isComprobacion = false;
        for(int cont=0; cont<SIZE; cont++){
            contador = 0;
            f = cont;
            c = SIZE-1;
            do {
                if (tablero[f][c] == turno) {
                    contador++;
                    if (paraPosicion!=null && paraPosicion.columna==c && paraPosicion.fila==f) isComprobacion = true;
                } else {
                    contador = 0;
                    isComprobacion = false;
                }
                if (contador==maxConectado &&
                        (paraPosicion==null || (paraPosicion!=null && isComprobacion))) {
                    return new Position(f, c);
                }
                f--;
                c--;
            } while (f>=0);
        }
        for(int cont=SIZE-2; cont>=0; cont--){
            contador = 0;
            f = SIZE-1;
            c = cont;
            do {
                if (tablero[f][c] == turno) {
                    contador++;
                    if (paraPosicion!=null && paraPosicion.columna==c && paraPosicion.fila==f) isComprobacion = true;
                } else {
                    contador = 0;
                    isComprobacion = false;
                }
                if (contador==maxConectado &&
                        (paraPosicion==null || (paraPosicion!=null && isComprobacion))) {
                    return new Position(f, c);
                }
                f--;
                c--;
            } while (c>=0);
        }
        return null;
    }
    private Position comprobarDiagonalInferior(int turno, int maxConectado, Position paraPosicion){
        int contador = 0;
        int f, c;
        boolean isComprobacion = false;
        for(int cont=0; cont<SIZE; cont++){
            contador = 0;
            f = cont;
            c = 0;
            do {
                if (tablero[f][c] == turno) {
                    contador++;
                    if (paraPosicion!=null && paraPosicion.columna==c && paraPosicion.fila==f) isComprobacion = true;
                } else {
                    contador = 0;
                    isComprobacion = false;
                }
                if (contador==maxConectado &&
                        (paraPosicion==null || (paraPosicion!=null && isComprobacion))) {
                    return new Position(f, c);
                }
                f--;
                c++;
            } while (f>=0);
        }
        for(int cont=1; cont<SIZE; cont++){
            contador = 0;
            f = SIZE-1;
            c = cont;
            do {
                if (tablero[f][c] == turno) {
                    contador++;
                    if (paraPosicion!=null && paraPosicion.columna==c && paraPosicion.fila==f) isComprobacion = true;
                } else {
                    contador = 0;
                    isComprobacion = false;
                }
                if (contador==maxConectado &&
                        (paraPosicion==null || (paraPosicion!=null && isComprobacion))) {
                    return new Position(f, c);
                }
                f--;
                c++;
            } while (c<SIZE);
        }
        return null;
    }

    private boolean comprobarCuatro(int turno, boolean finaliza, int maxConectado, Position paraPosicion){
        return getComprobarCuatro(turno, finaliza, maxConectado, paraPosicion)!=null;
    }

    private Position getComprobarCuatro(int turno, boolean finaliza, int maxConectado, Position paraPosicion){
        Position position = comprobarColumnas(turno, maxConectado, paraPosicion);
        if (position==null) position = comprobarDiagonalInferior(turno, maxConectado, paraPosicion);
        if (position==null) position = comprobarFilas(turno, maxConectado, paraPosicion);
        if (position==null) position = comprobarDiagonalSuperior(turno, maxConectado, paraPosicion);
        if (finaliza && position!=null) state = FINALIZADO;
        return position;
    }

    public boolean comprobarCuatro(int turno){
        return comprobarCuatro(turno, true, MAXCONECTADO, null);
    }



    public boolean isLibre(int f, int c){
        return tablero[f][c]==LIBRE;
    }
    public boolean isJugador1(int f, int c){
        return tablero[f][c]==JUG1;
    }
    public boolean isJugador2(int f, int c){
        return tablero[f][c]==JUG2;
    }
    public int getState(){return state;}
    public void setDificultad(int dificultad){
        this.dificultad = dificultad;
    }

    public void juegaJugador(int jugador, int f, int c){
        tablero[f][c] = jugador;
    }

    public boolean isFill() {
        for(int f=0; f<SIZE; f++){
            for(int c=0; c<SIZE; c++){
                if (tablero[f][c]==LIBRE) return false;
            }
        }
        state = FINALIZADO;
        return true;
    }
    private class Position{
        private int fila;
        private int columna;
        public Position(int fila, int columna){
            this.fila=fila;
            this.columna=columna;
        }

        @Override
        public boolean equals(Object o) {
            Position p = (Position) o;
            return p.fila==this.fila && p.columna==this.columna;
        }
    }
}
