package com.example.manejodeinventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TablaHash {
    private ConcurrentHashMap<Integer, ListaEnlazada> tabla;
    private int tamano;

    public TablaHash(int tamano) {
        this.tamano = tamano;
        this.tabla = new ConcurrentHashMap<>(tamano);
        for (int i = 0; i < tamano; i++) {
            tabla.put(i, new ListaEnlazada());
        }
    }

    private int funcionHash(int id) {
        return id % tamano;
    }

    public void insertar(Producto producto) {
        int hashIndex = funcionHash(producto.getId());
        tabla.computeIfAbsent(hashIndex, k -> new ListaEnlazada())
             .agregarProducto(producto.getId(), producto);
    }

    public Optional<Producto> buscar(int id) {
        return Optional.ofNullable(tabla.get(funcionHash(id)))
                       .flatMap(lista -> lista.obtenerProducto(id));
    }

    public boolean eliminarProducto(int id) {
        return tabla.getOrDefault(funcionHash(id), new ListaEnlazada())
                    .eliminarNodo(id);
    }

    public boolean actualizarProducto(int id, int nuevaCantidad, double nuevoPrecio) {
        return buscar(id).map(producto -> {
            producto.setCantidad(nuevaCantidad);
            producto.setPrecio(nuevoPrecio);
            return true;
        }).orElse(false);
    }

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
