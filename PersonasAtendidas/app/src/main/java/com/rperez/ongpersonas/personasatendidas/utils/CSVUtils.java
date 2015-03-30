package com.rperez.ongpersonas.personasatendidas.utils;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.rperez.ongpersonas.personasatendidas.beans.Atendido;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by rperez on 10/02/2015.
 */
public class CSVUtils {
    public static String[] columns = new String[] {"Hombre Nacional", "Mujer Nacional", "Hombre Extranjero", "Mujer Extranjero", "Total hombre", "Total mujer", "Total", "Fecha", "Atendido"};

    public static String separador = ";";
    public static String separadorLinea = "\n";

    public static String fileName = "export.csv";
    public static String fileDir = "AER";

    public static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


    public static String getColumnas(){
        return  getDatos(columns);
    }
    public static String getDatos(String[] datos){
        String columnas = "";
        for (int x=0; x<datos.length; x++) columnas+=datos[x]+separador;
        if (columnas.length()>0) columnas = columnas.substring(0, columnas.length()-1);
        return  columnas;
    }

    public static File getCSVByList(List<Atendido> atendidos, Activity activity){
        File csvFile = null;
        if (atendidos==null || atendidos.size()==0) Toast.makeText(activity, "No hay datos que enviar", Toast.LENGTH_LONG).show();
        else if (!isExternalStorageReadable()) Toast.makeText(activity, "No es posible acceder a su tarjeta SD", Toast.LENGTH_LONG).show();
        else if (!isExternalStorageWritable()) Toast.makeText(activity, "No es posible escribir en su tarjeta SD", Toast.LENGTH_LONG).show();
        else {
            String csv = CSVUtils.getColumnas();
            for (Atendido atendido : atendidos) {
                csv += CSVUtils.separadorLinea + CSVUtils.getDatos(atendido.getDataToCSV(format));
            }

            File dir = getCSVDir(fileDir);
            FileOutputStream outputStream;

            try {
                csvFile = new File(dir, fileName);
                outputStream = new FileOutputStream(csvFile);
                outputStream.write(csv.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getCSVDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), albumName);
        if (!file.mkdirs()) {
            Log.e("CSV", "Directory not created");
        }
        return file;
    }
}
