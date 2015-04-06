package com.astaroth.listacompra.layouts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astaroth.listacompra.R;
import com.astaroth.listacompra.Utils.StringUtil;
import com.astaroth.listacompra.Utils.SwipeListViewTouchListener;
import com.astaroth.listacompra.beans.Articulo;
import com.astaroth.listacompra.beans.ArticuloSerializable;
import com.astaroth.listacompra.beans.ListaSerializable;
import com.astaroth.listacompra.beans.Usuario;
import com.astaroth.listacompra.datos.DBAdapter;

import java.util.List;

public class ListaActual extends ActionBarActivity {
    private Usuario usuario;
    private ListView listaArticulos;
    List<ArticuloSerializable> articulos;
    ListaSerializable actual;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actual);
        usuario = (Usuario)getIntent().getExtras().get("usuario");
        actual = (ListaSerializable)getIntent().getExtras().get("lista");
        total = (TextView)findViewById(R.id.total);
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
        //Deslizar item para borrarlo
        SwipeListViewTouchListener touchListener =new SwipeListViewTouchListener(listaArticulos,new SwipeListViewTouchListener.OnSwipeCallback() {
            @Override
            public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda
                DialogFragment dialogoConfirmar = DialogoBorrado.newInstance(articulos.get(reverseSortedPositions[0]));
                dialogoConfirmar.show(getFragmentManager(), "dialog");
            }
            @Override
            public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la derecha
                marcaArticulo(articulos.get(reverseSortedPositions[0]));
                setData();
            }
        },false, false);
        listaArticulos.setOnTouchListener(touchListener);
        listaArticulos.setOnScrollListener(touchListener.makeScrollListener());
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
                String texto = getResources().getText(R.string.editar_articulo).toString();
                DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.editar_articulo, articulos.get(info.position));
                dialogoSeleccion.show(getFragmentManager(), "dialog");
                break;
            case R.id.action_marcar:
                marcaArticulo(articulos.get(info.position));
                setData();
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
        if (actual!=null) {
            setTitle(actual.getDescripcion());
            articulos = adp.getArticulosByLista(actual.getId());
        }
        adp.close();
        if (articulos!=null && articulos.size()>0) {
            double suma = 0;
            for (ArticuloSerializable toSum : articulos) suma += toSum.getImporte();
            if (suma!=0) total.setText(getString(R.string.total) + String.valueOf(suma));
            else total.setText("");
            ListaArticuloAdapter adapter = new ListaArticuloAdapter(this, R.layout.control_articulo, articulos);
            listaArticulos.setAdapter(adapter);
        }
    }
    public void deleteArticulo (int idArticulo){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.deleteArticulo(idArticulo);
        adp.close();
    }
    public void marcaArticulo (ArticuloSerializable articulo){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.marcaArticulo(articulo.getId(), (articulo.getMarcado() == 0 ? 1 : 0));
        adp.close();
    }
    public void guardarArticulo (ArticuloSerializable articulo){
        if (articulo.getFkIdLista()==0) articulo.setFkIdLista(actual.getId());
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.insertArticulo(articulo);
        adp.close();
    }
    public static class DialogoBorrado extends DialogFragment {
        ArticuloSerializable articuloD;
        static DialogoBorrado newInstance(ArticuloSerializable articulo) {
            DialogoBorrado f = new DialogoBorrado();
            Bundle args = new Bundle();
            args.putSerializable("articulo", articulo);
            f.setArguments(args);
            return f;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            articuloD = (ArticuloSerializable)getArguments().getSerializable("articulo");

            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.conf_borrar_articulo)
                    .setPositiveButton(R.string.si,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteArticuloDialog();
                                }
                            }
                    )
                    .setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) { }
                            }
                    )
                    .create();
        }
        public void deleteArticuloDialog(){
            ((ListaActual)getActivity()).deleteArticulo(articuloD.getId());
            ((ListaActual)getActivity()).setData();
        }
    }
    public static class DialogoUsuario extends DialogFragment {
        EditText nombre;
        EditText cantidad;
        EditText importe;
        CheckBox marcado;
        ArticuloSerializable articulo;

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
        static DialogoUsuario newInstance(int title, ArticuloSerializable articulo) {
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
            articulo = (ArticuloSerializable)getArguments().getSerializable("articulo");

            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardar();
                }
            });
            nombre = (EditText) v.findViewById(R.id.dialog_nombre);
            cantidad = (EditText) v.findViewById(R.id.dialog_cantidad);
            importe = (EditText) v.findViewById(R.id.dialog_importe);
            marcado = (CheckBox) v.findViewById(R.id.dialog_marcado);
            if (articulo!=null) {
                nombre.setText(articulo.getDescripcion());
                cantidad.setText(String.valueOf(articulo.getCantidad()));
                if (articulo.getImporte()!=0) importe.setText(String.valueOf(articulo.getImporte()));
                if (articulo.getMarcado()==1) marcado.setChecked(true);
            }
            return v;
        }

        private void guardar() {
            if (StringUtil.isNullOrEmpty(nombre.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_articulo_obligatorio, Toast.LENGTH_LONG).show();
            } else if ("0".equals(cantidad.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_cantidad_0, Toast.LENGTH_LONG).show();
//            } else if (!StringUtil.isEntero(cantidad.getText().toString(), true)) {
//                Toast.makeText(getActivity(), R.string.error_cantidad, Toast.LENGTH_LONG).show();
            } else {
                if (articulo==null) articulo = new ArticuloSerializable();
                //if (StringUtil.isNullOrEmpty(cantidad.getText().toString())) articulo.setCantidad(0);
                articulo.setCantidad(cantidad.getText().toString());
                articulo.setDescripcion(nombre.getText().toString());
                if (!StringUtil.isNullOrEmpty(importe.getText().toString())) articulo.setImporte(Double.parseDouble(importe.getText().toString()));
                else articulo.setImporte(0);
                articulo.setMarcado(marcado.isChecked() ? 1 : 0);
                ((ListaActual)getActivity()).guardarArticulo(articulo);
                ((ListaActual)getActivity()).setData();
                this.getDialog().cancel();
            }
        }
    }
}
