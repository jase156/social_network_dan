package com.halfbyte.danv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.halfbyte.danv1.Comentarios.Comentario;
import com.halfbyte.danv1.Comentarios.ComentarioAdapter;

import java.util.ArrayList;

public class Historia extends AppCompatActivity {

    ImageView imagenHistoria;
    ImageButton likeButon;
    ImageButton favoritoButon;
    ImageButton guardarBoton;
    TextView tituloHistoria;
    TextView historia;
    ListView listaComentario;
    EditText comentario;
    ImageButton enviarBoton;

    private ArrayList<Comentario> comentarios;

    ComentarioAdapter adaptadorComentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);

        Toolbar toolbar = findViewById(R.id.toolbarHistoria);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        imagenHistoria = findViewById(R.id.hMedia_image);
        //likeButon = findViewById(R.id.hLike_button);
        //favoritoButon = findViewById(R.id.hFavorite_button);
        //guardarBoton = findViewById(R.id.hShare_button);
        tituloHistoria = findViewById(R.id.hTitulo_historia);
        historia = findViewById(R.id.hSupporting_text);
        listaComentario = findViewById(R.id.hLista_comentarios);
        comentario = findViewById(R.id.hComenta);
        enviarBoton = findViewById(R.id.hEnviar_comentario);

        Bundle bundle = getIntent().getExtras();

        String titulo = bundle.getString("titulo");
        String texto = bundle.getString("texto");
        comentarios =  getIntent().getParcelableArrayListExtra("comentarios");

        imagenHistoria.setImageResource(R.drawable.prueba2);
        tituloHistoria.setText(titulo);
        historia.setText(texto);

        adaptadorComentario = new ComentarioAdapter();
        listaComentario.setAdapter(adaptadorComentario);

        for (int i = 0; i < comentarios.size(); i++){
            adaptadorComentario.agregar(comentarios.get(i));
            listaComentario.getLayoutParams().height=150*i+1;
        }

        enviarBoton.setOnClickListener(new View.OnClickListener() {
            Comentario objetoComentario;
            @Override
            public void onClick(View view) {
                String comentarioTexto = comentario.getText().toString();
                objetoComentario = new Comentario();
                objetoComentario.setPersona("J");
                objetoComentario.setComentario(comentarioTexto);
                adaptadorComentario.agregar(objetoComentario);
                listaComentario.getLayoutParams().height = listaComentario.getLayoutParams().height + 150;
                comentario.setText("");
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_historia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.hAction_favorito:
                Toast.makeText(Historia.this, "Se guardo en favorito", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.hAction_guardar:
                Toast.makeText(Historia.this, "Se guardo esta historia", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.hAction_like:
                Toast.makeText(Historia.this, "Se dio like", Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
