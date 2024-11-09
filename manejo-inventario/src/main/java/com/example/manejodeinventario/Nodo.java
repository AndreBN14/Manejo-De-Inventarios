package com.example.manejodeinventario;

public class Nodo {
    Producto producto;
    Nodo izquierdo, derecho;

    public Nodo(Producto producto) {
        this.producto = producto;
        izquierdo = null;
        derecho = null;
    }
}
