package com.example.winmart.Object;

public class Invoices {
    int id;
    String date;
    String products;
    String productBarcode;
    int  totalQuantity;
    Double totalPrice;

    public Invoices() {
    }

    public Invoices(int id, String date, String products, String productBarcode, int totalQuantity, Double totalPrice) {
        this.id = id;
        this.date = date;
        this.products = products;
        this.productBarcode = productBarcode;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}