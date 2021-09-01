package com.halfbyte.danv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.halfbyte.danv1.Chats.AdaptadorMensaje;
import com.halfbyte.danv1.Chats.MemberData;
import com.halfbyte.danv1.Chats.Mensaje;
import com.ibm.watson.developer_cloud.assistant.v1.model.*;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.http.ServiceCallback;


public class Chat extends AppCompatActivity {
    private EditText mensaje;
    private ImageButton enviar;
    private ListView listaMensaje;
    private TextView textView;

    private AdaptadorMensaje adaptadorMensaje;

    private static final int  EMISOR = 1;
    private static final int  RECEPTOR = 2;

    private boolean prueba;
    private boolean reconocimiento;

    //ChatBoot
    Assistant asistente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        textView = findViewById(R.id.txvBuscar);
        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)){
                    Intent intent = new Intent(Chat.this, Principal.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }else{
                    onBackPressed();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });

        mensaje = findViewById(R.id.Mensaje);

        adaptadorMensaje = new AdaptadorMensaje(this);
        listaMensaje = findViewById(R.id.lista_mensajes);
        listaMensaje.setAdapter(adaptadorMensaje);

        enviar = findViewById(R.id.Enviar);
        //ChatBoot
        asistente = new Assistant("2018-02-19");//necesario para realizar la conexión
        //estas son las credenciales que permite acceder al servicio de IBM Whatson
        asistente.setUsernameAndPassword("28e7a4e0-8316-448a-bb64-3196a7e0aed9","bOHn5CcGuCEE");
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });




    }

    /**
     * ChatBoot
     */
    private void SendMessage() {
        String texto = mensaje.getText().toString();//Obteneemos el texto que vamos a enviar

        final MemberData dataEmisor = new MemberData("Javier", "#fff");
        final MemberData dataReceptor = new MemberData("Dan", "#fff");

        Mensaje mensajeData;
        mensajeData = new Mensaje(texto,dataEmisor,true);
        prueba = !prueba;
        adaptadorMensaje.agregar(mensajeData);
        mensaje.getText().clear();

        InputData data= new InputData.Builder(texto).build();//construimos el objeto que será enviado junto al mensaje
        MessageOptions options= new MessageOptions.Builder("ed018c82-620e-4aaa-9216-4924611e4ac0"). //aqui especificamos el espacio de trabajo creado en IMB whatson
                input(data).
                build();//enviamos el menesaje
        //modo async= asincro
        asistente.message(options).enqueue(new ServiceCallback<MessageResponse>() {
            @Override
            public void onResponse(MessageResponse response) {
                //recuperados el data set devuelto por el servicio
                //la variable response posee un jason como respuesta
                //sin embargo la linea de abajo muestra solo el mensaje
                //presentarle al usuario como respuesta.
                Mensaje mensajeData;
                String respuesta = response.getOutput().getText().get(0);
                mensajeData = new Mensaje(respuesta,dataReceptor,false);
                adaptadorMensaje.agregar(mensajeData);
                mensaje.getText().clear();
            }

            @Override
            public void onFailure(Exception e) {
                //aqui puedes ver los estados de error en caso de que el mensaje no sea devuelto.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint( getText(R.string.app_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Chat.this, "Buscando", Toast.LENGTH_SHORT).show();
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                textView.setText(newText);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}
