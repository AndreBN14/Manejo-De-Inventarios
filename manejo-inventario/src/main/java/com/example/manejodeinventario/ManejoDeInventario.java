package com.example.manejodeinventario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.gson.Gson;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

public class ManejoDeInventario {
    public static void main(String[] args) {
        TablaHash tablaHash = new TablaHash(10);
        Arbol arbol = new Arbol(tablaHash);
        ActualizadorProductos actualizador = new ActualizadorProductos(arbol, tablaHash);
        BuscadorProductos buscador = new BuscadorProductos(arbol, tablaHash);
        Gson gson = new Gson();

        // Configuración de Spark
        port(4567);

        // Servir archivos estáticos (index.html, script.js, style.css)
        staticFiles.location("/public");

        // Endpoint para añadir un producto (POST /api/productos)
        post("/api/productos", (request, response) -> {
            Producto nuevoProducto = gson.fromJson(request.body(), Producto.class);
            arbol.insertar(nuevoProducto);
            tablaHash.insertar(nuevoProducto);
            response.status(201);
            return "Producto añadido exitosamente.";
        });

        // Endpoint para actualizar un producto (PUT /api/productos/:id)
        put("/api/productos/:id", (request, response) -> {
            int idActualizar = Integer.parseInt(request.params(":id"));
            Producto datosActualizados = gson.fromJson(request.body(), Producto.class);
            boolean actualizado = actualizador.actualizarProducto(idActualizar, datosActualizados.getCantidad(), datosActualizados.getPrecio());
            if (actualizado) {
                response.status(200);
                return "Producto actualizado exitosamente.";
            } else {
                response.status(404);
                return "Producto no encontrado.";
            }
        });

        get("/", (req, res) -> {
            // Ruta al archivo index.html
            File file = new File("src/main/resources/public/html/index.html");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString(); // Devolver el contenido del archivo
        });
        

        // Endpoint para buscar un producto por ID (GET /api/productos/:id)
        get("/api/productos/:id", (request, response) -> {
            int idBuscar = Integer.parseInt(request.params(":id"));
            Producto productoPorID = buscador.buscarPorID(idBuscar);
            if (productoPorID != null) {
                response.status(200);
                return gson.toJson(productoPorID);
            } else {
                response.status(404);
                return "Producto no encontrado.";
            }
        });

        // Endpoint para obtener todos los productos en orden (GET /api/productos)
        get("/api/productos", (request, response) -> {
            response.status(200);
            return gson.toJson(arbol.obtenerProductosEnOrden());
        });

        // Endpoint para eliminar un producto (DELETE /api/productos/:id)
        delete("/api/productos/:id", (request, response) -> {
            int idEliminar = Integer.parseInt(request.params(":id"));
            boolean eliminadoEnArbol = arbol.eliminarp(idEliminar);
            boolean eliminadoEnHash = tablaHash.eliminarProducto(idEliminar);
            
            if (eliminadoEnArbol && eliminadoEnHash) {
                response.status(200);
                return "Producto eliminado exitosamente.";
            } else {
                response.status(404);
                if (!eliminadoEnArbol) {
                    return "No se pudo eliminar el producto en el árbol.";
                } else if (!eliminadoEnHash) {
                    return "No se pudo eliminar el producto en la tabla hash.";
                }
                return "No se pudo eliminar el producto o tiene stock disponible.";
            }
        });
    }
}
