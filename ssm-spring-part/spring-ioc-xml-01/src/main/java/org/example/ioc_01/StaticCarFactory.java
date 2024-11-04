package org.example.ioc_01;

public class StaticCarFactory {
    static public Car createCar(String brand, String model) {
        return new Car(brand, model);
    }
}
