document.getElementById('form-producto').addEventListener('submit', function(e) {
    e.preventDefault();

    let id = document.getElementById('id').value;
    let nombre = document.getElementById('nombre').value;
    let precio = document.getElementById('precio').value;
    let cantidad = document.getElementById('cantidad').value;

    const producto = {
        id: id,
        nombre: nombre,
        precio: precio,
        cantidad: cantidad
    };

    // Llamada AJAX para enviar el producto al servidor
    fetch('http://localhost:4567/api/productos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(producto)
    })
    .then(response => response.json())
    .then(data => {
        alert('Producto agregado exitosamente!');
    })
    .catch(error => {
        alert('Error al agregar producto.');
    });
});
