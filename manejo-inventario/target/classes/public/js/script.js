// Función para cargar todos los productos
function cargarProductos() {
    fetch('/api/productos')
        .then(response => response.json())
        .then(data => {
            const productList = document.getElementById('product-list');
            productList.innerHTML = '';
            data.forEach(producto => {
                const li = document.createElement('li');
                li.textContent = `ID: ${producto.id} | Nombre: ${producto.nombre} | Cantidad: ${producto.cantidad} | Precio: $${producto.precio}`;
                productList.appendChild(li);
            });
        })
        .catch(error => console.error('Error al cargar productos:', error));
}

// Evento para añadir un producto
document.getElementById('add-product-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const id = parseInt(document.getElementById('id').value);
    const nombre = document.getElementById('nombre').value;
    const cantidad = parseInt(document.getElementById('cantidad').value);
    const precio = parseFloat(document.getElementById('precio').value);

    fetch('/api/productos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id,nombre, cantidad, precio })
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        cargarProductos();
    })
    .catch(error => console.error('Error al añadir producto:', error));
});

// Evento para actualizar un producto
document.getElementById('update-product-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const id = parseInt(document.getElementById('update-id').value);
    const nuevaCantidad = parseInt(document.getElementById('update-cantidad').value);
    const nuevoPrecio = parseFloat(document.getElementById('update-precio').value);

    fetch(`/api/productos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cantidad: nuevaCantidad, precio: nuevoPrecio })
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        cargarProductos();
    })
    .catch(error => console.error('Error al actualizar producto:', error));
});

// Evento para eliminar un producto
document.getElementById('delete-product-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const id = parseInt(document.getElementById('delete-id').value);
    console.log("Eliminando producto con ID:", id); // Para depurar

    fetch(`/api/productos/${id}`, {
        method: 'DELETE'
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        cargarProductos();
    })
    .catch(error => console.error('Error al eliminar producto:', error));
});

// Cargar productos al inicio
cargarProductos();
