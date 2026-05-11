<img width="720" height="1600" alt="actualizar1" src="https://github.com/user-attachments/assets/7ca970ed-e81c-4514-b849-1917c590186f" /># App Movil API Pizzas

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
<img width="720" height="1600" alt="crear1" src="https://github.com/user-attachments/assets/70a35de6-48cd-4eda-a006-c7eef2910df7" />
<img width="720" height="1600" alt="crear2" src="https://github.com/user-attachments/assets/4b1a858f-9aff-4c03-950b-3e9d1f57474b" />
<img width="720" height="1600" alt="crear3" src="https://github.com/user-attachments/assets/7b652607-9351-49aa-af67-9d4fac3720a2" />

### 2) Leer pizza por ID

- Boton en app: `Leer`
- Endpoint: `GET /pizzas/{id}`
- Que envia la app: ID desde el campo `txtId`.
- Respuesta esperada: datos de la pizza + detalle de ingredientes.
<img width="720" height="1600" alt="leer" src="https://github.com/user-attachments/assets/694c5dd0-a17f-4533-a33e-22748bac66ad" />


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
<img width="720" height="1600" alt="actualizar1" src="https://github.com/user-attachments/assets/da42a34d-ad43-4db4-a93c-7b7fe4ff899d" />
<img width="720" height="1600" alt="actualizar2" src="https://github.com/user-attachments/assets/83220c03-11ef-4465-8c1a-9f94f0cfba77" />



### 4) Eliminar pizza

- Boton en app: `Eliminar`
- Endpoint: `DELETE /pizzas/{id}`
- Que envia la app: ID de la pizza a eliminar.
- Respuesta esperada: eliminacion correcta (sin contenido).
<img width="720" height="1600" alt="eliminar" src="https://github.com/user-attachments/assets/37f2afb4-a5ce-48c1-8d2a-1ce6ff631dcf" />


### 5) Carga inicial de ingredientes

- Momento: al abrir la app.
- Endpoint: `GET /ingredients`
- Uso en UI: llena el selector de ingredientes para agregar al detalle.
- <img width="720" height="1600" alt="carga de datos" src="https://github.com/user-attachments/assets/719e0f29-9982-443f-8a6c-101545f9ace9" />


## Evidencia actual

- En el repositorio ya existe `Informe-Funcionamiento.pdf` con evidencia de uso.

## Estructura clave del codigo

- `app/src/main/java/com/example/aplicacionutn2026_001/MainActivity.java`: eventos de botones y flujo CRUD.
- `app/src/main/java/com/example/aplicacionutn2026_001/ApiClient.java`: llamadas HTTP a endpoints.
- `app/src/main/java/com/example/aplicacionutn2026_001/Pizza.java`: modelo de pizza.
- `app/src/main/java/com/example/aplicacionutn2026_001/Ingredient.java`: modelo de ingrediente.
