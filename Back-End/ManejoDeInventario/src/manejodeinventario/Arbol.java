
package manejodeinventario;

public class Arbol {
    private Nodo raiz;

    public Arbol() {
        raiz = null;
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
            System.out.println("Producto eliminado.");
            return true;
        } else {
            System.out.println("No se encontró el producto con ID: " + id + " o tiene stock disponible.");
            return false;
        }
    }
    //Eliminación considerando los tres casos mencionados
    private Nodo eliminarr(Nodo nodo, int id) {
        if (nodo == null) {
            return null;
        }

        if (id < nodo.producto.getId()) {
            nodo.izquierdo = eliminarr(nodo.izquierdo, id);
        } else if (id > nodo.producto.getId()) {
            nodo.derecho = eliminarr(nodo.derecho, id);
        } else {
            if (nodo.producto.getCantidad() > 0) {
                System.out.println("No se puede eliminar el producto con stock disponible.");
                return nodo;
            }

            // Caso 1: Nodo sin hijos
            if (nodo.izquierdo == null && nodo.derecho == null) {
                return null;
            }
            
            // Caso 2: Nodo con un solo hijo
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            }
            
            // Caso 3: Nodo con dos hijos
            Nodo sucesor = encontrarmin(nodo.derecho);
            nodo.producto = sucesor.producto;
            nodo.derecho = eliminarr(nodo.derecho, sucesor.producto.getId());
        }
        return nodo;
    }
    // Método para encontrar el nodo con el valor mínimo en un subárbol
    private Nodo encontrarmin(Nodo nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }
}
