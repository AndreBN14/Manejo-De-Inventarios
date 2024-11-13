package com.example.manejodeinventario;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class ManejoDeInventario {
    private static final Gson gson = new Gson();
    private static TablaHash tablaHash = new TablaHash(10);
    private static GestorProductos gestorProductos = new GestorProductos(tablaHash);

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");

        get("/", (request, response) -> {
            response.redirect("/html/index.html");
            return null;
        });

        get("/api/productos", (request, response) -> {
            response.type("application/json");
            return gson.toJson(tablaHash.obtenerTodosLosProductos());  // Método que obtendrá todos los productos en `TablaHash`
        });
        

        post("/api/productos", ManejoDeInventario::añadirProducto);
        put("/api/productos/:id", ManejoDeInventario::actualizarProducto);
        get("/api/productos/:id", ManejoDeInventario::buscarProductoPorId);
        delete("/api/productos/:id", ManejoDeInventario::eliminarProducto);
    }

    private static String añadirProducto(Request request, Response response) {
        try {
            Producto nuevoProducto = gson.fromJson(request.body(), Producto.class);
            tablaHash.insertar(nuevoProducto);
            response.status(201);
            return "Producto añadido exitosamente.";
        } catch (Exception e) {
            response.status(400);
            return "Error en el formato del producto.";
        }
    }

    private static String actualizarProducto(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Producto datosActualizados = gson.fromJson(request.body(), Producto.class);
            boolean actualizado = gestorProductos.actualizarProducto(id, datosActualizados.getCantidad(), datosActualizados.getPrecio());
            if (actualizado) {
                response.status(200);
                return "Producto actualizado exitosamente.";
            } else {
                response.status(404);
                return "Producto no encontrado.";
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return "ID inválido.";
        } catch (Exception e) {
            response.status(500);
            return "Error al actualizar el producto.";
        }
    }

    private static String buscarProductoPorId(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Producto producto = gestorProductos.buscarPorID(id);
            if (producto != null) {
                response.status(200);
                return gson.toJson(producto);
            } else {
                response.status(404);
                return "Producto no encontrado.";
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return "ID inválido.";
        }
    }

    private static String eliminarProducto(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            boolean eliminado = tablaHash.eliminarProducto(id);
            if (eliminado) {
                response.status(200);
                return "Producto eliminado exitosamente.";
            } else {
                response.status(404);
                return "Producto no encontrado.";
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return "ID inválido.";
        }
    }
}
