package com.example.winmart.Object;

import java.sql.Date;

public class Products {
    String productID, productName, productQuantity, productUnit, productBarcode, productPrice, productStatus;
    Date productManufacturing, productExpiration;

    public Products() {
    }

    public Products(String productID, String productName, String productQuantity, String productUnit, String productBarcode, String productPrice, String productStatus, Date productManufacturing, Date productExpiration) {
        this.productID = productID;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productUnit = productUnit;
        this.productBarcode = productBarcode;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productManufacturing = productManufacturing;
        this.productExpiration = productExpiration;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Date getProductManufacturing() {
        return productManufacturing;
    }

    public void setProductManufacturing(Date productManufacturing) {
        this.productManufacturing = productManufacturing;
    }

    public Date getProductExpiration() {
        return productExpiration;
    }

    public void setProductExpiration(Date productExpiration) {
        this.productExpiration = productExpiration;
    }
}
