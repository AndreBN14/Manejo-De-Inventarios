package com.example.manejodeinventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación de una tabla hash para el almacenamiento eficiente de productos.
 * Utiliza encadenamiento con listas enlazadas para manejar colisiones y
 * ConcurrentHashMap para garantizar la seguridad en entornos concurrentes.
 */
public class TablaHash {
    /** Estructura principal de la tabla hash que mapea índices a listas enlazadas */
    private ConcurrentHashMap<Integer, ListaEnlazada> tabla;

    /** Tamaño de la tabla hash (número de cubetas/buckets[posiciones o espacios de almacenamiento donde se guardan los elementos]) */
    private int tamano;

    public TablaHash(int tamano) {
        this.tamano = tamano;
        this.tabla = new ConcurrentHashMap<>(tamano);
        for (int i = 0; i < tamano; i++) {
            tabla.put(i, new ListaEnlazada());
        }
    }

    /**
     * Calcula el índice hash para un ID dado.
     * @param id ID del producto
     * @return índice en la tabla hash
     */
    /*private int funcionHash(int id) {
        return id % tamano;
    }*/

    /**
     * Inserta un nuevo producto en la tabla hash.
     * Si ya existe un producto con el mismo ID, se actualiza.
     * @param producto Producto a insertar o actualizar
     */
    public void insertar(Producto producto) {
        int hashIndex = producto.getId();
        tabla.computeIfAbsent(hashIndex, k -> new ListaEnlazada())
             .agregarProducto(producto.getId(), producto);
    }

    /**
     * Busca un producto por su ID.
     * @param id ID del producto a buscar
     * @return Optional conteniendo el producto si existe, Optional.empty() si no
     */
    public Optional<Producto> buscar(int id) {
        return Optional.ofNullable(tabla.get(id))
                       .flatMap(lista -> lista.obtenerProducto(id));
    }

    /**
     * Elimina un producto de la tabla hash.
     * @param id ID del producto a eliminar
     * @return true si el producto fue eliminado, false si no existía
     */
    public boolean eliminarProducto(int id) {
        return tabla.getOrDefault(id, new ListaEnlazada())
                    .eliminarNodo(id);
    }

    /**
     * Actualiza la cantidad y precio de un producto existente.
     * @param id ID del producto a actualizar
     * @param nuevaCantidad Nueva cantidad en inventario
     * @param nuevoPrecio Nuevo precio unitario
     * @return true si el producto fue actualizado, false si no existía
     */
    public boolean actualizarProducto(int id, int nuevaCantidad, double nuevoPrecio) {
        return buscar(id).map(producto -> {
            producto.setCantidad(nuevaCantidad);
            producto.setPrecio(nuevoPrecio);
            return true;
        }).orElse(false);
    }

    /**
     * Busca un producto por su nombre.
     * @param nombre Nombre del producto a buscar
     * @return Optional conteniendo el primer producto que coincida con el nombre,
     *         Optional.empty() si no se encuentra ninguno
     */
    public Optional<Producto> buscarPorNombre(String nombre) {
        return tabla.values().stream()
                    .map(lista -> lista.obtenerProductoPorNombre(nombre))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
    }

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        // Iteramos sobre cada lista enlazada en la tabla hash
        for (ListaEnlazada lista : tabla.values()) {
            // Agregamos todos los productos de esa lista a la lista final
            productos.addAll(lista.getLista());
        }
        return productos;
    }
}
