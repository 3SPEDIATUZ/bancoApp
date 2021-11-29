package com.example.bancoapp_mejorado.ActivityPrincipales;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.bancoapp_mejorado.MainActivity;
import com.example.bancoapp_mejorado.Metodos.Coneccion;
import com.example.bancoapp_mejorado.Metodos.Identificador;
import com.example.bancoapp_mejorado.Metodos.Metodos;
import com.example.bancoapp_mejorado.Metodos.Usuario;
import com.example.bancoapp_mejorado.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout usu , contra;
    Button ini, res;
    Toolbar toolbar;
    Boolean validador_usuario, validador_clave;
    Metodos met=new Metodos();
    RequestQueue requestQueue;
    Coneccion coneccion =new Coneccion();
    private String url_lon= coneccion.Login;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_login);
        //definimos las variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));//-->Colocamos el titulo del toolbar "Recargas App"
        toolbar.setSubtitle(getString(R.string.inicio)); //-->Colocamos el subtitulo del toolbar "Iniciar Sesion"
        setSupportActionBar(toolbar);//Barra de herramientas para establecer como la barra de acción de la actividad.
        //-------------------------------------------------------
        usu= (TextInputLayout) findViewById(R.id.usu);
        contra=(TextInputLayout) findViewById(R.id.contra);
        ini=(Button) findViewById(R.id.ingreBt);
        res=(Button) findViewById(R.id.regis_Bt);
        usu.getEditText().setText("jorge@gmail.com");
        contra.getEditText().setText("ivan97");
        //----------------------------------------------------------
        requestQueue = Volley.newRequestQueue(this);
        //----------------------------------------------------------
        //---------------------------------------------------------------
        ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validador_usuario=met.validarCorreo(usu.getEditText().getText().toString());
                validador_clave=met.validarClave(contra.getEditText().getText().toString());
                if(!(usu.getEditText().getText().toString()).trim().isEmpty()){
                    if(!(contra.getEditText().getText().toString()).trim().isEmpty()){
                        if(validador_usuario){
                            if(validador_clave){
                                Logueo((usu.getEditText().getText().toString()),(contra.getEditText().getText().toString()));

                            }else{
                                contra.requestFocus();
                                mensaje("Ojo digite una clave valida");
                            }
                        }else{
                            usu.requestFocus();
                            mensaje("Ojo digite un usuario valido");
                        }
                    }else{
                        contra.requestFocus();
                        mensaje("Por favor,Digite la clave");
                    }
                }else{
                    usu.requestFocus();
                    mensaje("Por favor, Digite el usuario");
                }
            }
        });
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
            }
        });
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

    public void registro(){
        Intent intenta = new Intent(this, RegisterActivity.class);
        startActivity(intenta);
    }
    public void mensaje(String x){
        Toast.makeText( this , x,Toast.LENGTH_SHORT).show();
    }

    public void Logueo (String correo, String clave) {

        ProgressDialog cargando = new ProgressDialog(this);
        cargando.setTitle("Iniciando sesión");
        cargando.setMessage("Espere por favor...");
        cargando.show();//lo inicio


        final Gson gson = new GsonBuilder().create();

        JSONObject jsonObject = new JSONObject();//creo el json que voy a enviar
        try {
            jsonObject.put("user_email", correo);
            jsonObject.put("user_password", clave);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Usuario body", jsonObject.toString());
        Log.e("URL","*************************++++ "+ coneccion.Login);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                coneccion.Login,
                jsonObject,// este es el body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cargando.dismiss();//cerrar dialogo
                        Log.e("Login respuesta", response.toString());
                        try {
                            Boolean status = response.getBoolean("status");
                            String msg = response.getString("msg");
                            Identificador.token = response.getString("token");
                            if (status) {
                                Identificador.user = new Usuario();
                                Identificador.user = gson.fromJson(String.valueOf(response.getJSONObject("data")), Usuario.class);
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Log.e("cuenta:", "" + Identificador.user.getBill().getBill_number());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cargando.dismiss();
                        Toast.makeText(LoginActivity.this, "Cant connect to server", Toast.LENGTH_LONG).show();
                        Log.d("", "error--->" + error);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    //-----------------------------------------------------------------------
}
