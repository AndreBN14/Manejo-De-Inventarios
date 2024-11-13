package com.example.manejodeinventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListaEnlazada {
    private NodoProducto lista = null;

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
