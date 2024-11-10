package com.example.manejodeinventario;

public class Nodo {
    Producto producto;  // Aquí guardamos el producto
    Nodo izquierdo, derecho;

    public Nodo(Producto producto) {
        this.producto = producto;  // Asignamos el producto al nodo
        izquierdo = null;
        derecho = null;
    }
}

