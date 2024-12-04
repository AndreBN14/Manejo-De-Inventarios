package com.example.manejodeinventario;

import java.io.Serializable;

/**
 * Representa un nodo en una lista enlazada de productos.
 * Esta clase se utiliza como elemento básico para implementar la estructura
 * de datos de la tabla hash que almacena los productos del inventario.
 */
public class NodoProducto implements Serializable{
    int id;
    Producto producto;
    NodoProducto siguiente;
    
    public NodoProducto(int id, Producto producto){
        this.id = id;
        this.producto = producto;
        this.siguiente = null;
    }
}
