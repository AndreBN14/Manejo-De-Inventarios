package com.example.manejodeinventario;

/**
 * Clase que gestiona las operaciones relacionadas con los productos en el inventario.
 * Esta clase actúa como una capa de abstracción entre la interfaz de usuario y la estructura de datos TablaHash que almacena los productos.
 */
public class GestorProductos {
    //Estructura de datos que almacena los productos
    private final TablaHash tablaHash;
    
    //Constructor de la clase con parametro tablaHash
    public GestorProductos(TablaHash tablaHash) {
        this.tablaHash = tablaHash;
    }

    /**
     * Actualiza la cantidad y precio de un producto existente.
     * @return true si la actualización fue exitosa, false si el producto no existe
     */
    public boolean actualizarProducto(int id, int nuevaCantidad, double nuevoPrecio) {
        return tablaHash.actualizarProducto(id, nuevaCantidad, nuevoPrecio);
    }

    /**
     * Busca un producto por su ID.
     * @return El producto encontrado o null si no existe
     */
    public Producto buscarPorID(int id) {
        return tablaHash.buscar(id).orElse(null);
    }

    /**
     * Busca un producto por su nombre.
     * @return El producto encontrado o null si no existe
     */
    public Producto buscarPorNombre(String nombre) {
        return tablaHash.buscarPorNombre(nombre).orElse(null);
    }
}
