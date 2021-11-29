package com.example.bancoapp_mejorado.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bancoapp_mejorado.Metodos.Identificador;
import com.example.bancoapp_mejorado.R;
import com.example.bancoapp_mejorado.databinding.FragmentPerfilBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;


public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilViewModel;
    TextInputLayout perf_nombr, perfil_cuen, perfil_sal;
    private FragmentPerfilBinding  binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilViewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        perf_nombr = (TextInputLayout) root.findViewById(R.id.perfil_nombre_completo);
        perfil_cuen = (TextInputLayout) root.findViewById(R.id.perfil_cuenta);
        perfil_sal = (TextInputLayout) root.findViewById(R.id.perfil_saldo);
        //------------------------------------------------------------------
        perf_nombr.getEditText().setEnabled(false);
        perfil_cuen.getEditText().setEnabled(false);
        perfil_sal.getEditText().setEnabled(false);
        //-----------------------------------------------------------------
        perf_nombr.getEditText().setText(Identificador.user.getUser_name());
        perfil_cuen.getEditText().setText(Identificador.user.getBill().getBill_number());
        perfil_sal.getEditText().setText("$ " + formatter.format(Integer.parseInt(Identificador.user.getBill().getBill_amount())));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
