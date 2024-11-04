package org.example.ioc_01;

public class CarFactory {
    public Car createCar(String brand, String model) {
        return new Car(brand, model);
    }
}
