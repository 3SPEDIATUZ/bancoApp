package com.example.bancoapp_mejorado.ui.transferir_qr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoapp_mejorado.Metodos.Coneccion;
import com.example.bancoapp_mejorado.Metodos.Identificador;
import com.example.bancoapp_mejorado.R;
import com.example.bancoapp_mejorado.databinding.FragmentTransferirQrBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Transferir_qrFragment extends Fragment {
    private Transferir_qrViewModel transferirViewModel;
    TextInputLayout cuenta, monto;
    Button accion_transferir;
    Coneccion coneccion =new Coneccion();
    RequestQueue requestQueue;
    private FragmentTransferirQrBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transferirViewModel =
                new ViewModelProvider(this).get(Transferir_qrViewModel.class);

        binding = FragmentTransferirQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cuenta = (TextInputLayout)root.findViewById(R.id.Cuenta_destino_QR);
        monto = (TextInputLayout)root.findViewById(R.id.monto_QR);
        accion_transferir = (Button) root.findViewById(R.id.transferir_QR_bt);
        //----------------------------------------------------------
        requestQueue = Volley.newRequestQueue(getActivity());
        //----------------------------------------------------------
        IntentIntegrator integrador = new IntentIntegrator(getActivity());
        // aca decimos que tipos de codigos puede leer; para no buscar elegimos todos
        integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        // asi colocamos la leyenda o un titulo que no va a mostrar nuestro lector
        integrador.setPrompt("Lector - QR");
        // aca decimos que camara va a utilizar, en este caso la trasera (0)
        integrador.setCameraId(0);
        // esto es para que de una alerta cuando se lee el codigo, para que se escuche
        integrador.setBeepEnabled(true);
        // esto es para que puede leer las imagenes correctamente
        integrador.setBarcodeImageEnabled(true);
        //esto es para que sea la orientacion vertical
        integrador.setOrientationLocked(true);
        //
        integrador.setBeepEnabled(true);
        // por ultimo inicializamos este elemento
        integrador.initiateScan();
        //---------------------------------------------------------------
        accion_transferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(cuenta.getEditText().getText().toString()).trim().isEmpty()){
                    if(!(monto.getEditText().getText().toString()).trim().isEmpty()){
                        if((cuenta.getEditText().getText().toString()).length()==10){
                            if(Integer.parseInt(monto.getEditText().getText().toString())>=1000){
                                transferir((cuenta.getEditText().getText().toString()),(monto.getEditText().getText().toString()));
                            }else {
                                monto.requestFocus();
                                mensaje("Ojo el monto minimo es de $1000");
                            }
                        }else{
                            cuenta.requestFocus();
                            mensaje("Ojo el numero de cuenta es de 10 caracteres");
                        }
                    }else{
                        monto.requestFocus();
                        mensaje("Por favor, Digite el monto a transferir");
                    }
                }else{
                    cuenta.requestFocus();
                    mensaje("Por favor,Digite el numero de cuenta destinatario");
                }

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //-------------------------------------------------------------------
    public void transferir(String cuentaDestino,String monto) {

        ProgressDialog cargando=new ProgressDialog(getActivity());
        cargando.setTitle("Realizando Transsacción");
        cargando.setMessage("Espere por favor...");
        cargando.show();//lo inicio

        final Gson gson = new GsonBuilder().create();

        JSONObject jsonObject = new JSONObject();//creo el json que voy a enviar
        try {
            jsonObject.put("numberBill", Identificador.user.getBill().getBill_number());
            jsonObject.put("numberBillDestiny", cuentaDestino);
            jsonObject.put("typeTransation", "TRANSFERENCIA");
            jsonObject.put("amount", monto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Transferir dinero body", jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                coneccion.Transferir,
                jsonObject,// este es el body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cargando.dismiss();//cerrar dialogo
                        Log.e("Transferir respuesta: ", response.toString());
                        try {
                            Boolean status = response.getBoolean("status");
                            String msg = response.getString("msg");
                            if(status){
                                //cuenta = new Cuenta();
                                //ListaUsuarios.cuenta = gson.fromJson(String.valueOf(response.getJSONObject("data")), Cuenta.class);
                                int montoInt=Integer.parseInt(monto);
                                int saldo=Integer.parseInt(Identificador.user.getBill().getBill_amount());
                                saldo-=montoInt;
                                //ListaUsuarios.listaUsers.get(posicion).getBill().setBill_amount(String.valueOf(saldo));
                                Identificador.user.getBill().setBill_amount(String.valueOf(saldo));
                                //tvSaldo.setText("Saldo: "+Identificador.user.getBill().getBill_amount());
                                Log.e("","Saldo del Objeto cuenta:"+Identificador.user.getBill().getBill_amount());

                            }else{
                                Toast.makeText(getContext(),"Error al realizar la transferencia",Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cargando.dismiss();
                        Toast.makeText(getContext(),"Cant connect to server",Toast.LENGTH_LONG).show();
                        Log.d("","error--->"+error);
                    }
                }
        ){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                // Map<String,String> params = Global.getHeaders();
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", Identificador.token);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
    //--------------------------------------------------------------------------------------------------
    public void mensaje(String x){
        Toast.makeText( getContext() , x,Toast.LENGTH_SHORT).show();
    }
    //---------------------------------------------------------------------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            //esto es en caso de que cancelemos el proceso
            if(result.getContents()==null){
                Toast.makeText(getContext(),"Lectura cancelada", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_LONG).show();
                cuenta.getEditText().setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //---------------------------------------------------------------------------------------------------
}
