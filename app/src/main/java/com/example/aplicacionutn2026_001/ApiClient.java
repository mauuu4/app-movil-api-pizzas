package com.example.aplicacionutn2026_001;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static final String BASE_URL = "https://api-pizzas-7v98.onrender.com/";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private static final Gson gson = new Gson();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // GET /pizzas — obtiene todas las pizzas
    public static List<Pizza> getPizzas() throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + "pizzas")
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body() != null ? response.body().string() : "[]";
            return gson.fromJson(json, new TypeToken<List<Pizza>>(){}.getType());
        }
    }

    // GET /pizzas/{id} — obtiene una pizza por ID
    public static Pizza getPizza(int id) throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + "pizzas/" + id)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return null;
            String json = response.body() != null ? response.body().string() : "{}";
            return gson.fromJson(json, Pizza.class);
        }
    }

    // POST /pizzas — crea una nueva pizza (Maestro-Detalle)
    public static Pizza createPizza(Pizza pizza) throws Exception {
        String jsonBody = gson.toJson(pizza);
        Request request = new Request.Builder()
                .url(BASE_URL + "pizzas")
                .post(RequestBody.create(jsonBody, JSON))
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body() != null ? response.body().string() : "{}";
            if (!response.isSuccessful()) {
                throw new Exception("Error " + response.code() + ": " + json);
            }
            return gson.fromJson(json, Pizza.class);
        }
    }

    // PUT /pizzas/{id} — actualiza una pizza
    public static Pizza updatePizza(int id, Pizza pizza) throws Exception {
        String jsonBody = gson.toJson(pizza);
        Request request = new Request.Builder()
                .url(BASE_URL + "pizzas/" + id)
                .put(RequestBody.create(jsonBody, JSON))
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body() != null ? response.body().string() : "{}";
            if (!response.isSuccessful()) {
                throw new Exception("Error " + response.code() + ": " + json);
            }
            return gson.fromJson(json, Pizza.class);
        }
    }

    // DELETE /pizzas/{id} — elimina una pizza
    public static void deletePizza(int id) throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + "pizzas/" + id)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            // No content expected
        }
    }

    // GET /ingredients - Lista maestra de ingredientes
    public static List<Ingredient> getIngredients() throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + "ingredients")
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body() != null ? response.body().string() : "[]";
            return gson.fromJson(json, new TypeToken<List<Ingredient>>(){}.getType());
        }
    }
}
