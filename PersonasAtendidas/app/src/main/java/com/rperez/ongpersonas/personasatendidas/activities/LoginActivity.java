package com.rperez.ongpersonas.personasatendidas.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rperez.ongpersonas.personasatendidas.R;
import com.rperez.ongpersonas.personasatendidas.datos.DBAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rperez on 06/02/2015.
 */
public class LoginActivity extends Activity {
    private static final int CODE_HOY=1;
    private static final int CODE_CONS=2;
    private Spinner coordinadores;
    private EditText mail;
    private Button anadirMail;
    private Button nuevoCoordinador;
    private Button entrar;

    private boolean viendo;
    private String[] aCoordinadoresBase = new String[]{"uno", "dos"};
    private String[] aCoordinadores;
    private int coordinadorSelect;
    private String mailInSesion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinadores = (Spinner)findViewById(R.id.coordinador);
        rellenaCoordinadores();

        mail = (EditText)findViewById(R.id.email);
        anadirMail = (Button)findViewById(R.id.anadirC);
        nuevoCoordinador = (Button)findViewById(R.id.anadir);
        entrar = (Button)findViewById(R.id.entrar);

        setVisibilidad(aCoordinadores!=null && aCoordinadores.length>0);

        anadirMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrarEnApp();
            }
        });
        nuevoCoordinador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibilidad(!viendo);
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrarEnApp();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibilidad(aCoordinadores!=null && aCoordinadores.length>0);
    }

    private void entrarEnApp(){
        mail.setError(null);

        View focusView = null;
        boolean cancel = false;
        String mailText;
        if (viendo) {
            mailText = aCoordinadores[coordinadorSelect];
        } else {
            mailText = mail.getText().toString();
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mailText)) {
            mail.setError(getString(R.string.error_field_required));
            focusView = mail;
            cancel = true;
        } else if (!isEmailValid(mailText)) {
            mail.setError(getString(R.string.error_invalid_email));
            focusView = mail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (!viendo) {
                insertaCoordinador(mailText);
                rellenaCoordinadores();
            }
            mailInSesion = mailText;
            Intent intent=new Intent(this,PrincipalActivity.class);
            intent.putExtra("mail", mailInSesion);
            startActivityForResult(intent, CODE_HOY);
            Log.i("INFO", "Ha entrado "+mailInSesion);
        }
    }
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        //Toast.makeText(this, "CODE["+requestCode+"]; RESULT["+resultCode+"]", Toast.LENGTH_LONG).show();
        if(requestCode==CODE_HOY){
            if(resultCode==RESULT_OK){
                Intent intent=new Intent(this,ConsultaActivity.class);
                intent.putExtra("mail", mailInSesion);
                startActivityForResult(intent, CODE_CONS);
            }
        } else if(requestCode==CODE_CONS){
            if(resultCode==RESULT_OK){
                Intent intent=new Intent(this,PrincipalActivity.class);
                intent.putExtra("mail", mailInSesion);
                startActivityForResult(intent, CODE_HOY);
            }
        }
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void setVisibilidad(boolean mostrarCombo){
        if (mostrarCombo) {
            viendo = true;
            coordinadores.setVisibility(View.VISIBLE);
            nuevoCoordinador.setVisibility(View.VISIBLE);
            entrar.setVisibility(View.VISIBLE);
            mail.setVisibility(View.INVISIBLE);
            anadirMail.setVisibility(View.INVISIBLE);
        } else {
            viendo = false;
            coordinadores.setVisibility(View.INVISIBLE);
            nuevoCoordinador.setVisibility(View.INVISIBLE);
            entrar.setVisibility(View.INVISIBLE);
            mail.setVisibility(View.VISIBLE);
            anadirMail.setVisibility(View.VISIBLE);
        }
    }

    private void insertaCoordinador(String mail){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.insertCoordinador(mail);
        adp.close();
    }
    private void rellenaCoordinadores(){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        aCoordinadores = adp.getAllMailCoordinadores();
        adp.close();
        coordinadores.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aCoordinadores));
    }
}
