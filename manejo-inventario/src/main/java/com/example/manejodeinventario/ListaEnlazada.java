package com.example.manejodeinventario;

// Para la tabla hash para el manejo de colisiones
public class ListaEnlazada {
    private NodoProducto lista = null;
    
    public void agregarProducto(int id, Producto producto){
        NodoProducto actual = lista;

        // Buscar si ya existe en la lista para actualizarlo
        while(actual != null){
            if(actual.id == id){
                actual.producto = producto;
                return;
            }
            actual = actual.siguiente;
        }

        // Si no existe, agregarlo al inicio de la lista
        NodoProducto np = new NodoProducto(id, producto);
        np.siguiente = lista;
        lista = np;
    }
    
    public Producto obtenerProducto(int id){
        NodoProducto actual = lista;

        // Buscar producto por ID
        while(actual != null){
            if(actual.id == id){
                return actual.producto;
            }
            actual = actual.siguiente;
        }
        return null; // Producto no encontrado
    }
    
    public void mostrarLista(){
        NodoProducto actual = lista;
        System.out.println("La lista es: ");
        
        // Mostrar todos los productos en la lista
        while(actual != null){
            System.out.println("[" + actual.producto + "]");
            actual = actual.siguiente;
        }
        System.out.println("null");
    }
    
    public boolean eliminarNodo(int id) {
        if (lista == null) return false;

        // Si el nodo a eliminar es el primero
        if (lista.id == id) {
            lista = lista.siguiente;
            return true;
        }

        // Buscar nodo a eliminar
        NodoProducto actual = lista;
        while (actual.siguiente != null && actual.siguiente.id != id) {
            actual = actual.siguiente;
        }

        // Si no se encontró el nodo
        if (actual.siguiente == null) return false;

        // Eliminar nodo
        actual.siguiente = actual.siguiente.siguiente;
        return true;
    }
    
    public Producto obtenerProductoPorNombre(String nombre) {
        NodoProducto actual = lista;
        
        // Buscar producto por nombre
        while (actual != null) {
            if (actual.producto.getNombre().equalsIgnoreCase(nombre)) {
                return actual.producto;
            }
            actual = actual.siguiente;
        }
        return null; // Producto no encontrado
    }

    // Clase NodoProducto interna
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
}
