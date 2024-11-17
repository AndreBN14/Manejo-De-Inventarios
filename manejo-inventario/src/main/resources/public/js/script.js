// Función para validar los datos de producto antes de enviarlos
function validarDatosProducto(id, nombre, cantidad, precio, validarNombre = true) {
    if (isNaN(id) || id <= 0) {
        alert('ID inválido. Debe ser un número positivo.');
        return false;
    }
    if (validarNombre && !nombre.trim()) {
        alert('Nombre inválido. Este campo es obligatorio.');
        return false;
    }
    if (isNaN(cantidad) || cantidad < 0) {
        alert('Cantidad inválida. Debe ser un número no negativo.');
        return false;
    }
    if (isNaN(precio) || precio <= 0 || !/^(\d+(\.\d{1,2})?)$/.test(precio)) {
        alert('Precio inválido. Debe ser positivo y con un máximo de dos decimales.');
        return false;
    }
    return true;
}

// Función para limpiar un formulario
function clearForm(formId) {
    document.getElementById(formId).reset();
}

// Evento para añadir un producto con validación de datos
document.getElementById('addProductForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const id = parseInt(document.getElementById('productId').value);
    const nombre = document.getElementById('productName').value;
    const cantidad = parseInt(document.getElementById('productQuantity').value);
    const precio = parseFloat(document.getElementById('productPrice').value);

    if (!validarDatosProducto(id, nombre, cantidad, precio)) return;

    fetch('/api/productos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id, nombre, cantidad, precio })
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadProductList();
        clearForm('addProductForm'); // Limpiar el formulario después de añadir
    })
    .catch(error => alert('Error al añadir producto: ' + error));
});

// Evento para actualizar un producto con validación de datos
document.getElementById('updateProductForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const id = parseInt(document.getElementById('updateProductId').value);
    const nuevaCantidad = parseInt(document.getElementById('updateProductQuantity').value);
    const nuevoPrecio = parseFloat(document.getElementById('updateProductPrice').value);

    if (!validarDatosProducto(id, 'dummy', nuevaCantidad, nuevoPrecio, false)) return;

    fetch(`/api/productos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cantidad: nuevaCantidad, precio: nuevoPrecio })
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadProductList();
        clearForm('updateProductForm'); // Limpiar el formulario después de actualizar
    })
    .catch(error => alert('Error al actualizar producto: ' + error));
});

// Función para buscar un producto por ID
function searchProduct() {
    const id = parseInt(document.getElementById('searchProductId').value);

    fetch(`/api/productos/${id}`)
        .then(response => {
            if (!response.ok) throw new Error('Producto no encontrado');
            return response.json();
        })
        .then(product => {
            document.getElementById('searchResult').innerHTML = `
                <div class="table-responsive">
                    <table class="table table-bordered mt-3">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Cantidad</th>
                                <th>Precio</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>${product.id}</td>
                                <td>${product.nombre}</td>
                                <td>${product.cantidad}</td>
                                <td>$${product.precio.toFixed(2)}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            `;
        })
        .catch(error => {
            document.getElementById('searchResult').innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
            document.getElementById('searchProductId').value = ''; // Limpiar campo de búsqueda después de error
        });
}

// Función para eliminar un producto
function deleteProduct() {
    const productId = document.getElementById('deleteProductId').value;
    fetch(`/api/productos/${productId}`, {
        method: 'DELETE'
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadProductList();
        // Limpiar el campo de ID de eliminación y el resultado de búsqueda si coincide
        document.getElementById('deleteProductId').value = '';
        const searchResultId = document.getElementById('searchProductId').value;
        if (searchResultId === productId) {
            document.getElementById('searchResult').innerHTML = '';
            document.getElementById('searchProductId').value = '';
        }
    })
    .catch(error => alert('Error al eliminar producto: ' + error));
}

// Función para cargar y mostrar la lista de productos
function loadProductList() {
    fetch('/api/productos')
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar productos');
            return response.json();
        })
        .then(products => {
            const productList = document.getElementById('productList');
            if (products.length === 0) {
                productList.innerHTML = '<div class="alert alert-info">No hay productos registrados.</div>';
                return;
            }

            productList.innerHTML = `
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Cantidad</th>
                            <th>Precio</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${products.map(product => `
                            <tr>
                                <td>${product.id}</td>
                                <td>${product.nombre}</td>
                                <td>${product.cantidad}</td>
                                <td>$${product.precio.toFixed(2)}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        })
        .catch(error => {
            document.getElementById('productList').innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
        });
}

// Cargar la lista de productos cuando la página se carga por primera vez
document.addEventListener('DOMContentLoaded', loadProductList);
