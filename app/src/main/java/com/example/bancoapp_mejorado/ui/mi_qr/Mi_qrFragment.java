package com.example.bancoapp_mejorado.ui.mi_qr;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.bancoapp_mejorado.databinding.FragmentMiQrBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Mi_qrFragment extends Fragment {
    private Mi_qrViewModel mi_qrViewModel;
    private FragmentMiQrBinding  binding;
    ImageView qr;
    TextInputLayout nom_qr, cuen_qr;
    Coneccion coneccion =new Coneccion();
    RequestQueue requestQueue;
    Bitmap bitmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mi_qrViewModel =
                new ViewModelProvider(this).get(Mi_qrViewModel.class);

        binding = FragmentMiQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        qr= (ImageView) root.findViewById(R.id.Im_qr);
        nom_qr= (TextInputLayout) root.findViewById(R.id.perfil_nombre_completo_qr);
        cuen_qr= (TextInputLayout) root.findViewById(R.id.perfil_cuenta_rq);
        //------------------------------------------------------------------
        nom_qr.getEditText().setEnabled(false);
        cuen_qr.getEditText().setEnabled(false);
        //-----------------------------------------------------------------
        nom_qr.getEditText().setText(Identificador.user.getUser_name());
        cuen_qr.getEditText().setText(Identificador.user.getBill().getBill_number());
        //----------------------------------------------------------
        requestQueue = Volley.newRequestQueue(getActivity());
        //----------------------------------------------------------
        MiQr();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //------------------------------------------------------------------
    public void mostrar_qr(String imagen){
        byte[] imageBytes = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        qr.setImageBitmap(decodedImage);

    }

    //-------------------------------------------------------------------
    public void MiQr() {

        ProgressDialog cargando=new ProgressDialog(getActivity());
        cargando.setTitle("Obteniedo QR");
        cargando.setMessage("Espere por favor...");
        cargando.show();//lo inicio

        final Gson gson = new GsonBuilder().create();

        JSONObject jsonObject = new JSONObject();//creo el json que voy a enviar
        try {
            jsonObject.put("numberBill", Identificador.user.getBill().getBill_number());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("mI QR body", jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                coneccion.MIQR,
                jsonObject,// este es el body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cargando.dismiss();//cerrar dialogo
                        Log.e("MiQR respuesta: ", response.toString());
                        try {
                            Boolean status = response.getBoolean("status");
                            String imagen = response.getString("img");
                            String msg = response.getString("msg");
                            if(status){
                                mostrar_qr(imagen);
                            }else{
                                Toast.makeText(getContext(),"Error al obtener MiQR",Toast.LENGTH_LONG).show();
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
                params.put("Authorization",Identificador.token);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
}
