package org.example.ioc_01;

public class Car {
    private String brand;
    private String model;
    public Car(){}
    public Car(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    @Override
    public String toString() {
        return "Car [brand=" + brand + ", model=" + model + "]";
    }
}
