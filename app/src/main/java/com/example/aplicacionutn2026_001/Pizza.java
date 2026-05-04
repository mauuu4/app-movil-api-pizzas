package com.example.aplicacionutn2026_001;

import java.util.List;

public class Pizza {
    public Integer piz_id; // Usar Integer para que pueda ser null
    public String piz_name;
    public String piz_origin;
    public Boolean piz_state;
    public List<Ingredient> ingredients;
}
