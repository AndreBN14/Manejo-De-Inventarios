package com.example.manejodeinventario;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import spark.Request;
import spark.Response;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;
/**
 * Clase principal que maneja el servidor web y las operaciones del inventario.
 * Implementa una API RESTful utilizando Spark Framework para gestionar productos
 * a través de endpoints HTTP.
 */
public class ManejoDeInventario  {
    /** Instancia de Gson para la serialización/deserialización JSON */
    private static final Gson gson = new Gson();

    /** Tabla hash para almacenar y gestionar los productos */
    private static TablaHash tablaHash;

    /** Gestor de productos que encapsula la lógica de negocio */
    private static GestorProductos gestorProductos;

    /**
     * Método principal que inicia el servidor web y configura las rutas API.
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) throws IOException {       

        port(4567);
        staticFiles.location("/public");

        // Configuración de rutas API
        get("/", (request, response) -> {            
            response.redirect("/html/index.html");
            return null;
        });

        get("/api/cargar", (request, response) -> {
            cargarArchivo();
            return "Archivo cargado";
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

    private static void actualizarArchivo() throws IOException {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("tablaObj.txt"))) {
            salida.writeObject(tablaHash);
            salida.flush();
            salida.close();
        }
    }

    private static void cargarArchivo() {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream("tablaObj.txt"))) {
            tablaHash = (TablaHash) entrada.readObject();
            gestorProductos = new GestorProductos(tablaHash);
            System.out.println("Archivo cargado exitosamente");
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, creando uno nuevo...");
            tablaHash = new TablaHash(10);
            gestorProductos = new GestorProductos(tablaHash);
            try {
                actualizarArchivo();
            } catch (IOException ioException) {
                System.out.println("Error al crear el archivo inicial");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el archivo");
        }
    }
    
    /**
     * Maneja la solicitud POST para añadir un nuevo producto.
     * @param request solicitud HTTP que contiene los datos del producto en formato JSON
     * @param response respuesta HTTP
     * @return mensaje indicando el resultado de la operación
     */
    
     private static String añadirProducto(Request request, Response response) {
        try {
            JsonObject json = gson.fromJson(request.body(), JsonObject.class);
            String n = json.get("nombre").getAsString();
            int c = json.get("cantidad").getAsInt();
            double p = json.get("precio").getAsDouble();
            if (gestorProductos.buscarPorNombre(n) != null) {
                response.status(200);
                return "Producto ya existe.";
            }
            Producto nuevoProducto = new Producto(n,c,p);
            tablaHash.insertar(nuevoProducto);
            actualizarArchivo();
            response.status(201);
            return gson.toJson(nuevoProducto);
        } catch (JsonSyntaxException | IOException e) {
            response.status(400);
            return "Error en el formato del producto.";
        }
    }

    /**
     * Maneja la solicitud PUT para actualizar un producto existente.
     * @param request solicitud HTTP con el ID del producto y los nuevos datos
     * @param response respuesta HTTP
     * @return mensaje indicando el resultado de la actualización
     */
    private static String actualizarProducto(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Producto datosActualizados = gson.fromJson(request.body(), Producto.class);
            boolean actualizado = gestorProductos.actualizarProducto(id, datosActualizados.getCantidad(), datosActualizados.getPrecio());
            if (actualizado) {
                actualizarArchivo();
                response.status(200);
                return "Producto actualizado exitosamente.";
            } else {
                response.status(404);
                return "Producto no encontrado.";
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return "ID inválido.";
        } catch (JsonSyntaxException | IOException e) {
            response.status(500);
            return "Error al actualizar el producto.";
        }
    }

    /**
     * Maneja la solicitud GET para buscar un producto por su ID.
     * @param request solicitud HTTP con el ID del producto
     * @param response respuesta HTTP
     * @return JSON del producto encontrado o mensaje de error
     */
    private static String buscarProductoPorId(Request request, Response response) throws IOException {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Producto producto = gestorProductos.buscarPorID(id);
            if (producto != null) {
                actualizarArchivo();
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

    /**
     * Maneja la solicitud DELETE para eliminar un producto.
     * @param request solicitud HTTP con el ID del producto a eliminar
     * @param response respuesta HTTP
     * @return mensaje indicando el resultado de la eliminación
     */
    private static String eliminarProducto(Request request, Response response) throws IOException {
        try {
            int id = Integer.parseInt(request.params(":id"));
            boolean eliminado = tablaHash.eliminarProducto(id);
            if (eliminado) {
                actualizarArchivo();
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
