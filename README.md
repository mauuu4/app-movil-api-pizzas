# App Movil API Pizzas

Aplicacion Android para gestionar pizzas con estructura maestro-detalle.
La app consume una API REST publica propia para operaciones CRUD de pizzas y consulta de ingredientes.

## API usada

- Base URL: `https://api-pizzas-7v98.onrender.com/`
- Permiso en Android: `android.permission.INTERNET`

## Endpoints consumidos

- `GET /ingredients` -> carga el catalogo de ingredientes al iniciar.
- `GET /pizzas` -> obtiene listado de pizzas (metodo disponible en cliente).
- `GET /pizzas/{id}` -> trae una pizza por ID.
- `POST /pizzas` -> crea una pizza con sus ingredientes.
- `PUT /pizzas/{id}` -> actualiza una pizza existente.
- `DELETE /pizzas/{id}` -> elimina una pizza por ID.

## Funcionamiento

### 1) Crear pizza

- Boton en app: `Crear`
- Endpoint: `POST /pizzas`
- Que envia la app:

```json
{
  "piz_name": "Margarita",
  "piz_origin": "Italia",
  "piz_state": true,
  "ingredients": [
    { "ing_id": 1, "ing_quantity": 120 },
    { "ing_id": 2, "ing_quantity": 90 }
  ]
}
```

- Respuesta esperada: pizza creada con `piz_id`.
![Crear pizza](docs/capturas/crear-pizza.png)

### 2) Leer pizza por ID

- Boton en app: `Leer`
- Endpoint: `GET /pizzas/{id}`
- Que envia la app: ID desde el campo `txtId`.
- Respuesta esperada: datos de la pizza + detalle de ingredientes.
![Leer pizza](docs/capturas/leer-pizza.png)

### 3) Actualizar pizza

- Boton en app: `Actualizar`
- Endpoint: `PUT /pizzas/{id}`
- Que envia la app: ID + payload completo de pizza (nombre, origen, estado e ingredientes).

```json
{
  "piz_name": "Margarita Especial",
  "piz_origin": "Italia",
  "piz_state": true,
  "ingredients": [
    { "ing_id": 1, "ing_quantity": 130 },
    { "ing_id": 3, "ing_quantity": 60 }
  ]
}
```

- Respuesta esperada: pizza actualizada.
![Actualizar pizza](docs/capturas/actualizar-pizza.png)

### 4) Eliminar pizza

- Boton en app: `Eliminar`
- Endpoint: `DELETE /pizzas/{id}`
- Que envia la app: ID de la pizza a eliminar.
- Respuesta esperada: eliminacion correcta (sin contenido).
![Eliminar pizza](docs/capturas/eliminar-pizza.png)

### 5) Carga inicial de ingredientes

- Momento: al abrir la app.
- Endpoint: `GET /ingredients`
- Uso en UI: llena el selector de ingredientes para agregar al detalle.
![Catalogo de ingredientes](docs/capturas/catalogo-ingredientes.png)

## Evidencia actual

- En el repositorio ya existe `Informe-Funcionamiento.pdf` con evidencia de uso.

## Estructura clave del codigo

- `app/src/main/java/com/example/aplicacionutn2026_001/MainActivity.java`: eventos de botones y flujo CRUD.
- `app/src/main/java/com/example/aplicacionutn2026_001/ApiClient.java`: llamadas HTTP a endpoints.
- `app/src/main/java/com/example/aplicacionutn2026_001/Pizza.java`: modelo de pizza.
- `app/src/main/java/com/example/aplicacionutn2026_001/Ingredient.java`: modelo de ingrediente.
