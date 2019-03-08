package com.example.computer.moneymall.Model;

public class Products {
    public String image;
    public String name;
    public String rating;
    public String cost;
    public String discount;
    public Products() {
    }

    public Products(String image, String name, String rating, String cost, String discount) {
        this.image = image;
        this.name = name;
        this.rating = rating;
        this.cost = cost;
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
