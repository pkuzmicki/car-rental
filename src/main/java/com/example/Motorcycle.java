package com.example;

public class Motorcycle extends Vehicle{
    String category;

    public Motorcycle(String id, String brand, String model, int year, int price, boolean rented, String category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }

    @Override
    public String toCsV(){
        return id + ";" + brand  + ";" + model  + ";" + year  + ";" + price + ";" + rented + ";" + category;
    }

    public static boolean equals(Vehicle a, Vehicle b){
        if (a.getClass() == b.getClass()) {
            Motorcycle a1 = ((Motorcycle) a);
            Motorcycle b1 = ((Motorcycle) b);
            return a1.id.equals(b1.id) && a1.brand.equals(b1.brand) && a1.model.equals(b1.model) && a1.year == b1.year && a1.price == b1.price && a1.rented == b1.rented && a1.category.equals(b1.category);
        }
        return false;
    }
}
