package com.example.manejodeinventario;

public class TablaHash {
    private ListaEnlazada[] tabla;
    private int tamano;
    
    public TablaHash(int tamano){
        this.tamano = tamano;
        tabla = new ListaEnlazada[tamano];
        
        for(int i=0; i<tamano; i++){
            tabla[i] = new ListaEnlazada();
        }
    }
    
    public int funcionHash(int id){
        return id % tamano;
    }
    
    public void insertar(Producto producto){
        int indice = funcionHash(producto.getId());
        tabla[indice].agregarProducto(producto.getId(), producto);
    }
    
    public Producto buscar(int id){
        int indice = funcionHash(id);
        return tabla[indice].obtenerProducto(id);
    }
    
    public void mostrarTabla(){
        for(short i=0; i<tamano;i++){
            System.out.println("Indice--> " + i + ":");
            tabla[i].mostrarLista();
        }
    }

    public boolean eliminarProducto(int id) {
        int indice = funcionHash(id);
        Producto producto = tabla[indice].obtenerProducto(id);

        if (producto == null) {
            System.out.println("Producto con ID " + id + " no encontrado.");
            return false;
        }
        
        // Llamar al método eliminar de la lista enlazada
        return tabla[indice].eliminarNodo(id);
    }
    
    // Método para actualizar un producto existente en la tabla hash
    public boolean actualizarProducto(int id, int nuevaCantidad, double nuevoPrecio) {
        int indice = funcionHash(id);
        Producto producto = tabla[indice].obtenerProducto(id);

        if (producto != null) {
            producto.setCantidad(nuevaCantidad);
            producto.setPrecio(nuevoPrecio);
            return true;
        } else {
            System.out.println("Producto con ID " + id + " no encontrado.");
            return false;
        }
    }

    // Método para buscar un producto por su nombre en toda la tabla hash
    public Producto buscarPorNombre(String nombre) {
        for (ListaEnlazada lista : tabla) {
            Producto producto = lista.obtenerProductoPorNombre(nombre);
            if (producto != null) {
                return producto;
            }
        }
        System.out.println("Producto con nombre " + nombre + " no encontrado.");
        return null;
    }
}
