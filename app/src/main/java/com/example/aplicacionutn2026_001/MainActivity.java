package com.example.aplicacionutn2026_001;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtId, txtName, txtOrigin, txtIngQuantity;
    AutoCompleteTextView spinnerIngredients;
    ChipGroup chipGroupIngredients;
    LinearProgressIndicator loadingIndicator;
    List<Ingredient> tempIngredients = new ArrayList<>();
    List<Ingredient> masterIngredients = new ArrayList<>();
    Ingredient selectedIngredient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        txtId = findViewById(R.id.txtId);
        txtName = findViewById(R.id.txtName);
        txtOrigin = findViewById(R.id.txtOrigin);
        spinnerIngredients = findViewById(R.id.spinnerIngredients);
        txtIngQuantity = findViewById(R.id.txtIngQuantity);
        chipGroupIngredients = findViewById(R.id.chipGroupIngredients);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        loadMasterIngredients();
    }

    private void showLoading(boolean show) {
        runOnUiThread(() -> {
            loadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        });
    }

    private void loadMasterIngredients() {
        showLoading(true);
        new Thread(() -> {
            try {
                List<Ingredient> ingredients = ApiClient.getIngredients();
                runOnUiThread(() -> {
                    masterIngredients = ingredients;
                    setupIngredientSpinner();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar catálogo de ingredientes", Toast.LENGTH_SHORT).show());
            } finally {
                showLoading(false);
            }
        }).start();
    }

    private void setupIngredientSpinner() {
        List<String> ingredientNames = new ArrayList<>();
        for (Ingredient ing : masterIngredients) {
            ingredientNames.add(ing.ing_name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, ingredientNames);
        spinnerIngredients.setAdapter(adapter);

        spinnerIngredients.setOnItemClickListener((parent, view, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            for (Ingredient ing : masterIngredients) {
                if (ing.ing_name.equals(selectedName)) {
                    selectedIngredient = ing;
                    break;
                }
            }
        });
    }

    // --- Lógica del Detalle (Ingredientes) ---

    public void btnAddIngredient_onClick(View v) {
        try {
            String qtyStr = txtIngQuantity.getText().toString();
            
            if (selectedIngredient == null || qtyStr.isEmpty()) {
                Toast.makeText(this, "Seleccione un ingrediente y cantidad", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(qtyStr);

            // Crear objeto para la lista temporal
            Ingredient newIng = new Ingredient(selectedIngredient.ing_id, quantity);
            newIng.ing_name = selectedIngredient.ing_name;
            
            tempIngredients.add(newIng);
            updateIngredientDisplay();
            
            spinnerIngredients.setText("", false);
            txtIngQuantity.setText("");
            selectedIngredient = null;
        } catch (Exception e) {
            Toast.makeText(this, "Ingrese valores válidos", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnClearIngredients_onClick(View v) {
        tempIngredients.clear();
        updateIngredientDisplay();
    }

    private void updateIngredientDisplay() {
        chipGroupIngredients.removeAllViews();
        for (Ingredient ing : tempIngredients) {
            Chip chip = new Chip(this);
            String ingredientName = ing.ing_name != null ? ing.ing_name : findIngredientName(ing.ing_id);
            chip.setText(ingredientName + " (" + ing.ing_quantity + "g)");
            chip.setChipIconResource(android.R.drawable.ic_menu_add);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                tempIngredients.remove(ing);
                updateIngredientDisplay();
            });
            chipGroupIngredients.addView(chip);
        }
    }

    private String findIngredientName(int id) {
        if (masterIngredients != null) {
            for (Ingredient i : masterIngredients) {
                if (i.ing_id == id) return i.ing_name;
            }
        }
        return "ID: " + id;
    }

    // --- Operaciones CRUD ---

    public void cmdCrear_onClick(View v) {
        String name = txtName.getText().toString().trim();
        String origin = txtOrigin.getText().toString().trim();
        
        if (name.isEmpty() || tempIngredients.isEmpty()) {
            Toast.makeText(this, "Nombre e ingredientes son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Preparamos los ingredientes: Solo ID y Cantidad para la API
        List<Ingredient> sanitizedIngredients = new ArrayList<>();
        for (Ingredient temp : tempIngredients) {
            sanitizedIngredients.add(new Ingredient(temp.ing_id, temp.ing_quantity));
        }

        showLoading(true);
        new Thread(() -> {
            try {
                Pizza pizza = new Pizza();
                pizza.piz_name = name;
                pizza.piz_origin = origin;
                pizza.piz_state = true;
                pizza.ingredients = sanitizedIngredients;

                Pizza createdPizza = ApiClient.createPizza(pizza);
                runOnUiThread(() -> {
                    Toast.makeText(this, "¡Pizza \"" + name + "\" creada!", Toast.LENGTH_LONG).show();
                    clearFields();
                    if (createdPizza != null && createdPizza.piz_id != null) {
                        txtId.setText(String.valueOf(createdPizza.piz_id));
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : "Error de red";
                    Toast.makeText(this, "No se pudo crear: " + errorMsg, Toast.LENGTH_LONG).show();
                });
            } finally {
                showLoading(false);
            }
        }).start();
    }

    public void cmdLeer_onClick(View v) {
        String idStr = txtId.getText().toString();
        if (idStr.isEmpty()) return;
        
        int id = Integer.parseInt(idStr);
        showLoading(true);
        new Thread(() -> {
            try {
                Pizza pizza = ApiClient.getPizza(id);
                if (pizza != null) {
                    runOnUiThread(() -> {
                        txtName.setText(pizza.piz_name);
                        txtOrigin.setText(pizza.piz_origin);
                        tempIngredients.clear();
                        if (pizza.ingredients != null) {
                            for (Ingredient ing : pizza.ingredients) {
                                int qty = (ing.pizzaIngredient != null) ? ing.pizzaIngredient.ing_quantity : 0;
                                Ingredient displayIng = new Ingredient(ing.ing_id, qty);
                                displayIng.ing_name = ing.ing_name; // Preservar el nombre que viene de la API
                                tempIngredients.add(displayIng);
                            }
                        }
                        updateIngredientDisplay();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "La pizza con ID " + id + " no existe", Toast.LENGTH_SHORT).show();
                        clearFields();
                        txtId.setText(String.valueOf(id)); // Mantener el ID buscado para referencia
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al buscar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                showLoading(false);
            }
        }).start();
    }

    public void cmdActualizar_onClick(View v) {
        String idStr = txtId.getText().toString();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int id = Integer.parseInt(idStr);
        String name = txtName.getText().toString().trim();
        String origin = txtOrigin.getText().toString().trim();

        List<Ingredient> sanitizedIngredients = new ArrayList<>();
        for (Ingredient temp : tempIngredients) {
            sanitizedIngredients.add(new Ingredient(temp.ing_id, temp.ing_quantity));
        }

        showLoading(true);
        new Thread(() -> {
            try {
                Pizza pizza = new Pizza();
                pizza.piz_name = name;
                pizza.piz_origin = origin;
                pizza.piz_state = true;
                pizza.ingredients = sanitizedIngredients;
                ApiClient.updatePizza(id, pizza);
                runOnUiThread(() -> Toast.makeText(this, "Pizza actualizada correctamente", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : "Error de red";
                    Toast.makeText(this, "Error al actualizar: " + errorMsg, Toast.LENGTH_LONG).show();
                });
            } finally {
                showLoading(false);
            }
        }).start();
    }

    public void cmdEliminar_onClick(View v) {
        String idStr = txtId.getText().toString();
        if (idStr.isEmpty()) return;
        
        int id = Integer.parseInt(idStr);
        showLoading(true);
        new Thread(() -> {
            try {
                ApiClient.deletePizza(id);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Pizza eliminada", Toast.LENGTH_SHORT).show();
                    clearFields();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show());
            } finally {
                showLoading(false);
            }
        }).start();
    }

    private void clearFields() {
        txtName.setText("");
        txtOrigin.setText("");
        txtId.setText("");
        spinnerIngredients.setText("", false);
        txtIngQuantity.setText("");
        tempIngredients.clear();
        updateIngredientDisplay();
    }
}
