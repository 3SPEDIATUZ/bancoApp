package com.example.bancoapp_mejorado.Metodos;

public class Coneccion {
    public static  String registro_usuario = "http://192.168.43.119:8080/user/register";
    public static String Login = "http://192.168.43.119:8080/user/login";
    public static  String Transferir = "http://192.168.43.119:8080/transaction/transfer";
    public static  String Historial = "http://192.168.43.119:8080/transaction/getTransfers";
    public static  String MIQR = "http://192.168.43.119:8080/QR/createCommonQRCode";
}
