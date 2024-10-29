
package manejodeinventario;

public class ManejoDeInventario {

    public static void main(String[] args) {
        /*TablaHash inventario = new TablaHash(10);
        
        Producto producto1 = new Producto(1, "Laptop", 10, 89.99);
        Producto producto4 = new Producto(11, "Computadora", 20, 599.99);
        Producto producto2 = new Producto(2, "Mouse", 50, 19.99);
        
        inventario.insertar(producto1);
        inventario.insertar(producto4);
        inventario.insertar(producto2);
        
        System.out.println("Producto con ID 2: " + inventario.buscar(2));
        
        inventario.mostrarTabla();*/
        Arbol inventario = new Arbol();
        Producto producto1 = new Producto(1, "Laptop", 10, 899.99);
        Producto producto2 = new Producto(2, "Teclado", 10, 29.99);
        Producto producto3 = new Producto(3, "Mouse", 5, 19.99);
        Producto producto4 = new Producto(4, "Monitor", 5, 129.99);
        Producto producto5 = new Producto(5, "Impresora", 3, 249.99);
        
        inventario.insertar(producto1);
        inventario.insertar(producto2);
        inventario.insertar(producto3);
        inventario.insertar(producto4);
        inventario.insertar(producto5);

        // Mostrar el árbol en orden
        inventario.mostrarInOrden();
    }
    
}
