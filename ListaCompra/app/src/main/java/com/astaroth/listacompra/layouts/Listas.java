package com.astaroth.listacompra.layouts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.astaroth.listacompra.R;
import com.astaroth.listacompra.beans.Articulo;
import com.astaroth.listacompra.beans.ArticuloSerializable;
import com.astaroth.listacompra.beans.Lista;
import com.astaroth.listacompra.beans.ListaArticulo;
import com.astaroth.listacompra.beans.ListaArticuloSerializable;
import com.astaroth.listacompra.beans.ListaSerializable;
import com.astaroth.listacompra.beans.Usuario;
import com.astaroth.listacompra.datos.DBAdapter;
import com.astaroth.listacompra.utils.StringUtil;
import com.astaroth.listacompra.utils.SwipeListViewTouchListener;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listas extends ActionBarActivity {
    private Usuario usuario;
    private ListView listaListas;
    List<ListaSerializable> listas;
    private static final int LISTA_DETALLE = 3;
    private static final String TAG_LOG = "LOG_LISTA";

    //Firebase data
    private static final String FIREBASE_URL = "https://sizzling-torch-375.firebaseio.com/";
    private Firebase mFirebaseRef = null;
    private ValueEventListener mConnectedListener;
    private static final String users = "users";
    private static final String LISTA = "lista";
    private static final String ARTICULO = "articulo";
    boolean doingConection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_listas);
        usuario = (Usuario)getIntent().getExtras().get("usuario");
        listaListas = (ListView)findViewById(R.id.listaListas);
        ImageView anadir = (ImageView)findViewById(R.id.listas_anadir);
        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anadirLista();
            }
        });
        ImageView descargar = (ImageView)findViewById(R.id.listas_descargar);
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargarLista();
            }
        });
        registerForContextMenu(listaListas);
        listaListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                abrirLista(listas.get(position));
            }
        });
        //Deslizar item para borrarlo
        SwipeListViewTouchListener touchListener =new SwipeListViewTouchListener(listaListas,new SwipeListViewTouchListener.OnSwipeCallback() {
            @Override
            public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda
                DialogFragment dialogoConfirmar = DialogoBorrado.newInstance(listas.get(reverseSortedPositions[0]).getId());
                dialogoConfirmar.show(getFragmentManager(), "dialog");
            }
            @Override
            public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la derecha
                sendLista(listas.get(reverseSortedPositions[0]));
            }
        },false, false);
        listaListas.setOnTouchListener(touchListener);
        listaListas.setOnScrollListener(touchListener.makeScrollListener());
        abrirActual();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_listas, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_borrar:
                deleteLista(listas.get(info.position).getId());
                setData();
                break;
            case R.id.action_editar:
                DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.editar_lista, listas.get(info.position));
                dialogoSeleccion.show(getFragmentManager(), "listaEdit");
                break;
            case R.id.action_bluetooth:
                sendLista(listas.get(info.position));
                break;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFirebaseRef!=null) mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
    }

    public void sendData (ListaArticulo listaArticulo, String loginToSend){
        if (doingConection) Toast.makeText(this, R.string.error_ya_enviando, Toast.LENGTH_LONG).show();
        else if (tieneInternet()) {
            //String loginToSend = "astaroth";
            doingConection = true;
            guardarLoginEnvio(loginToSend);
            mFirebaseRef = new Firebase(FIREBASE_URL).child(loginToSend);
            Firebase.AuthResultHandler auth = new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) { }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) { }
            };
            mFirebaseRef.authAnonymously(auth);
            mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        Log.i(TAG_LOG, "Connected to Firebase");
                    } else {
                        Log.i(TAG_LOG, "Disconnected from Firebase");
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            Map<String, Lista> lista = new HashMap<String, Lista>();
            lista.put("lista", listaArticulo.getLista());
            mFirebaseRef.setValue(lista);
            Map<String, Articulo> articulos = new HashMap<String, Articulo>();
            for (Articulo articulo : listaArticulo.getArticulos()) {
                articulos.put(String.valueOf(articulo.getId()), articulo);
            }
            mFirebaseRef.child(ARTICULO).setValue(articulos);
            Toast.makeText(this, R.string.lista_enviada, Toast.LENGTH_LONG).show();
            doingConection = false;
        } else {
            Toast.makeText(this, R.string.error_conexion, Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode,int resultCode, Intent data){
        switch (requestCode) {
            case LISTA_DETALLE:
                if(resultCode==RESULT_FIRST_USER){
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
        }
    }

    private void sendLista(ListaSerializable toSend){
        List<ArticuloSerializable> toSendArt = getArticulos(toSend.getId());
        if (toSendArt==null || toSendArt.size()==0) Toast.makeText(this,R.string.error_no_articulo_en_lista, Toast.LENGTH_LONG).show();
        else {
            DialogFragment dialogoSend = DialogoLoginToSend.newInstance(new ListaArticuloSerializable(toSend, toSendArt));
            dialogoSend.show(getFragmentManager(), "listaSend");
        }
    }

    private void anadirLista(){
        DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.nueva_lista);
        dialogoSeleccion.show(getFragmentManager(), "dialog");
    }
    public void descargarLista(){
        if (doingConection) Toast.makeText(this, R.string.error_ya_enviando, Toast.LENGTH_LONG).show();
        else if (tieneInternet()) {
            doingConection = true;
            String loginToSend = usuario.getLogin();
            Firebase uno = new Firebase(FIREBASE_URL);
            mFirebaseRef = uno.child(loginToSend);
            Firebase.AuthResultHandler auth = new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) { }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) { }
            };
            mFirebaseRef.authAnonymously(auth);
            mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        Log.i(TAG_LOG, "Connected to Firebase");
                    } else {
                        Log.i(TAG_LOG, "Disconnected from Firebase");
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            mFirebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        boolean bContinue = true;
                        try {
                            String sReturn = (String) snapshot.getValue();
                            bContinue = !StringUtil.isNullOrEmpty(sReturn);
                        } catch (ClassCastException e) {
                            Log.i(TAG_LOG, "Error al cargar. La lista no está vacía."+e);
                        }
                        if (bContinue) {
                            HashMap<String, Object> valor = (HashMap<String, Object>) snapshot.getValue();

                            HashMap<String, Object> listaHM = (HashMap<String, Object>) valor.get(LISTA);
                            ListaSerializable lista = new ListaSerializable(((Long) listaHM.get("id")).intValue()
                                    , (String) listaHM.get("descripcion")
                                    , ((Long) listaHM.get("fkIdUsuario")).intValue()
                                    , ((Long) listaHM.get("listaActual")).intValue());

                            ArrayList articulosList = null;
                            HashMap<String, Object> articulosHM = null;
                            List<ArticuloSerializable> articulos = null;
                            try {
                                articulosList = (ArrayList) valor.get(ARTICULO);
                                HashMap<String, Object> articuloHM;
                                articulos = new ArrayList<ArticuloSerializable>();
                                for (Object articuloO : articulosList) {
                                    articuloHM = (HashMap<String, Object>) articuloO;
                                    if (articuloHM != null) {
                                        articulos.add(new ArticuloSerializable(((Long) articuloHM.get("id")).intValue()
                                            , (String) articuloHM.get("descripcion")
                                            , (String) articuloHM.get("cantidad")
                                            , ((Long) articuloHM.get("fkIdLista")).intValue()
                                            , (articuloHM.get("importe")==null?0:((Double) articuloHM.get("importe")).doubleValue())
                                            , (articuloHM.get("marcado")==null?0:((Long) articuloHM.get("marcado")).intValue())
                                        ));
                                    }
                                }
                            } catch (ClassCastException e) {
                                Log.i(TAG_LOG, "Error al hacer cast de los articulos. No es ArrayList."+e);
                                //articulosList = null;
                            }
                            if (articulosList==null) {
                                try {
                                    articulosHM = (HashMap<String, Object>) valor.get(ARTICULO);
                                    HashMap<String, Object> articuloHM;
                                    articulos = new ArrayList<ArticuloSerializable>();
                                    for (String key:articulosHM.keySet()){
                                        articuloHM = (HashMap<String, Object>) articulosHM.get(key);
                                        if (articuloHM != null) {
                                            articulos.add(new ArticuloSerializable(((Long) articuloHM.get("id")).intValue()
                                                , (String) articuloHM.get("descripcion")
                                                , (String) articuloHM.get("cantidad")
                                                , ((Long) articuloHM.get("fkIdLista")).intValue()
                                                , (articuloHM.get("importe")==null?0:((Double) articuloHM.get("importe")).doubleValue())
                                                , (articuloHM.get("marcado")==null?0:((Long) articuloHM.get("marcado")).intValue())
                                            ));
                                        }
                                    }
                                } catch (ClassCastException e) {
                                    Log.i(TAG_LOG, "Error al hacer cast de los articulos. No es HashMap."+e);
                                }
                            }
                            if (articulos==null || articulos.size()==0) {
                                Toast.makeText(Listas.this, R.string.error_lista, Toast.LENGTH_LONG).show();
                            } else {
                                mFirebaseRef.setValue("");
                                guardarRecibido(lista, articulos);
                            }
                        } else {
                            Toast.makeText(Listas.this, R.string.error_lista, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Listas.this, R.string.error_lista, Toast.LENGTH_LONG).show();
                    }
                    mFirebaseRef.removeEventListener(this);
                    doingConection = false;
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.w(TAG_LOG, "The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(this, R.string.error_conexion, Toast.LENGTH_LONG).show();
        }
    }
    public List<ListaSerializable> existeListaPorNombre(String descripcion){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        List<ListaSerializable> encontrados = adp.getListasByNameUsuario(usuario.getId(), descripcion);
        adp.close();
        return encontrados;
    }
    public void deleteArticulosByLista(int idLista){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.deleteArticulosByLista(idLista);
        adp.close();
    }
    public void guardarLoginEnvio(String loginToSend){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.guardarLoginEnvio(loginToSend, usuario.getId());
        adp.close();
    }

    public void guardarRecibido(ListaSerializable lista, List<ArticuloSerializable> articulos){
        ListaSerializable listaToSave = lista;
        listaToSave.setId(0);
        listaToSave.setFkIdUsuario(usuario.getId());
        listaToSave.setListaActual(0);
        List<ListaSerializable> encontrados = existeListaPorNombre(listaToSave.getDescripcion());
        if (encontrados.size()>0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("La lista ya existe. Si continua se sobrescribirán los datos")
                    .setTitle("Sobreescribir")
                    .setPositiveButton("Aceptar", new PositiveClickListener(encontrados, articulos))
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            int idLista = guardarLista(listaToSave);
            guardarArticulos(idLista, articulos);
            setData();
        }
    }

    public class PositiveClickListener implements DialogInterface.OnClickListener {
        private List<ListaSerializable> encontrados;
        private List<ArticuloSerializable> articulo;
        public PositiveClickListener(List<ListaSerializable> encontradosM, List<ArticuloSerializable> articuloM){
            super();
            encontrados = encontradosM;
            articulo = articuloM;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            for (int i=0; i<encontrados.size(); i++) {
                if (i==(encontrados.size()-1)) {
                    int idLista = encontrados.get(i).getId();
                    deleteArticulosByLista(idLista);
                    guardarArticulos(idLista, articulo);
                } else {
                    deleteLista(encontrados.get(i).getId());
                }
            }
            setData();
        }
    }

    public int guardarLista(ListaSerializable lista){
        if (lista.getFkIdUsuario()==0) {
            lista.setFkIdUsuario(usuario.getId());
        }
        int id;
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        id = (int)adp.insertLista(lista);
        adp.close();
        return id;
    }
    public void guardarArticulos(int idLista, List<ArticuloSerializable> articulos){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        for (ArticuloSerializable a:articulos) {
            a.setFkIdLista(idLista);
            a.setId(0);
            adp.insertArticulo(a);
        }
        adp.close();
    }
    public void deleteLista (int idLista){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        adp.deleteLista(idLista);
        adp.close();
    }
    public List<ArticuloSerializable> getArticulos(int idLista){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        List<ArticuloSerializable> articulos = adp.getArticulosByLista(idLista);
        adp.close();
        return articulos;
    }
    private void setData(){
        DBAdapter adp=new DBAdapter(this);
        adp.open();

        Cursor c = adp.getCursorListasByUsuario(usuario.getId());
        listas = adp.getListasByCursor(c);
        ListaSerializable lista = adp.getActual(usuario.getId());
        adp.close();

        String[] columnas = {DBAdapter.DB_DESCRIPCION};
        int[] vistas = {R.id.listadescripcion};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.control_lista, c, columnas, vistas, 0);
        listaListas.setAdapter(adapter);
    }
    private void abrirActual(){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        ListaSerializable lista = adp.getActual(usuario.getId());
        adp.close();
        if (lista!=null) abrirLista (lista);
    }
    private void abrirLista(ListaSerializable lista){
        Intent intent = new Intent(this, ListaActual.class);
        intent.putExtra("usuario", usuario);
        intent.putExtra("lista", lista);
        startActivityForResult(intent, LISTA_DETALLE);
    }
    public static class DialogoBorrado extends DialogFragment {
        int idLista;
        static DialogoBorrado newInstance(int idLista) {
            DialogoBorrado f = new DialogoBorrado();
            Bundle args = new Bundle();
            args.putSerializable("idLista", idLista);
            f.setArguments(args);
            return f;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            idLista = getArguments().getInt("idLista");

            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.conf_borrar_lista)
                    .setPositiveButton(R.string.si,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteListaDialog();
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
        public void deleteListaDialog(){
            ((Listas)getActivity()).deleteLista(idLista);
            ((Listas)getActivity()).setData();
        }
    }
    public static class DialogoUsuario extends DialogFragment {
        EditText nombre;
        CheckBox actual;
        ListaSerializable lista;

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
        static DialogoUsuario newInstance(int title, ListaSerializable lista) {
            DialogoUsuario f = new DialogoUsuario();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putSerializable("lista", lista);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.nuevo_lista_dialog, container, false);
            ImageView entrar = (ImageView) v.findViewById(R.id.dialog_entrar);
            getDialog().setTitle(getArguments().getInt("title"));
            lista = (ListaSerializable)getArguments().getSerializable("lista");


            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardar();
                }
            });
            nombre = (EditText) v.findViewById(R.id.dialog_nombre);
            actual = (CheckBox) v.findViewById(R.id.lista_actual);
            if (lista!=null) {
                nombre.setText(lista.getDescripcion());
                if (lista.getListaActual()==1) actual.setChecked(true);
            }
            return v;
        }

        private void guardar() {
            if (StringUtil.isNullOrEmpty(nombre.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_lista_obligatoria, Toast.LENGTH_LONG).show();
            } else {
                if (lista==null) lista = new ListaSerializable();
                lista.setDescripcion(nombre.getText().toString());
                if (actual.isChecked()) lista.setListaActual(1);
                else lista.setListaActual(0);
                ((Listas)getActivity()).guardarLista(lista);
                ((Listas)getActivity()).setData();
                this.getDialog().cancel();
            }
        }
    }
    public static class DialogoLoginToSend extends DialogFragment {
        AutoCompleteTextView login;
        private ListaArticuloSerializable listaArticulo;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        static DialogoLoginToSend newInstance(ListaArticuloSerializable lista) {
            DialogoLoginToSend f = new DialogoLoginToSend();
            Bundle args = new Bundle();
            args.putSerializable("lista", lista);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.login_send_dialog, container, false);
            ImageView entrar = (ImageView) v.findViewById(R.id.entrar);
            getDialog().setTitle(R.string.usuario);
            listaArticulo = (ListaArticuloSerializable)getArguments().getSerializable("lista");

            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardar();
                }
            });
            login = (AutoCompleteTextView) v.findViewById(R.id.dialog_user);
            String[] logins = getLogins();
            login.setAdapter(new ArrayAdapter<String>(((Listas)getActivity()), android.R.layout.simple_dropdown_item_1line, logins));
            login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        ((AutoCompleteTextView)v).showDropDown();
                    }
                }
            });
            return v;
        }

        private void guardar() {
            if (StringUtil.isNullOrEmpty(login.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_user_obligatorio, Toast.LENGTH_LONG).show();
            } else {
                ((Listas)getActivity()).sendData(new ListaArticulo(listaArticulo.getLista().getLista(), getArticulosByArticulosSerializable(listaArticulo.getArticulos())), login.getText().toString() );
                this.getDialog().cancel();
            }
        }
        private String[] getLogins(){
            DBAdapter adp=new DBAdapter((Listas)getActivity());
            adp.open();
            String[] logins = adp.getLoginToSend(((Listas)getActivity()).usuario.getId());
            adp.close();
            return logins;
        }
    }
    public static List<Articulo> getArticulosByArticulosSerializable(List<ArticuloSerializable> inicial){
        List<Articulo> toReturn = new ArrayList<Articulo>();
        for (ArticuloSerializable articulo:inicial) toReturn.add(articulo.getArticulo());
        return toReturn;
    }
    public boolean tieneInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null && info.isConnected()) {
                return true;
            } else {
                info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (info != null && info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}
