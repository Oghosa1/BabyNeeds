package com.edna.babyneeds.model;

public class Items {
    private int id;
    private String itemName;
    private String itemColor;
    private int itemSize;
    private int itemQuant;
    private String dateItemAdded;

    public Items() {
    }

    public Items(int id, String itemName, String itemColor, int itemSize, int itemQuant, String dateItemAdded) {
        this.id = id;
        this.itemName = itemName;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.itemQuant = itemQuant;
        this.dateItemAdded = dateItemAdded;
    }

    public Items(String itemName, String itemColor, int itemSize, int itemQuant, String dateItemAdded) {
        this.itemName = itemName;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.itemQuant = itemQuant;
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    public int getItemQuant() {
        return itemQuant;
    }

    public void setItemQuant(int itemQuant) {
        this.itemQuant = itemQuant;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }
}
