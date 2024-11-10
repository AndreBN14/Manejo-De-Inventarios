package com.example.manejodeinventario;
import java.util.ArrayList;
import java.util.List;
public class Arbol {
    private Nodo raiz;
    private TablaHash tablaHash;

    public Arbol(TablaHash tablaHash) {
        raiz = null;
        this.tablaHash = tablaHash;
    }
    
    public boolean actualizarProducto(int id, int nuevaCantidad, double nuevoPrecio){
        Nodo nodo = buscarNodoPorID(raiz, id);
        if (nodo != null) {
            nodo.producto.setCantidad(nuevaCantidad);
            nodo.producto.setPrecio(nuevoPrecio);
            return true;
        }
        return false;
    }
    
    public Producto buscarPorID(int id) {
        Nodo nodo = buscarNodoPorID(raiz, id);
        return nodo != null ? nodo.producto : null;
    }

    public Producto buscarPorNombre(String nombre) {
        return buscarPorNombreRecursivo(raiz, nombre);
    }

    private Producto buscarPorNombreRecursivo(Nodo nodo, String nombre) {
        if (nodo == null) return null;
        if (nodo.producto.getNombre().equals(nombre)) return nodo.producto;
        Producto productoIzq = buscarPorNombreRecursivo(nodo.izquierdo, nombre);
        if (productoIzq != null) return productoIzq;
        return buscarPorNombreRecursivo(nodo.derecho, nombre);
    }
    
    private Nodo buscarNodoPorID(Nodo nodo, int id) {
        if (nodo == null) return null;
        if (nodo.producto.getId() == id) return nodo;
        if (id < nodo.producto.getId()) return buscarNodoPorID(nodo.izquierdo, id);
        else return buscarNodoPorID(nodo.derecho, id);
    }
    
    public void insertar(Producto producto) {
        raiz = insertarRecursivo(raiz, producto);
    }
    
    private Nodo insertarRecursivo(Nodo nodo, Producto producto) {
        if (nodo == null) {
            return new Nodo(producto);
        }

        // Primero comparamos por la cantidad
        if (producto.getCantidad() < nodo.producto.getCantidad()) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, producto);
        } else if (producto.getCantidad() > nodo.producto.getCantidad()) {
            nodo.derecho = insertarRecursivo(nodo.derecho, producto);
        } else {
            //Comparamos por ID (clave secundaria)
            if (producto.getId() < nodo.producto.getId()) {
                nodo.izquierdo = insertarRecursivo(nodo.izquierdo, producto);
            } else if (producto.getId() > nodo.producto.getId()) {
                nodo.derecho = insertarRecursivo(nodo.derecho, producto);
            }
        }
        return nodo;
    }

     // Método para obtener productos en orden
     public List<Producto> obtenerProductosEnOrden() {
        List<Producto> productos = new ArrayList<>();
        obtenerProductosEnOrdenRecursivo(raiz, productos);
        return productos;
    }

    // Método recursivo para recorrer el árbol en orden y agregar productos a la lista
    private void obtenerProductosEnOrdenRecursivo(Nodo nodo, List<Producto> productos) {
        if (nodo != null) {
            obtenerProductosEnOrdenRecursivo(nodo.izquierdo, productos);  // Recorrer izquierdo
            productos.add(nodo.producto);  // Agregar el producto
            obtenerProductosEnOrdenRecursivo(nodo.derecho, productos);  // Recorrer derecho
        }
    }
    
    public void mostrarInOrden() {
        mostrarInOrdenRecursivo(raiz);
    }
     
    private void mostrarInOrdenRecursivo(Nodo nodo) {
        if (nodo != null) {
            mostrarInOrdenRecursivo(nodo.izquierdo);
            System.out.println(nodo.producto);
            mostrarInOrdenRecursivo(nodo.derecho);
        }
    }

    // Método para eliminar un producto
    public boolean eliminarp(int id) {
        if (raiz == null) {
            System.out.println("El árbol está vacío.");
            return false;
        }
        Nodo resultado = eliminarr(raiz, id);
        if (resultado != null) {
            // Eliminar en la tabla hash también si se encontró en el árbol
            if (tablaHash.eliminarProducto(id)) {
                System.out.println("Producto eliminado.");
                return true;
            } else {
                System.out.println("No se pudo eliminar el producto de la tabla hash.");
                return false;
            }
        } else {
            System.out.println("No se encontró el producto con ID: " + id);
            return false;
        }
    }

    //Eliminación considerando los tres casos mencionados
    private Nodo eliminarr(Nodo nodo, int id) {
        
        if (nodo == null) {
            return null;  // Si el nodo es nulo, no hay nada que eliminar
        }
    
        if (id < nodo.producto.getId()) {  // Accedemos al id del producto
            // Si el ID es menor, buscamos en el subárbol izquierdo
            nodo.izquierdo = eliminarr(nodo.izquierdo, id);
        } else if (id > nodo.producto.getId()) {  // Accedemos al id del producto
            // Si el ID es mayor, buscamos en el subárbol derecho
            nodo.derecho = eliminarr(nodo.derecho, id);
        } else {
            // Si encontramos el nodo con el ID del producto
            if (nodo.izquierdo == null && nodo.derecho == null) {
                // Si no tiene hijos, simplemente lo eliminamos
                return null;
            } else if (nodo.izquierdo == null) {
                // Si solo tiene hijo derecho, lo reemplazamos por el hijo derecho
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                // Si solo tiene hijo izquierdo, lo reemplazamos por el hijo izquierdo
                return nodo.izquierdo;
            } else {
                // Si tiene ambos hijos, encontramos el sucesor en el subárbol derecho
                Nodo sucesor = encontrarMinimo(nodo.derecho);
                nodo.producto = sucesor.producto;  // Reemplazamos el producto del nodo con el producto del sucesor
                nodo.derecho = eliminarr(nodo.derecho, sucesor.producto.getId());  // Eliminamos el sucesor
            }
        }
        return nodo;  // Retornamos el nodo actualizado
    }
        
    // Método para encontrar el nodo con el valor mínimo en el subárbol derecho
    private Nodo encontrarMinimo(Nodo nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }
}
