
package manejodeinventario;


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
        
        if (producto != null && producto.getCantidad() > 0) {
            System.out.println("No se puede eliminar el producto con stock disponible.");
            return false;
        }
        
        // Llamar al método eliminar de la lista enlazada
        return tabla[indice].eliminarNodo(id);
    }
    
}
