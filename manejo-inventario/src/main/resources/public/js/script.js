document.getElementById('addProductForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const id = parseInt(document.getElementById('productId').value);
    const nombre = document.getElementById('productName').value;
    const cantidad = parseInt(document.getElementById('productQuantity').value);
    const precio = parseFloat(document.getElementById('productPrice').value);

    fetch('/api/productos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id, nombre, cantidad, precio })
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadProductList();  // Actualizar la lista de productos después de añadir uno nuevo
    })
    .catch(error => alert('Error al añadir producto: ' + error));
});

// Función para eliminar un producto
function deleteProduct() {
    const productId = document.getElementById('deleteProductId').value;
    fetch(`/api/productos/${productId}`, {
        method: 'DELETE'
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadProductList();  // Actualizar la lista de productos después de eliminar uno
    })
    .catch(error => alert('Error al eliminar producto: ' + error));
}

// Función para cargar y mostrar la lista de productos
function loadProductList() {
    fetch('/api/productos')  // Asume que esta ruta devuelve una lista de productos
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar productos');
            return response.json();
        })
        .then(products => {
            const productList = document.getElementById('productList');
            productList.innerHTML = products.map(product => `
                <div class="card mt-2 p-3">
                    <h4>${product.nombre}</h4>
                    <p>ID: ${product.id}</p>
                    <p>Cantidad: ${product.cantidad}</p>
                    <p>Precio: $${product.precio}</p>
                </div>
            `).join('');
        })
        .catch(error => {
            document.getElementById('productList').innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
        });
}

// Cargar la lista de productos cuando la página se carga por primera vez
document.addEventListener('DOMContentLoaded', loadProductList);

