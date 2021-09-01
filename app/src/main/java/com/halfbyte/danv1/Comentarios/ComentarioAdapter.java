package com.halfbyte.danv1.Comentarios;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.halfbyte.danv1.R;

import java.util.ArrayList;
import java.util.List;

public class ComentarioAdapter extends BaseAdapter {
    List<Comentario> comentarios = new ArrayList<Comentario>();;

    public ComentarioAdapter(){

    }

    public void agregar(Comentario comentario) {
        this.comentarios.add(comentario);
    }

    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int i) {
        return comentarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewComentario holder = new ViewComentario();
        LayoutInflater comentarioInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Comentario comentario = comentarios.get(i);

        view = comentarioInflater.inflate(R.layout.comentario,null);

        holder.persona = view.findViewById(R.id.usuario_comenta);
        holder.persona.setText(comentario.getPersona());

        holder.comentario = view.findViewById(R.id.comentario_body);
        holder.comentario.setText(comentario.getComentario());

        view.setTag(holder);

        return view;
    }

}
class ViewComentario {
    public TextView persona;
    public TextView comentario;
}
