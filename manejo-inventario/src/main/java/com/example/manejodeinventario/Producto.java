package com.example.manejodeinventario;

/**
 * Representa un producto en el sistema de inventario.
 * Esta clase almacena toda la información relevante de un producto,
 * incluyendo su identificador único, nombre, cantidad en stock y precio.
 */
public class Producto {
    private int id;
    private String nombre;
    private int cantidad;
    private double precio;

    /*public Producto(int id, String nombre, int cantidad, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }*/

    public Producto(String nombre, int cantidad, double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.id = this.hash(nombre);
    }

    private int hash(String n){
        int h = 0;
        for (int i = 0; i < n.length(); i++){
            int hex = n.codePointAt(i);
            h = (333667*h + hex) % 997;
        }
        System.out.println(h);
        return h;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
     @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + ", Cantidad: " + cantidad + ", Precio: " + precio;
    }
}
