package manejodeinventario;

public class BuscadorProductos {
    private Arbol arbol;
    private TablaHash tablaHash;

    public BuscadorProductos(Arbol arbol, TablaHash tablaHash) {
        this.arbol = arbol;
        this.tablaHash = tablaHash;
    }

    public Producto buscarPorID(int id) {
        Producto producto = arbol.buscarPorID(id);
        if (producto != null) {
            return producto;
        }
        
        return tablaHash.buscar(id);
    }

    public Producto buscarPorNombre(String nombre) {
        Producto producto = arbol.buscarPorNombre(nombre);
        if (producto != null) {
            return producto;
        }

        return tablaHash.buscarPorNombre(nombre);
    }
}
