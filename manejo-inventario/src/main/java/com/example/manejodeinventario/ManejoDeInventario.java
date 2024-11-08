package com.example.manejodeinventario;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
import static spark.Spark.*;

public class ManejoDeInventario {
    public static void main(String[] args) {
        Arbol arbol = new Arbol();
        TablaHash tablaHash = new TablaHash(10);
        ActualizadorProductos actualizador = new ActualizadorProductos(arbol, tablaHash);
        BuscadorProductos buscador = new BuscadorProductos(arbol, tablaHash);
        Gson gson = new Gson();

        // Configuración de Spark
        port(4567);

        // Servir archivos estáticos (index.html, script.js, style.css)
        staticFiles.location("/public");

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
                return "No se pudo eliminar el producto o tiene stock disponible.";
            }
        });

        // Interfaz de consola para el manejo del inventario
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("---- Sistema de Manejo de Inventario ----");
            System.out.println("1. Insertar Producto");
            System.out.println("2. Actualizar Producto");
            System.out.println("3. Buscar Producto por ID");
            System.out.println("4. Buscar Producto por Nombre");
            System.out.println("5. Eliminar Producto");
            System.out.println("6. Mostrar Inventario");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Limpiar el buffer de entrada

            switch (opcion) {
                case 1:
                    // Código para insertar producto
                    break;
                case 2:
                    // Código para actualizar producto
                    break;
                case 3:
                    // Código para buscar por ID
                    break;
                case 4:
                    // Código para buscar por nombre
                    break;
                case 5:
                    // Código para eliminar producto
                    break;
                case 6:
                    // Mostrar inventario en orden
                    List<Producto> productos = arbol.obtenerProductosEnOrden();
                    if (productos.isEmpty()) {
                        System.out.println("El inventario está vacío.");
                    } else {
                        System.out.println("---- Inventario en Orden ----");
                        for (Producto producto : productos) {
                            System.out.println(producto);
                        }
                    }
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida, por favor intente nuevamente.");
            }
            if(opcion == 0){
                break;
            }
        }
    }
}
