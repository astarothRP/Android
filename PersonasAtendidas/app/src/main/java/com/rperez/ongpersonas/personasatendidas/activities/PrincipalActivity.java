package com.rperez.ongpersonas.personasatendidas.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.rperez.ongpersonas.personasatendidas.R;
import com.rperez.ongpersonas.personasatendidas.beans.Atendido;
import com.rperez.ongpersonas.personasatendidas.datos.DBAdapter;
import com.rperez.ongpersonas.personasatendidas.utils.CSVUtils;
import com.rperez.ongpersonas.personasatendidas.utils.CalendarUtil;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrincipalActivity extends ActionBarActivity {
    private RadioButton atendida;

    public static int NACIONAL_HOMBRE = 1;
    public static int NACIONAL_MUJER = 2;
    public static int EXTRANJERO_HOMBRE = 3;
    public static int EXTRANJERO_MUJER = 4;
    public static int TOTAL_HOMBRE = 5;
    public static int TOTAL_MUJER = 6;
    public static int TOTAL = 7;

    public static int TIPO_ATENDIDO = 1;
    public static int TIPO_NOATENDIDO = 2;

    private int atendidoSel;
    private long milsHoy;
    private String mailLogin;
    private boolean exportado;

    private EditText nacionalHombre;
    private ImageView incNacionalHombre;
    private ImageView decNacionalHombre;
    private EditText nacionalMujer;
    private ImageView incNacionalMujer;
    private ImageView decNacionalMujer;

    private EditText extranjeroHombre;
    private ImageView incExtranjeroHombre;
    private ImageView decExtranjeroHombre;
    private EditText extranjeroMujer;
    private ImageView incExtranjeroMujer;
    private ImageView decExtranjeroMujer;

    private EditText hombre;
    private ImageView incHombre;
    private ImageView decHombre;
    private EditText mujer;
    private ImageView incMujer;
    private ImageView decMujer;

    private EditText total;
    private ImageView incTotal;
    private ImageView decTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        milsHoy = CalendarUtil.cleanCal(Calendar.getInstance());
        mailLogin = (String)getIntent().getExtras().get("mail");

        atendida = (RadioButton)findViewById(R.id.atendida);
        atendida.setChecked(true);

        nacionalHombre = (EditText)findViewById(R.id.nacional_hombre);
        incNacionalHombre = (ImageView)findViewById(R.id.inc_nacional_hombre);
        incNacionalHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(NACIONAL_HOMBRE, true);
            }
        });
        decNacionalHombre = (ImageView)findViewById(R.id.dec_nacional_hombre);
        decNacionalHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(NACIONAL_HOMBRE, false);
            }
        });
        nacionalMujer = (EditText)findViewById(R.id.nacional_mujer);
        incNacionalMujer = (ImageView)findViewById(R.id.inc_nacional_mujer);
        incNacionalMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(NACIONAL_MUJER, true);
            }
        });
        decNacionalMujer = (ImageView)findViewById(R.id.dec_nacional_mujer);
        decNacionalMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(NACIONAL_MUJER, false);
            }
        });

        extranjeroHombre = (EditText)findViewById(R.id.extranjero_hombre);
        incExtranjeroHombre = (ImageView)findViewById(R.id.inc_extranjero_hombre);
        incExtranjeroHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(EXTRANJERO_HOMBRE, true);
            }
        });
        decExtranjeroHombre = (ImageView)findViewById(R.id.dec_extranjero_hombre);
        decExtranjeroHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(EXTRANJERO_HOMBRE, false);
            }
        });
        extranjeroMujer = (EditText)findViewById(R.id.extranjero_mujer);
        incExtranjeroMujer = (ImageView)findViewById(R.id.inc_extranjero_mujer);
        incExtranjeroMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(EXTRANJERO_MUJER, true);
            }
        });
        decExtranjeroMujer = (ImageView)findViewById(R.id.dec_extranjero_mujer);
        decExtranjeroMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(EXTRANJERO_MUJER, false);
            }
        });

        hombre = (EditText)findViewById(R.id.hombre);
        incHombre = (ImageView)findViewById(R.id.inc_hombre);
        incHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(TOTAL_HOMBRE, true);
            }
        });
        decHombre = (ImageView)findViewById(R.id.dec_hombre);
        decHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(TOTAL_HOMBRE, false);
            }
        });
        mujer = (EditText)findViewById(R.id.mujer);
        incMujer = (ImageView)findViewById(R.id.inc_mujer);
        incMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(TOTAL_MUJER, true);
            }
        });
        decMujer = (ImageView)findViewById(R.id.dec_mujer);
        decMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(TOTAL_MUJER, false);
            }
        });

        total = (EditText)findViewById(R.id.total);
        incTotal = (ImageView)findViewById(R.id.inc_total);
        incTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(TOTAL, true);
            }
        });
        decTotal = (ImageView)findViewById(R.id.dec_total);
        decTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incValor(TOTAL, false);
            }
        });

        setAtendida(View.INVISIBLE);
        setEditexts(TIPO_ATENDIDO);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent in=new Intent();
        setResult(RESULT_CANCELED,in);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent in;
        switch(item.getItemId()) {
            case R.id.action_consulta:
                in=new Intent();
                setResult(RESULT_OK,in);
                finish();
                break;
            case R.id.action_exportar:
                if (exportado) Toast.makeText(this, "No se puede exportar", Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder alertDialogBuilder  = new AlertDialog.Builder(this);
                    alertDialogBuilder .setMessage("¿Confirma que desea exportar todos los datos disponibles?")
                            .setTitle("Exportación")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.i("Dialogos", "Confirmacion Aceptada.");
                                    exportacionTotal();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.i("Dialogos", "Confirmacion Cancelada.");
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
            case R.id.action_desconectar:
                in=new Intent();
                setResult(RESULT_CANCELED,in);
                finish();
                break;
        }
        return true;
    }
    public void exportacionTotal(){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        List<Atendido> atendidos = adp.findAllAtendidoNoExportado(mailLogin);
        adp.close();
        File csvFile = CSVUtils.getCSVByList(atendidos, this);
        if (csvFile != null) {
            Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
            itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mailLogin});
            itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, "Resumen general personas atendidas");
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
        Toast.makeText(this, "Exportación total", Toast.LENGTH_LONG).show();
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.atendida:
                if (checked) {
                    setAtendida(View.INVISIBLE);
                    atendidoSel = TIPO_ATENDIDO;
                }
                break;
            case R.id.noAtendida:
                if (checked){
                    setAtendida(View.VISIBLE);
                    atendidoSel = TIPO_NOATENDIDO;
                }
                break;
        }
        setEditexts(atendidoSel);
    }

    // BBDD
    private void setEditexts(int tipo){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        Atendido atendido = adp.getAtendidoPorFecha(milsHoy, tipo, mailLogin);
        adp.close();
        if (atendido == null) atendido = new Atendido(0, 0, 0, 0, 0, 0, 0, 0, tipo, mailLogin, 0);
        nacionalHombre.setText(String.valueOf(atendido.getNacionalHombre()));
        nacionalMujer.setText(String.valueOf(atendido.getNacionalMujer()));
        extranjeroHombre.setText(String.valueOf(atendido.getExtranjeroHombre()));
        extranjeroMujer.setText(String.valueOf(atendido.getExtranjeroMujer()));
        hombre.setText(String.valueOf(atendido.getTotalHombre()));
        mujer.setText(String.valueOf(atendido.getTotalMujer()));
        total.setText(String.valueOf(atendido.getTotal()));
        atendidoSel = atendido.getTipo();
        exportado = atendido.getExportado()==1;
    }
    private void saveEditexts(){
        Atendido atendido = new Atendido(Integer.parseInt(nacionalHombre.getText().toString())
                , Integer.parseInt(nacionalMujer.getText().toString())
                , Integer.parseInt(extranjeroHombre.getText().toString())
                , Integer.parseInt(extranjeroMujer.getText().toString())
                , Integer.parseInt(hombre.getText().toString())
                , Integer.parseInt(mujer.getText().toString())
                , Integer.parseInt(total.getText().toString())
                , milsHoy
                , atendidoSel
                , mailLogin
                , exportado ? 1 : 0
        );
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.insertAtendido(atendido);
        adp.close();
    }

    // Utils
    private void incValor(int tipo, boolean incrementa){
        boolean inferior = false;
        if (tipo==NACIONAL_HOMBRE) {
            inferior = setValor(nacionalHombre, incrementa);
        } else if (tipo==NACIONAL_MUJER) {
            inferior = setValor(nacionalMujer, incrementa);
        } else if (tipo==EXTRANJERO_HOMBRE) {
            inferior = setValor(extranjeroHombre, incrementa);
        } else if (tipo==EXTRANJERO_MUJER) {
            inferior = setValor(extranjeroMujer, incrementa);
        }

        if (!inferior && (tipo==NACIONAL_HOMBRE || tipo==EXTRANJERO_HOMBRE || tipo==TOTAL_HOMBRE)) {
            inferior = setValor(hombre, incrementa);
        } else if (!inferior && (tipo==NACIONAL_MUJER || tipo==EXTRANJERO_MUJER || tipo==TOTAL_MUJER)) {
            inferior = setValor(mujer, incrementa);
        }
        if (!inferior) setValor(total, incrementa);
        saveEditexts();
    }
    private void setAtendida(int visible){
        incHombre.setVisibility(visible);
        decHombre.setVisibility(visible);
        incMujer.setVisibility(visible);
        decMujer.setVisibility(visible);
        incTotal.setVisibility(visible);
        decTotal.setVisibility(visible);
    }
    private static boolean setValor (EditText text, boolean incrementa){
        boolean menor0 = false;
        int valorActual = Integer.parseInt(text.getText().toString());
        if (incrementa) valorActual++;
        else valorActual--;
        if (valorActual<0) {
            valorActual = 0;
            menor0 = true;
        }
        text.setText(String.valueOf(valorActual));
        return  menor0;
    }

}
