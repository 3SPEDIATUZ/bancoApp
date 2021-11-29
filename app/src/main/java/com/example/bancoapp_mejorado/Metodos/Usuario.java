package com.example.bancoapp_mejorado.Metodos;

public class Usuario {
    private String user_id;
    private String user_name;
    private String user_identification;
    private String user_email;
    private String  user_estate;
    private Bill bill;
    private String user_password;

    public Usuario(){

    }

    public Usuario(String user_id, String user_name, String user_identification, String user_email, String user_estate, Bill bill, String user_password) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_identification = user_identification;
        this.user_email = user_email;
        this.user_estate = user_estate;
        this.bill = bill;
        this.user_password = user_password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_identification() {
        return user_identification;
    }

    public void setUser_identification(String user_identification) {
        this.user_identification = user_identification;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_estate() {
        return user_estate;
    }

    public void setUser_estate(String user_estate) {
        this.user_estate = user_estate;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
