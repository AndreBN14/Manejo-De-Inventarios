
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
}
