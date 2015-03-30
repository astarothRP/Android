package com.astaroth.listacompra.layouts;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.astaroth.listacompra.R;
import com.astaroth.listacompra.Utils.StringUtil;
import com.astaroth.listacompra.beans.Articulo;
import com.astaroth.listacompra.beans.ListaSerializable;
import com.astaroth.listacompra.beans.Usuario;
import com.astaroth.listacompra.datos.DBAdapter;

import java.util.List;

public class ListaActual extends ActionBarActivity {
    private Usuario usuario;
    private ListView listaArticulos;
    List<Articulo> articulos;
    ListaSerializable actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actual);
        usuario = (Usuario)getIntent().getExtras().get("usuario");
        actual = (ListaSerializable)getIntent().getExtras().get("lista");
        listaArticulos = (ListView)findViewById(R.id.listaArticulos);
        listaArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.editar_articulo, articulos.get(position));
                dialogoSeleccion.show(getFragmentManager(), "dialog");
            }
        });
        ImageView anadir = (ImageView)findViewById(R.id.actual_anadir);
        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anadirArticulo();
            }
        });
        registerForContextMenu(listaArticulos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_actual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        switch(item.getItemId()) {
            case R.id.action_desconectar:
                intent=new Intent();
                setResult(RESULT_FIRST_USER,intent);
                finish();
                break;
            case R.id.action_listas:
                intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_borrar:
                deleteArticulo(articulos.get(info.position).getId());
                setData();
                break;
            case R.id.action_editar:
                DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.editar_articulo, articulos.get(info.position));
                dialogoSeleccion.show(getFragmentManager(), "dialog");
                break;
        }
        return true;
    }
    private void anadirArticulo(){
        if (actual==null) Toast.makeText(this, R.string.error_no_lista_data, Toast.LENGTH_LONG).show();
        else {
            DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.nuevo_articulo);
            dialogoSeleccion.show(getFragmentManager(), "dialog");
        }
    }
    private void setData(){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        //actual = adp.getActual(usuario.getId());
        Cursor c = null;
        if (actual!=null) {
            setTitle(actual.getDescripcion());
            c = adp.getCursorArticulosByLista(actual.getId());
            articulos = adp.getArticulosByCursor(c);
        }
        adp.close();
        if (c!=null) {
            String[] columnas = {DBAdapter.DB_DESCRIPCION, DBAdapter.DB_CANTIDAD};
            int[] vistas = {R.id.descripcion, R.id.cantidad};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.control_articulo, c, columnas, vistas, 0);
            listaArticulos.setAdapter(adapter);
        }
    }
    public void deleteArticulo (int idArticulo){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.deleteArticulo(idArticulo);
        adp.close();
    }
    public void guardarArticulo (Articulo articulo){
        if (articulo.getFkIdLista()==0) articulo.setFkIdLista(actual.getId());
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.insertArticulo(articulo);
        adp.close();
    }
    public static class DialogoUsuario extends DialogFragment {
        EditText nombre;
        EditText cantidad;
        Articulo articulo;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        static DialogoUsuario newInstance(int title) {
            DialogoUsuario f = new DialogoUsuario();
            Bundle args = new Bundle();
            args.putInt("title", title);
            f.setArguments(args);
            return f;
        }
        static DialogoUsuario newInstance(int title, Articulo articulo) {
            DialogoUsuario f = new DialogoUsuario();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putSerializable("articulo", articulo);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.nuevo_articulo_dialog, container, false);
            ImageView entrar = (ImageView) v.findViewById(R.id.dialog_entrar);
            getDialog().setTitle(getArguments().getInt("title"));
            articulo = (Articulo)getArguments().getSerializable("articulo");

            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardar();
                }
            });
            nombre = (EditText) v.findViewById(R.id.dialog_nombre);
            cantidad = (EditText) v.findViewById(R.id.dialog_cantidad);
            if (articulo!=null) {
                nombre.setText(articulo.getDescripcion());
                cantidad.setText(String.valueOf(articulo.getCantidad()));
            }
            return v;
        }

        private void guardar() {
            if (StringUtil.isNullOrEmpty(nombre.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_articulo_obligatorio, Toast.LENGTH_LONG).show();
            } else if ("0".equals(cantidad.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_cantidad_0, Toast.LENGTH_LONG).show();
            } else if (!StringUtil.isEntero(cantidad.getText().toString(), true)) {
                Toast.makeText(getActivity(), R.string.error_cantidad, Toast.LENGTH_LONG).show();
            } else {
                if (articulo==null) articulo = new Articulo();
                //if (StringUtil.isNullOrEmpty(cantidad.getText().toString())) articulo.setCantidad(0);
                articulo.setCantidad(cantidad.getText().toString());
                articulo.setDescripcion(nombre.getText().toString());
                ((ListaActual)getActivity()).guardarArticulo(articulo);
                ((ListaActual)getActivity()).setData();
                this.getDialog().cancel();
            }
        }
    }
}
