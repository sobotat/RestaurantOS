package com.restaurantos_domain;

public class Food {
    private int foodId;
    private FoodType foodType;
    private String name;
    private String description;
    private String allergens;
    private double cost;

    public Food(int foodId, FoodType foodType, String name, String description, String allergens, double cost) {
        this.foodId = foodId;
        this.foodType = foodType;
        this.name = name;
        this.description = description;
        this.allergens = allergens;
        this.cost = cost;
    }

    // Getters
    public int getFoodId() {
        return foodId;
    }
    public FoodType getFoodType() {
        return foodType;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getAllergens() {
        return allergens;
    }
    public double getCost() {
        return cost;
    }

    public static class FoodType {
        int typeId;
        public String name;

        public FoodType(int typeId, String name) {
            this.typeId = typeId;
            this.name = name;
        }
    }
}
