package com.astaroth.listacompra.layouts;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.astaroth.listacompra.R;
import com.astaroth.listacompra.utils.StringUtil;
import com.astaroth.listacompra.beans.Usuario;
import com.astaroth.listacompra.datos.DBAdapter;


public class Login extends Activity {
    private static final int LISTAS = 1;
    private EditText user;
    private EditText pass;
    private CheckBox conectado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.user);
        pass = (EditText)findViewById(R.id.pass);
        conectado = (CheckBox)findViewById(R.id.conectado);

        ImageView entrar = (ImageView)findViewById(R.id.entrar);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });
        ImageView anadir = (ImageView)findViewById(R.id.anadir);
        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anadir();
            }
        });

        DBAdapter adp=new DBAdapter(this);
        adp.open();
        Usuario logueado = adp.getConectado();
        adp.close();
        if (logueado!=null) {
            user.setText(logueado.getLogin());
            pass.setText(logueado.getPassword());
            conectado.setChecked(logueado.getMantenerConectado()==1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!conectado.isChecked()){
            user.setText("");
            pass.setText("");
        }
    }

    public void entrar(){
        String usuario = user.getText().toString();
        String contrasena = pass.getText().toString();
        boolean isConectado = conectado.isChecked();
        if (StringUtil.isNullOrEmpty(usuario)) {
            Toast.makeText(this, R.string.error_user_obligatorio, Toast.LENGTH_LONG).show();
        } else if (StringUtil.isNullOrEmpty(contrasena)) {
            Toast.makeText(this, R.string.error_pass_obligatoria, Toast.LENGTH_LONG).show();
        } else {
            Usuario logueado;
            DBAdapter adp=new DBAdapter(this);
            adp.open();
            logueado = adp.getUsuario(usuario, contrasena, isConectado);
            adp.close();
            if (logueado==null) Toast.makeText(this, R.string.error_user_o_pass_mal, Toast.LENGTH_LONG).show();
            else {
                if (StringUtil.isLoginValid(logueado.getLogin())) {
                    Intent intent = new Intent(this, Listas.class);
                    intent.putExtra("usuario", logueado);
                    startActivityForResult(intent, LISTAS);
                } else {
                    Toast.makeText(this, R.string.error_usuario, Toast.LENGTH_LONG).show();
                    DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.update_usuario, logueado);
                    dialogoSeleccion.show(getFragmentManager(), "dialog");
                }
            }
        }
    }

    public void anadir(){
        String usuario = user.getText().toString();
        String contrasena = pass.getText().toString();
        Usuario logueado=null;
        if (!StringUtil.isNullOrEmpty(usuario) && !StringUtil.isNullOrEmpty(contrasena) && "recordar".equals(contrasena)) {
            DBAdapter adp=new DBAdapter(this);
            adp.open();
            logueado = adp.getUsuario(usuario);
            adp.close();
            if (logueado!=null) Toast.makeText(this,logueado.getPassword(),Toast.LENGTH_SHORT).show();
            else Toast.makeText(this,R.string.error_user_o_pass_mal,Toast.LENGTH_SHORT).show();
        } else {
            if (!StringUtil.isNullOrEmpty(usuario) && !StringUtil.isNullOrEmpty(contrasena)) {
                DBAdapter adp=new DBAdapter(this);
                adp.open();
                logueado = adp.getUsuario(usuario, contrasena);
                adp.close();
            }
            DialogFragment dialogoSeleccion;
            if (logueado!=null) dialogoSeleccion = DialogoUsuario.newInstance(R.string.update_usuario, logueado);
            else dialogoSeleccion = DialogoUsuario.newInstance(R.string.nuevo_usuario);
            dialogoSeleccion.show(getFragmentManager(), "dialog");
        }
    }

    public void guardarUsuario(Usuario usuario){
        //Toast.makeText(this, "guardando ["+usuario.getMail()+"]", Toast.LENGTH_LONG).show();
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.insertUsuario(usuario);
        adp.close();
        user.setText(usuario.getLogin());
        pass.setText(usuario.getPassword());
    }

    public static class DialogoUsuario extends DialogFragment {
        EditText mail;
        EditText user;
        EditText pass;
        EditText pass2;
        Usuario usuario;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        static DialogoUsuario newInstance(int title, Usuario user) {
            DialogoUsuario f = new DialogoUsuario();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putSerializable("usuario", user);
            f.setArguments(args);
            return f;
        }

        static DialogoUsuario newInstance(int title) {
            DialogoUsuario f = new DialogoUsuario();
            Bundle args = new Bundle();
            args.putInt("title", title);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.nuevo_user_dialog, container, false);
            ImageView entrar = (ImageView) v.findViewById(R.id.entrar);
            getDialog().setTitle(getArguments().getInt("title"));
            usuario = (Usuario)getArguments().getSerializable("usuario");
            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardar();
                }
            });
            mail = (EditText) v.findViewById(R.id.mail);
            user = (EditText) v.findViewById(R.id.user);
            pass = (EditText) v.findViewById(R.id.pass);
            pass2 = (EditText) v.findViewById(R.id.pass2);
            if (usuario!=null) {
                mail.setText(usuario.getMail());
                user.setText(usuario.getLogin());
                pass.setText(usuario.getPassword());
                pass2.setText(usuario.getPassword());
            }
            return v;
        }

        private void guardar() {
            if (!pass.getText().toString().equals(pass2.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_contrasena_igual, Toast.LENGTH_LONG).show();
            } else if (!StringUtil.isEmailValid(mail.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_mail_no_valido, Toast.LENGTH_LONG).show();
            } else if (StringUtil.isNullOrEmpty(user.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_user_obligatorio, Toast.LENGTH_LONG).show();
            } else if (StringUtil.isNullOrEmpty(pass.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_pass_obligatoria, Toast.LENGTH_LONG).show();
            } else if (!StringUtil.isLoginValid(user.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_usuario, Toast.LENGTH_LONG).show();
            } else {
                if (usuario==null) usuario = new Usuario(user.getText().toString(), pass.getText().toString(), mail.getText().toString(), 0);
                else {
                    usuario.setLogin(user.getText().toString());
                    usuario.setPassword(pass.getText().toString());
                    usuario.setMail(mail.getText().toString());
                }
                ((Login)getActivity()).guardarUsuario(usuario);
                this.getDialog().cancel();
            }
        }
    }
}
