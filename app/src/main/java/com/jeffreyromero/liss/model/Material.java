package com.jeffreyromero.liss.model;

/**
 * The type field is used by Gson to identify subtypes for deserialization.
 * All subtypes must pass their class name to this type field.
 *
 */
public abstract class Material {
    protected String type;
    protected String name;
    protected int price;
    protected double length;
    protected double quantity;

    // Stores value to quantity variable on subclasses.
    public abstract double calcQuantity(double length, double width);
    public abstract double calcQuantity(double area);

    public Material(String type, String name, int price, double length) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
