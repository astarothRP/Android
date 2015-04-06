package com.astaroth.listacompra.layouts;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astaroth.listacompra.R;
import com.astaroth.listacompra.beans.ArticuloSerializable;

import java.util.Date;
import java.util.List;

/**
 * Created by Astaroth on 03/04/2015.
 */
public class ListaArticuloAdapter extends ArrayAdapter<ArticuloSerializable> {
    private int layoutResource;
    private List<ArticuloSerializable> articulos = null;

    public ListaArticuloAdapter(Context context, int resource, List<ArticuloSerializable> objects) {
        super(context, resource, objects);
        this.layoutResource = resource;
        this.articulos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (position<articulos.size()) {
            ArticuloHolder holder = null;

            if (row == null) {
                //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = LayoutInflater.from(getContext()).inflate(layoutResource, parent, false);

                holder = new ArticuloHolder();
                holder.campoCantidad = (TextView) row.findViewById(R.id.cantidad);
                holder.campoDescripcion = (TextView) row.findViewById(R.id.descripcion);
                holder.filaArticulo = (LinearLayout) row.findViewById(R.id.filaArticulo);
                row.setTag(holder);
            } else {
                holder = (ArticuloHolder) row.getTag();
            }
            ArticuloSerializable articulo = articulos.get(position);
            holder.campoCantidad.setText(articulo.getCantidad());
            holder.campoDescripcion.setText(articulo.getDescripcion());
            if (articulo.getMarcado()==1) holder.campoDescripcion.setPaintFlags(holder.campoDescripcion.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else holder.campoDescripcion.setPaintFlags(holder.campoDescripcion.getPaintFlags() &~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return row;
    }

    public static class ArticuloHolder{
        public TextView campoDescripcion;
        public TextView campoCantidad;
        public LinearLayout filaArticulo;
    }
}
