package com.halfbyte.danv1.Chats;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.halfbyte.danv1.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorMensaje extends BaseAdapter {
    List<Mensaje> mensajes = new ArrayList<Mensaje>();
    Context context;

    public AdaptadorMensaje(Context context){
        this.context = context;
    }

    public void agregar(Mensaje message) {
        this.mensajes.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mensajes.size();
    }

    @Override
    public Object getItem(int i) {
        return mensajes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TitularMensaje holder = new TitularMensaje();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Mensaje message = mensajes.get(i);

        if (message.enviadoPorUsuario()) {
            view = messageInflater.inflate(R.layout.mi_mensaje, null);
            holder.cuerpoMensaje = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);
            holder.cuerpoMensaje.setText(message.getTexto());
        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            view = messageInflater.inflate(R.layout.mensaje_otro, null);
            holder.avatar = (View) view.findViewById(R.id.avatar);
            holder.nombre = (TextView) view.findViewById(R.id.name);
            holder.cuerpoMensaje = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);

            holder.nombre.setText(message.getDatos().getName());
            holder.cuerpoMensaje.setText(message.getTexto());
        }

        return view;
    }
}


class TitularMensaje {
    public View avatar;
    public TextView nombre;
    public TextView cuerpoMensaje;
}