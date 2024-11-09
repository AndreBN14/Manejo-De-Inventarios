package com.example.manejodeinventario;

//Para la lista simple de la tabla hash
public class NodoProducto {
    int id;
    Producto producto;
    NodoProducto siguiente;
    
    public NodoProducto(int id, Producto producto){
        this.id = id;
        this.producto = producto;
        this.siguiente = null;
    }
}
