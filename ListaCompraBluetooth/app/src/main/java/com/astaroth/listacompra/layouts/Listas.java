package com.astaroth.listacompra.layouts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.astaroth.listacompra.R;
import com.astaroth.listacompra.Utils.BluetoothUtils;
import com.astaroth.listacompra.Utils.StringUtil;
import com.astaroth.listacompra.beans.Articulo;
import com.astaroth.listacompra.beans.Lista;
import com.astaroth.listacompra.beans.ListaSerializable;
import com.astaroth.listacompra.beans.ListaArticulo;
import com.astaroth.listacompra.beans.Usuario;
import com.astaroth.listacompra.datos.DBAdapter;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listas extends ActionBarActivity {
    private Usuario usuario;
    private ListView listaListas;
    List<ListaSerializable> listas;

    //Firebase data
    private static final String FIREBASE_URL = "https://sizzling-torch-375.firebaseio.com/";
    private Firebase mFirebaseRef = null;
    private ValueEventListener mConnectedListener;
    private static final String users = "users";

    // Bluetooth data
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int LISTA_DETALLE = 3;
    BluetoothAdapter bluetoothAdapter;
    BluetoothUtils bluetoothUtils = null;
    private String deviceName = null;
    private int positionToSend;

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
        registerForContextMenu(listaListas);
        listaListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                abrirLista(listas.get(position));
            }
        });
        abrirActual();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null &&  bluetoothAdapter.isEnabled()) setupBluetooth();
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
                dialogoSeleccion.show(getFragmentManager(), "dialog");
                break;
            case R.id.action_bluetooth:
                positionToSend = info.position;
                ListaSerializable toSend = listas.get(info.position);
                List<Articulo> toSendArt = getArticulos(toSend.getId());
                if (toSendArt==null || toSendArt.size()==0) Toast.makeText(this,R.string.error_no_articulo_en_lista, Toast.LENGTH_LONG).show();
                else sendBluetooth(new ListaArticulo(toSend, toSendArt));
                break;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        setData();
        if (bluetoothUtils != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (bluetoothUtils.getState() == BluetoothUtils.STATE_NONE) {
                // Start the Bluetooth chat services
                bluetoothUtils.start();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothUtils != null) {
            bluetoothUtils.stop();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
    }

    public void sendBluetooth (ListaArticulo listaArticulo){
        String loginToSend = "astaroth";
        if (mFirebaseRef==null) {
            mFirebaseRef = new Firebase(FIREBASE_URL).child(loginToSend);
            Firebase.AuthResultHandler auth = new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    Toast.makeText(Listas.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Toast.makeText(Listas.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            };
            mFirebaseRef.authAnonymously(auth);
            mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        Toast.makeText(Listas.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Listas.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        Map<String, Lista> mapa = new HashMap<String, Lista>();
        Lista lista = new Lista(listaArticulo.getLista().getId(), listaArticulo.getLista().getDescripcion(), listaArticulo.getLista().getFkIdUsuario(), listaArticulo.getLista().getListaActual());
        mapa.put("lista", lista);
        mFirebaseRef.setValue(mapa);
        /*if (bluetoothAdapter == null) bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_no_bluetooth, Toast.LENGTH_LONG).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            setupBluetooth();
            Intent serverIntent = new Intent(this, DispositivosBluetooth.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }*/
    }

    public void onActivityResult(int requestCode,int resultCode, Intent data){
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DispositivosBluetooth.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                    bluetoothUtils.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                if(resultCode==RESULT_OK){
                    setupBluetooth();
                    Intent serverIntent = new Intent(this, DispositivosBluetooth.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else Toast.makeText(this, R.string.error_bluetooth_no_activo, Toast.LENGTH_LONG).show();
                break;
            case LISTA_DETALLE:
                if(resultCode==RESULT_FIRST_USER){
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
        }
    }


    public void anadirLista(){
        DialogFragment dialogoSeleccion = DialogoUsuario.newInstance(R.string.nueva_lista);
        dialogoSeleccion.show(getFragmentManager(), "dialog");
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
    public void guardarRecibido(ListaArticulo listaArticulo){
        ListaSerializable listaToSave = listaArticulo.getLista();
        listaToSave.setId(0);
        listaToSave.setFkIdUsuario(usuario.getId());
        List<ListaSerializable> encontrados = existeListaPorNombre(listaToSave.getDescripcion());
        if (encontrados.size()>0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("La lista ya existe. Si continua se sobrescribirán los datos")
                    .setTitle("Sobreescribir")
                    .setPositiveButton("Aceptar", new PositiveClickListener(encontrados, listaArticulo.getArticulos()))
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            int idLista = guardarLista(listaToSave);
            guardarArticulos(idLista, listaArticulo.getArticulos());
        }
        reconectarBluetooth();
    }
    public void reconectarBluetooth(){
        bluetoothUtils.start();
    }
    public void desconectarBluetooth(){
        bluetoothUtils.stop();
    }

    public class PositiveClickListener implements DialogInterface.OnClickListener {
        private List<ListaSerializable> encontrados;
        private List<Articulo> articulo;
        public PositiveClickListener(List<ListaSerializable> encontradosM, List<Articulo> articuloM){
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
    public void guardarArticulos(int idLista, List<Articulo> articulos){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        for (Articulo a:articulos) {
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
    public List<Articulo> getArticulos(int idLista){
        DBAdapter adp=new DBAdapter(this);
        adp.open();
        List<Articulo> articulos = adp.getArticulosByLista(idLista);
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
    private void setupBluetooth(){
        bluetoothUtils = new BluetoothUtils(handler);
    }
    private void setStatus(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
    private void setStatus(String resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
    private void setStatus(int resId, String text) {
        Toast.makeText(this, getString(resId) + text, Toast.LENGTH_SHORT).show();
    }
    private void sendLista(ListaSerializable lista, List<Articulo> articulos) {
        // Check that we're actually connected before trying anything
        if (bluetoothUtils.getState() != BluetoothUtils.STATE_CONNECTED) {
            Toast.makeText(this, R.string.no_dispositivos, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (lista!=null && articulos!=null && articulos.size()>0) {
            ListaArticulo listaToSend = new ListaArticulo(lista, articulos);
            // Get the message bytes and tell the BluetoothChatService to write
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            try {
                try {
                    out = new ObjectOutputStream(bos);
                    out.writeObject(listaToSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] send = bos.toByteArray();
                bluetoothUtils.write(send);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ex) {
                    // ignore close exception
                }
                try {
                    bos.close();
                } catch (IOException ex) {
                    // ignore close exception
                }
            }
        }
    }
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothUtils.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothUtils.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothUtils.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothUtils.STATE_LISTEN:
                        case BluetoothUtils.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case BluetoothUtils.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    setStatus(R.string.lista_enviada);
                    reconectarBluetooth();
                    break;
                case BluetoothUtils.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(deviceName + ":  " + readMessage);

                    ByteArrayInputStream bis2 = new ByteArrayInputStream(readBuf);
                    ObjectInput in2 = null;
                    try {
                        try {
                            in2 = new ObjectInputStream(bis2);
                            ListaArticulo listaArticulo = (ListaArticulo)in2.readObject();
                            setStatus(R.string.lista_recibida);
                            guardarRecibido(listaArticulo);
                            setData();
                        } catch (IOException|ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        try {
                            bis2.close();
                        } catch (IOException ex) {
                            // ignore close exception
                        }
                        try {
                            if (in2 != null) {
                                in2.close();
                            }
                        } catch (IOException ex) {
                            // ignore close exception
                        }
                    }
                    break;
                case BluetoothUtils.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    deviceName = msg.getData().getString(BluetoothUtils.DEVICE_NAME);
                    setStatus(R.string.conectado_a, deviceName);
                    Log.i(BluetoothUtils.TAG, "Handle. ["+bluetoothAdapter.getName()+"] conectado a ["+deviceName+"]. Position to send["+positionToSend+"]");
                    if (positionToSend!=0) {
                        ListaSerializable toSend = listas.get(positionToSend);
                        List<Articulo> toSendArt = getArticulos(toSend.getId());
                        if (toSendArt == null || toSendArt.size() == 0)
                            setStatus(R.string.error_no_articulo_en_lista);
                        else sendLista(toSend, toSendArt);
                    }
                    break;
                case BluetoothUtils.MESSAGE_TOAST:
                    if (null != this) {
                        setStatus(msg.getData().getString(BluetoothUtils.TOAST));
                    }
                    break;
            }
        }
    };
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
}
