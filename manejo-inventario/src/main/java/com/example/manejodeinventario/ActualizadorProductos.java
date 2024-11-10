package com.example.manejodeinventario;

public class ActualizadorProductos {
    private Arbol arbol;
    private TablaHash tablaHash;

    public ActualizadorProductos(Arbol arbol, TablaHash tablaHash) {
        this.arbol = arbol;
        this.tablaHash = tablaHash;
    }

    public boolean actualizarProducto(int id, int nuevaCantidad, double nuevoPrecio) {
        boolean actualizadoEnArbol = arbol.actualizarProducto(id, nuevaCantidad, nuevoPrecio);
        boolean actualizadoEnHash = tablaHash.actualizarProducto(id, nuevaCantidad, nuevoPrecio);
    
        // Si ambos fueron actualizados, retornamos verdadero, sino revertimos
        if (actualizadoEnArbol && actualizadoEnHash) {
            return true;
        } else {
            // Si alguno falló, podrías intentar revertir la operación
            if (actualizadoEnArbol) {
                tablaHash.eliminarProducto(id);  // Asegúrate de que eliminas el producto de la tabla hash si el árbol se actualizó
            }
            if (actualizadoEnHash) {
                arbol.eliminarp(id);  // Asegúrate de eliminar del árbol si la tabla hash se actualizó
            }
            return false;
        }
    }
    
}
