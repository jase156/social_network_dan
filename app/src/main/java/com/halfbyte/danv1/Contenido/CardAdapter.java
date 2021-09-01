package com.halfbyte.danv1.Contenido;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.halfbyte.danv1.Comentarios.Comentario;
import com.halfbyte.danv1.Comentarios.ComentarioAdapter;
import com.halfbyte.danv1.Historia;
import com.halfbyte.danv1.R;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<Card> mCards;
    Context context;



    private boolean isExpand=true;




    public  CardAdapter (){
        super();
        mCards = new ArrayList<Card>();

        Card card = new Card();

        card.setmName("L");
        card.setmFecha("06/08/2018");
        card.setmTitulo("Empecé a odiarme a mí misma");
        card.setmResumen("Mi vida es como una montaña rusa. Nunca sé cómo va a ir.");
        card.setmHistoria("Mi vida es como una montaña rusa. Nunca sé cómo va a ir.\n" +
                "\n" +
                "Este año empezó mal. En mi antiguo instituto empecé a darme cuenta de que no era tan bonito como imaginaba. Vinieron muchos problemas.\n" +
                "\n" +
                "En el instituto Ingeniero de la Cierva no tenía a nadie. Sólo me hablaban para insultarme. Empecé a odiarme a mí misma. \n" +
                "\n" +
                "Luego pude olvidar a gente que nunca debió estar en mi vida. Llegué a mi nuevo instituto y dos chicas maravillosas me acogieron. Yessi y Thais, os quiero. \n");
        card.setmImagen(R.drawable.prueba2);
        mCards.add(card);

        card = new Card();
        card.setmName("J");
        card.setmFecha("28/07/2018");
        card.setmTitulo("Mi vida es como una montaña rusa.");
        card.setmResumen("Este año empezó mal. En mi antiguo instituto empecé a darme cuenta de que no era tan bonito como imaginaba. Vinieron muchos problemas.\n");
        card.setmHistoria("Mi vida es como una montaña rusa. Nunca sé cómo va a ir.\n" +
                "\n" +
                "Este año empezó mal. En mi antiguo instituto empecé a darme cuenta de que no era tan bonito como imaginaba. Vinieron muchos problemas.\n" +
                "\n" +
                "En el instituto Ingeniero de la Cierva no tenía a nadie. Sólo me hablaban para insultarme. Empecé a odiarme a mí misma. \n" +
                "\n" +
                "Luego pude olvidar a gente que nunca debió estar en mi vida. Llegué a mi nuevo instituto y dos chicas maravillosas me acogieron. Yessi y Thais, os quiero. \n");
        card.setmImagen(R.drawable.prueba);
        mCards.add(card);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_card_item,parent,false);

        context = parent.getContext();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CardAdapter.ViewHolder holder, final int position) {
        final Card card = mCards.get(position);
        final List<Comentario> listComentarios = new ArrayList<Comentario>();


        holder.inicial.setText(card.getmName());
        holder.fecha.setText(card.getmFecha());
        holder.mediaImage.setImageResource(card.getmImagen());
        holder.tituloHistoria.setText(card.getmTitulo());
        holder.resumen.setText(card.getmResumen());
        holder.historia.setText(card.getmHistoria());
        holder.collapse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holder.historia.setVisibility( isExpand ? View.VISIBLE : View.GONE);
                holder.collapse.setImageResource(isExpand ? R.drawable.ic_expand_less_black_36dp : R.drawable.ic_expand_more_black_36dp);
                holder.resumen.setVisibility(isExpand ? View.GONE : View.VISIBLE);
                holder.listaComentario.setVisibility(isExpand ? View.VISIBLE : View.GONE);
                isExpand = !isExpand;
            }
        });

        holder.enviarComentario.setOnClickListener(new View.OnClickListener(){
            boolean inicio = true;
            int comentarios=1;
            //comentarios
            ComentarioAdapter adaptadorComentario;
            @Override
            public void onClick(View v) {
                if(inicio){
                    adaptadorComentario = new ComentarioAdapter();
                    inicio = false;
                }
                holder.listaComentario.getLayoutParams().height=150*comentarios;
                comentarios++;
                holder.listaComentario.setAdapter(adaptadorComentario);
                String comentarioTexto = holder.comentario.getText().toString();
                Comentario comentario = new Comentario();
                comentario.setPersona("J");
                comentario.setComentario(comentarioTexto);
                listComentarios.add(comentario);
                adaptadorComentario.agregar(comentario);
                holder.comentario.getText().clear();
                holder.textoComentarios.setText(adaptadorComentario.getCount()==1 ? "Comentario" : "Comentarios");
                holder.numeroComentarios.setText(adaptadorComentario.getCount()>0 ? ""+adaptadorComentario.getCount() : "" );

            }
        });

        holder.tituloHistoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Historia.class);
                intent.putExtra("titulo",card.getmTitulo());
                intent.putExtra("texto", card.getmHistoria());
                intent.putParcelableArrayListExtra("comentarios", (ArrayList<? extends Parcelable>) listComentarios);
                context.startActivity(intent);
                Activity mActivity = (Activity) context;
                mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView inicial;
        public TextView fecha;
        public ImageView mediaImage;
        public TextView tituloHistoria;
        public TextView resumen;
        public TextView historia;
        public ImageButton collapse;
        private TextView numeroComentarios;
        private TextView textoComentarios;
        private ListView listaComentario;
        private EditText comentario;
        private ImageButton enviarComentario;

        public ViewHolder(View itemView) {
            super(itemView);
            inicial = itemView.findViewById(R.id.icon_text);
            fecha = itemView.findViewById(R.id.fecha_publicacion);
            mediaImage = itemView.findViewById(R.id.media_image);
            tituloHistoria = itemView.findViewById(R.id.titulo_historia);
            resumen = itemView.findViewById(R.id.sub_text);
            historia = itemView.findViewById(R.id.supporting_text);
            collapse = itemView.findViewById(R.id.expand_button);
            numeroComentarios = itemView.findViewById(R.id.txtNumeroComentarios);
            textoComentarios = itemView.findViewById(R.id.txtComentario);
            listaComentario = itemView.findViewById(R.id.lista_comentarios);
            comentario = itemView.findViewById(R.id.comenta);
            enviarComentario = itemView.findViewById(R.id.enviar_comentario);

        }
    }
}
