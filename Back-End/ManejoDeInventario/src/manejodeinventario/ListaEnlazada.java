
package manejodeinventario;

//Para la tabla hash para el manejo de colisiones
public class ListaEnlazada {
    private NodoProducto lista = null;
    
    public void agregarProducto(int id, Producto producto){
        NodoProducto actual = lista;
   
        while(actual != null){
            
            //Buscamos si ya existe en la lista para actualizar
            if(actual.id == id){
                actual.producto = producto;
                return;
            }
            actual = actual.siguiente;
        }
        NodoProducto np = new NodoProducto(id, producto);
        np.siguiente = lista;
        lista = np;
    }
    
    public Producto obtenerProducto(int id){
        NodoProducto actual = lista;
        
        while(actual != null){
            if(actual.id == id){
                return actual.producto;
            }
            actual = actual.siguiente;
        }
        return null;
    }
    
    public void mostrarLista(){
        NodoProducto actual = lista;
        System.out.println("La lista es: ");
        
        while(actual != null){
            System.out.println("[" + actual.producto + "]");
            actual = actual.siguiente;
        }
        
        System.out.println("null");
    }
    
    public boolean eliminarNodo(int id) {
        if (lista == null) return false;
    
        if (lista.id == id) {
            lista = lista.siguiente;
            return true;
        }
    
        NodoProducto actual = lista;
        while (actual.siguiente != null && actual.siguiente.id != id) {
            actual = actual.siguiente;
        }
    
        if (actual.siguiente == null) return false;
    
        actual.siguiente = actual.siguiente.siguiente;
        return true;
    }
    
}
