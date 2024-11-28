package com.example.manejodeinventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de una lista enlazada simple para almacenar productos.
 * Esta estructura de datos permite almacenar y gestionar productos de manera dinámica,
 * permitiendo operaciones de inserción, búsqueda y eliminación.
 */
public class ListaEnlazada {
    /** Referencia al primer nodo de la lista enlazada */
    private NodoProducto lista = null;

    /**
     * Agrega o actualiza un producto en la lista.
     * Si el ID ya existe, actualiza el producto existente.
     * Si el ID no existe, agrega el producto al inicio de la lista.
     * @param id ID único del producto
     * @param producto Producto a agregar o actualizar
     */
    public void agregarProducto(int id, Producto producto) {
        NodoProducto actual = lista;
        while (actual != null) {
            if (actual.id == id) { // Actualizar si ya existe
                actual.producto = producto;
                return;
            }
            actual = actual.siguiente;
        }
        // Agregar nuevo producto al inicio de la lista
        NodoProducto nuevoNodo = new NodoProducto(id, producto);
        nuevoNodo.siguiente = lista;
        lista = nuevoNodo;
    }

    /**
     * Busca un producto por su ID.
     * @param id ID del producto a buscar
     * @return Optional con el producto si existe, Optional.empty() si no se encuentra
     */
    public Optional<Producto> obtenerProducto(int id) {
        NodoProducto actual = lista;
        while (actual != null) {
            if (actual.id == id) {
                return Optional.of(actual.producto);
            }
            actual = actual.siguiente;
        }
        return Optional.empty();
    }

    /**
     * Elimina un producto de la lista por su ID.
     * @param id ID del producto a eliminar
     * @return true si el producto fue eliminado, false si no se encontró
     */
    public boolean eliminarNodo(int id) {
        if (lista == null) return false;
        if (lista.id == id) { // Si es el primer nodo
            lista = lista.siguiente;
            return true;
        }
        NodoProducto actual = lista;
        while (actual.siguiente != null && actual.siguiente.id != id) {
            actual = actual.siguiente;
        }
        if (actual.siguiente == null) return false;
        actual.siguiente = actual.siguiente.siguiente; // Saltar el nodo
        return true;
    }

    /**
     * Busca un producto por su nombre (búsqueda case-insensitive).
     * @param nombre Nombre del producto a buscar
     * @return Optional con el primer producto que coincida con el nombre, Optional.empty() si no se encuentra
     */
    public Optional<Producto> obtenerProductoPorNombre(String nombre) {
        NodoProducto actual = lista;
        while (actual != null) {
            if (actual.producto.getNombre().equalsIgnoreCase(nombre)) {
                return Optional.of(actual.producto);
            }
            actual = actual.siguiente;
        }
        return Optional.empty();
    }

    /**
     * Clase interna que representa un nodo en la lista enlazada.
     * Cada nodo contiene un ID, un producto y una referencia al siguiente nodo.
     */
    private class NodoProducto {
        int id;
        Producto producto;
        NodoProducto siguiente;

        public NodoProducto(int id, Producto producto) {
            this.id = id;
            this.producto = producto;
            this.siguiente = null;
        }
    }

    /**
     * Convierte la lista enlazada a una lista de Java.
     * @return Lista que contiene todos los productos almacenados
     */
    public List<Producto> getLista() {
        List<Producto> productos = new ArrayList<>();
        NodoProducto actual = lista;
        while (actual != null) {
            productos.add(actual.producto);
            actual = actual.siguiente;
        }
        return productos;
    }
}
