package com.restaurant_os.restaurantos;

public class Food {
    int foodId;
    FoodType foodType;
    String name;
    String description;
    String allergens;
    double cost;

    public Food(int foodId, FoodType foodType, String name, String description, String allergens, double cost) {
        this.foodId = foodId;
        this.foodType = foodType;
        this.name = name;
        this.description = description;
        this.allergens = allergens;
        this.cost = cost;
    }

    public static class FoodType {
        int typeId;
        String name;

        public FoodType(int typeId, String name) {
            this.typeId = typeId;
            this.name = name;
        }
    }
}
