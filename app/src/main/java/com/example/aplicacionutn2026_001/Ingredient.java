package com.example.aplicacionutn2026_001;

public class Ingredient {
    public Integer ing_id;
    public String ing_name;
    public Integer ing_calories;
    public Boolean ing_state;
    public PizzaIngredient pizzaIngredient;

    // Para peticiones POST/PUT
    public Integer ing_quantity;

    public Ingredient() {}

    public Ingredient(Integer ing_id, Integer ing_quantity) {
        this.ing_id = ing_id;
        this.ing_quantity = ing_quantity;
    }
}
