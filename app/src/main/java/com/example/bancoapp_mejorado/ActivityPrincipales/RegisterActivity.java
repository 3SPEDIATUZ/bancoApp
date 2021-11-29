package com.example.bancoapp_mejorado.ActivityPrincipales;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.bancoapp_mejorado.Metodos.Coneccion;
import com.example.bancoapp_mejorado.Metodos.Metodos;
import com.example.bancoapp_mejorado.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout user_name , user_identification, user_email, user_password;
    String user_estate = "ACTIVO";
    Boolean validador_usuario, validador_clave;
    Button reg;
    Toolbar toolbar;
    Metodos met=new Metodos();
    Coneccion coneccion =new Coneccion();
    RequestQueue requestQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_register);
        //definimos las variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));//-->Colocamos el titulo del toolbar "Recargas App"
        toolbar.setSubtitle(getString(R.string.regist)); //-->Colocamos el subtitulo del toolbar "Registro de usuario"
        setSupportActionBar(toolbar);//Barra de herramientas para establecer como la barra de acción de la actividad.
        //-------------------------------------------------------
        user_name=(TextInputLayout) findViewById(R.id.nombre_completo_usuario);
        user_identification= (TextInputLayout) findViewById(R.id.regitro_cc);
        user_email= (TextInputLayout) findViewById(R.id.registro_email);
        user_password= (TextInputLayout) findViewById(R.id.registro_clave);
        reg= (Button) findViewById(R.id.registro_bt);
        //----------------------------------------------------------
        requestQueue = Volley.newRequestQueue(this);
        //----------------------------------------------------------

        //---------------------------------------------------------
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validador_usuario=met.validarCorreo(user_email.getEditText().getText().toString());
                validador_clave=met.validarClave(user_password.getEditText().getText().toString());
                if(validador_usuario){
                    if(validador_clave){
                        if(!(user_name.getEditText().getText().toString()).trim().isEmpty()){
                            if(!(user_identification.getEditText().getText().toString()).trim().isEmpty()){
                                if(!(user_email.getEditText().getText().toString()).trim().isEmpty()){
                                    if(!(user_password.getEditText().getText().toString()).trim().isEmpty()){
                                        registrar((user_name.getEditText().getText().toString()),(user_identification.getEditText().getText().toString()),(user_email.getEditText().getText().toString()),(user_password.getEditText().getText().toString()),user_estate);
                                        finish();
                                    }else{
                                        user_password.requestFocus();
                                        mensaje("Por favor, Digite la clave");
                                    }
                                }else{
                                    user_email.requestFocus();
                                    mensaje("Por favor, Digite el correo electronico");
                                }
                            }else{
                                user_identification.requestFocus();
                                mensaje("Por favor, Digite el numero de cedula");
                            }
                        }else{
                            user_name.requestFocus();
                            mensaje("Por favor, Digite el nombre completo");
                        }

                    }else{
                        user_password.requestFocus();
                        mensaje("Ojo la clave no es valida");
                    }
                }else{
                    user_email.requestFocus();
                    mensaje("Ojo correo electronico no valido");
                }

            }
        });
        //---------------------------------------------------------------
    }

    // metodo que carga el menu a mostrar que se encuentra en la ruta> res>menu> main.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú; esto agrega elementos a la barra de acción si está presente
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics del elemento de la barra de acción aquí.
        // La barra de acción manejará automáticamente los clics en el botón Inicio / Arriba,
        // siempre que especifique una actividad principal en AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {

            finishAffinity(); //finaliza la actividad o cierra la app
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void mensaje(String x){
        Toast.makeText( this , x,Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------
    public void registrar(String nombre_completo, String cedula, String correo, String clave, String estado) {


        final Gson gson = new GsonBuilder().create();

        JSONObject jsonObject = new JSONObject();//creo el json que voy a enviar
        try {
            jsonObject.put("user_name", nombre_completo);
            jsonObject.put("user_identification", cedula);
            jsonObject.put("user_email", correo);
            jsonObject.put("user_password", clave);
            jsonObject.put("user_estate", estado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Usuario body", jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                coneccion.registro_usuario,
                jsonObject,// este es el body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Login respuesta", response.toString());
                        try {
                            Boolean status = response.getBoolean("status");
                            String msg = response.getString("msg");
                            if (status) {

                                Toast.makeText(RegisterActivity.this, "El usuario se ha registrado exitosamente", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(RegisterActivity.this, "El usuario no se ha podido registrar", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Cant connect to server", Toast.LENGTH_LONG).show();
                        Log.d("", "error--->" + error);
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    //--------------------------------------------------------------------------
}
