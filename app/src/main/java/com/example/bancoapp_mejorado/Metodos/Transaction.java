package com.example.bancoapp_mejorado.Metodos;

public class Transaction {
    private int transaction_id;
    private String transaction_type;
    private String transaction_amount;
    private String transaction_description;
    private String transaction_date;
    private boolean transaction_estate;

    public Transaction(int transaction_id, String transaction_type, String transaction_amount, String transaction_description, String transaction_date, boolean transaction_estate) {
        this.transaction_id = transaction_id;
        this.transaction_type = transaction_type;
        this.transaction_amount = transaction_amount;
        this.transaction_description = transaction_description;
        this.transaction_date = transaction_date;
        this.transaction_estate = transaction_estate;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_description() {
        return transaction_description;
    }

    public void setTransaction_description(String transaction_description) {
        this.transaction_description = transaction_description;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public boolean isTransaction_estate() {
        return transaction_estate;
    }

    public void setTransaction_estate(boolean transaction_estate) {
        this.transaction_estate = transaction_estate;
    }
}
