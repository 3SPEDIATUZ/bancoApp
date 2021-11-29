package com.example.bancoapp_mejorado.Metodos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metodos {


    // validar Correo
    public boolean validarCorreo(String email) {
        // Patrón para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }

    // validar password
    public boolean validarClave(String clave) {
        // Patrón para validar que el password contenga letras y numeros
        Pattern pattern = Pattern.compile("(([A-Za-z].[0-9])|([0-9].[A-Za-z]))");
        Matcher mather = pattern.matcher(clave);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }
}
