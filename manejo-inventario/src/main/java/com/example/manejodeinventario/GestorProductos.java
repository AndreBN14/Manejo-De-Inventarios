package com.example.manejodeinventario;

public class GestorProductos {
    private TablaHash tablaHash;

    public GestorProductos(TablaHash tablaHash) {
        this.tablaHash = tablaHash;
    }

    public boolean actualizarProducto(int id, int nuevaCantidad, double nuevoPrecio) {
        return tablaHash.actualizarProducto(id, nuevaCantidad, nuevoPrecio);
    }

    public Producto buscarPorID(int id) {
        return tablaHash.buscar(id).orElse(null);
    }

    public Producto buscarPorNombre(String nombre) {
        return tablaHash.buscarPorNombre(nombre).orElse(null);
    }
}
