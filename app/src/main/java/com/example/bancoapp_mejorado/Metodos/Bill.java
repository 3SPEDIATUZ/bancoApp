package com.example.bancoapp_mejorado.Metodos;

import java.util.ArrayList;

public class Bill {
    private ArrayList<Transaction> transaction;
    private String bill_number;
    private String bill_amount;

    public Bill(){

    }

    public Bill(ArrayList<Transaction> transaction, String bill_number, String bill_amount) {
        this.transaction = transaction;
        this.bill_number = bill_number;
        this.bill_amount = bill_amount;
    }

    public ArrayList<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(ArrayList<Transaction> transaction) {
        this.transaction = transaction;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }
}
