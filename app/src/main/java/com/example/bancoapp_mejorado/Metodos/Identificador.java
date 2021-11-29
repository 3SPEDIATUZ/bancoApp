package com.example.bancoapp_mejorado.Metodos;

public class Identificador {
    public static Usuario user;
    public static String token;
    public static String codigo;

    public Identificador(Usuario user, String token, String codigo) {
        this.user=user;
        this.token=token;
        this.codigo=codigo;
    }

    public static Usuario getUser() {
        return user;
    }

    public static void setUser(Usuario user) {
        Identificador.user = user;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Identificador.token = token;
    }

    public static String getCodigo() {
        return codigo;
    }

    public static void setCodigo(String codigo) {
        Identificador.codigo = codigo;
    }

}
