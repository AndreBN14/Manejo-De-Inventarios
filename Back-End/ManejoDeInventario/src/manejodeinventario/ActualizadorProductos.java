package manejodeinventario;

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

        return actualizadoEnArbol && actualizadoEnHash;
    }
}
