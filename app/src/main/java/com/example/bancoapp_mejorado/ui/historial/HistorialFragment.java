package com.example.bancoapp_mejorado.ui.historial;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoapp_mejorado.Adapter.Adapter;
import com.example.bancoapp_mejorado.Metodos.Bill;
import com.example.bancoapp_mejorado.Metodos.Coneccion;
import com.example.bancoapp_mejorado.Metodos.Identificador;
import com.example.bancoapp_mejorado.Metodos.Transaction;
import com.example.bancoapp_mejorado.R;
import com.example.bancoapp_mejorado.databinding.FragmentHistorialBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistorialFragment extends Fragment implements Adapter.RecyclerItemClick{

    private HistorialViewModel hitorialViewModel;
    RecyclerView recyclerView;
    Adapter adapter;
    Bill cuenta=new Bill();
    Coneccion coneccion =new Coneccion();
    RequestQueue requestQueue;

    private FragmentHistorialBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hitorialViewModel =
                new ViewModelProvider(this).get(HistorialViewModel.class);

        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot(); requestQueue = Volley.newRequestQueue(getActivity());
        recyclerView = root.findViewById(R.id.rvLista);
        transferir();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void OnClick(Transaction item) {

    }
    public void mostrarDatos(){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new Adapter(cuenta.getTransaction(), item -> recyclerView.getContext());
        recyclerView.setAdapter(adapter);
    }
    //-------------------------------------------------------------------
    public void transferir() {

        ProgressDialog cargando=new ProgressDialog(getActivity());
        cargando.setTitle("Mostrando Historial");
        cargando.setMessage("Espere por favor...");
        cargando.show();//lo inicio

        final Gson gson = new GsonBuilder().create();

        JSONObject jsonObject = new JSONObject();//creo el json que voy a enviar
        try {
            jsonObject.put("numberBill", Identificador.user.getBill().getBill_number());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Historial  body", jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                coneccion.Historial,
                jsonObject,// este es el body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cargando.dismiss();//cerrar dialogo
                        Log.e("Historial respuesta: ", response.toString());
                        try {
                            String msg = response.getString("msg");
                            JSONArray data = response.getJSONArray("data");
                            Boolean status = response.getBoolean("status");

                            if(status){
                                ArrayList<Transaction> transactions = null;
                                //---------------------------------------------------------
                                if(data.length()!=0){
                                    String cat;
                                    transactions = new ArrayList<Transaction>();
                                    for (int i = 0; i < data.length(); i++) {
                                        cat = data.getString(i);
                                        Transaction cate = gson.fromJson(cat, Transaction.class);
                                        transactions.add(cate);
                                    }
                                }
                                cuenta.setTransaction(transactions);
                                mostrarDatos();
                            }else{
                                Toast.makeText(getContext(),"Error al realizar el historial",Toast.LENGTH_LONG).show();
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
    //----------------------------------------------------------------------------------------
}
