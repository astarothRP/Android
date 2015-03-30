package com.rperez.ongpersonas.personasatendidas.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rperez.ongpersonas.personasatendidas.R;
import com.rperez.ongpersonas.personasatendidas.beans.Atendido;
import com.rperez.ongpersonas.personasatendidas.datos.DBAdapter;
import com.rperez.ongpersonas.personasatendidas.utils.CSVUtils;
import com.rperez.ongpersonas.personasatendidas.utils.CalendarUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

public class ConsultaActivity extends ActionBarActivity {
    private String mailLogin;
    private Calendar fechaConsulta;


    private EditText nacionalHombreNoAten;
    private EditText nacionalMujerNoAten;
    private EditText extranjeroHombreNoAten;
    private EditText extranjeroMujerNoAten;
    private EditText hombreNoAten;
    private EditText mujerNoAten;
    private EditText totalNoAten;
    private EditText nacionalHombreAten;
    private EditText nacionalMujerAten;
    private EditText extranjeroHombreAten;
    private EditText extranjeroMujerAten;
    private EditText hombreAten;
    private EditText mujerAten;
    private EditText totalAten;
    private TextView lblFecha;
    private CheckBox atendida;
    private CheckBox noAtendida;
    private ImageView excelImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        mailLogin = (String)getIntent().getExtras().get("mail");
        fechaConsulta = Calendar.getInstance();

        excelImg = (ImageView)findViewById(R.id.excel);
        lblFecha = (TextView)findViewById(R.id.lblFecha);
        atendida = (CheckBox)findViewById(R.id.atendida);
        noAtendida = (CheckBox)findViewById(R.id.noatendida);

        nacionalHombreNoAten = (EditText)findViewById(R.id.noaten_nacional_hombre);
        nacionalMujerNoAten = (EditText)findViewById(R.id.noaten_nacional_mujer);
        extranjeroHombreNoAten = (EditText)findViewById(R.id.noaten_extranjero_hombre);
        extranjeroMujerNoAten = (EditText)findViewById(R.id.noaten_extranjero_mujer);
        hombreNoAten = (EditText)findViewById(R.id.noaten_hombre);
        mujerNoAten = (EditText)findViewById(R.id.noaten_mujer);
        totalNoAten = (EditText)findViewById(R.id.noaten_total);
        nacionalHombreAten = (EditText)findViewById(R.id.aten_nacional_hombre);
        nacionalMujerAten = (EditText)findViewById(R.id.aten_nacional_mujer);
        extranjeroHombreAten = (EditText)findViewById(R.id.aten_extranjero_hombre);
        extranjeroMujerAten = (EditText)findViewById(R.id.aten_extranjero_mujer);
        hombreAten = (EditText)findViewById(R.id.aten_hombre);
        mujerAten = (EditText)findViewById(R.id.aten_mujer);
        totalAten = (EditText)findViewById(R.id.aten_total);

        setEditexts();

        ImageView previo = (ImageView)findViewById(R.id.prev);
        previo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaConsulta(true);
            }
        });
        ImageView siguiente = (ImageView)findViewById(R.id.sig);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaConsulta(false);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent in=new Intent();
        setResult(RESULT_CANCELED,in);
        finish();
    }

    private void nuevaConsulta(boolean previo){
        if (previo) fechaConsulta.add(Calendar.DATE, -1);
        else fechaConsulta.add(Calendar.DATE, 1);
        setEditexts();
    }

    private void setEditexts(){
        lblFecha.setText(CSVUtils.format.format(fechaConsulta.getTime()));
        long milsCons = CalendarUtil.cleanCal(fechaConsulta);
        boolean muestraExcelAtendido = true, muestraExcelNoAtendido = true;
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        Atendido atendido = adp.getAtendidoPorFecha(milsCons, PrincipalActivity.TIPO_ATENDIDO, mailLogin);
        adp.close();
        if (atendido == null) {
            atendido = new Atendido(0, 0, 0, 0, 0, 0, 0, 0, PrincipalActivity.TIPO_ATENDIDO, mailLogin, 0);
            muestraExcelAtendido = false;
        }
        nacionalHombreAten.setText(String.valueOf(atendido.getNacionalHombre()));
        nacionalMujerAten.setText(String.valueOf(atendido.getNacionalMujer()));
        extranjeroHombreAten.setText(String.valueOf(atendido.getExtranjeroHombre()));
        extranjeroMujerAten.setText(String.valueOf(atendido.getExtranjeroMujer()));
        hombreAten.setText(String.valueOf(atendido.getTotalHombre()));
        mujerAten.setText(String.valueOf(atendido.getTotalMujer()));
        totalAten.setText(String.valueOf(atendido.getTotal()));
        if (atendido.getExportado()==1) atendida.setChecked(true);
        else atendida.setChecked(false);

        adp.open();
        atendido = adp.getAtendidoPorFecha(milsCons, PrincipalActivity.TIPO_NOATENDIDO, mailLogin);
        adp.close();
        if (atendido == null) {
            atendido = new Atendido(0, 0, 0, 0, 0, 0, 0, 0, PrincipalActivity.TIPO_NOATENDIDO, mailLogin, 0);
            muestraExcelNoAtendido = false;
        }
        nacionalHombreNoAten.setText(String.valueOf(atendido.getNacionalHombre()));
        nacionalMujerNoAten.setText(String.valueOf(atendido.getNacionalMujer()));
        extranjeroHombreNoAten.setText(String.valueOf(atendido.getExtranjeroHombre()));
        extranjeroMujerNoAten.setText(String.valueOf(atendido.getExtranjeroMujer()));
        hombreNoAten.setText(String.valueOf(atendido.getTotalHombre()));
        mujerNoAten.setText(String.valueOf(atendido.getTotalMujer()));
        totalNoAten.setText(String.valueOf(atendido.getTotal()));
        if (atendido.getExportado()==1) noAtendida.setChecked(true);
        else noAtendida.setChecked(false);

        if (muestraExcelAtendido || muestraExcelNoAtendido) excelImg.setVisibility(View.VISIBLE);
        else excelImg.setVisibility(View.INVISIBLE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consulta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent in;
        switch(item.getItemId()) {
            case R.id.action_hoy:
                in=new Intent();
                setResult(RESULT_OK,in);
                finish();
                break;
            case R.id.action_desconectar:
                in=new Intent();
                setResult(RESULT_CANCELED,in);
                finish();
                break;
        }
        return true;
    }

    public void exportar(View v){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        long milsCons = CalendarUtil.cleanCal(fechaConsulta);
        List<Atendido> atendidos = adp.findAtendidoPorFechas(milsCons, milsCons, mailLogin);
        adp.close();
        File csvFile = CSVUtils.getCSVByList(atendidos, this);
        if (csvFile != null) {
            Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
            itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mailLogin});
            itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, "Resumen personas atendidas [" + CSVUtils.format.format(fechaConsulta.getTime()) + "]");
            itSend.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(csvFile));
            itSend.setType("text/csv");
            startActivity(itSend);
            adp=new DBAdapter(this);
            adp.open();
            adp.setExportado(atendidos);
            adp.close();
        } else {
            Toast.makeText(this, "No se pudo adjuntar el archivo. El mail no se envió", Toast.LENGTH_LONG).show();
        }
    }
}
