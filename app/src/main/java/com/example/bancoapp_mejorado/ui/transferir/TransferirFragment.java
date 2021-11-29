package com.example.bancoapp_mejorado.ui.transferir;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.bancoapp_mejorado.databinding.FragmentTransferirBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TransferirFragment extends Fragment {
    private TransferirViewModel transferirViewModel;
    TextInputLayout cuenta, monto;
    Button accion_transferir;
    Coneccion coneccion =new Coneccion();
    RequestQueue requestQueue;
    private FragmentTransferirBinding  binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transferirViewModel =
                new ViewModelProvider(this).get(TransferirViewModel.class);

        binding = FragmentTransferirBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cuenta = (TextInputLayout)root.findViewById(R.id.Cuenta_destino);
        monto = (TextInputLayout)root.findViewById(R.id.monto);
        accion_transferir = (Button) root.findViewById(R.id.transferir_bt);
        //----------------------------------------------------------
        requestQueue = Volley.newRequestQueue(getActivity());
        //----------------------------------------------------------
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
}
