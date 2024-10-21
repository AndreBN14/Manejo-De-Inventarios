
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
}
