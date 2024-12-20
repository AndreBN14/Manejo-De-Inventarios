/**
 * Archivo principal de JavaScript para el manejo del inventario
 * Contiene todas las funciones necesarias para la gestión de productos,
 * incluyendo operaciones CRUD, paginación, ordenamiento y filtrado
 */

// ============= UTILIDADES Y FUNCIONES AUXILIARES =============

/**
 * Muestra una notificación temporal en la esquina superior derecha
 * @param {string} message - Mensaje a mostrar
 * @param {string} type - Tipo de notificación ('success', 'danger', etc.)
 */
function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed top-0 end-0 m-3`;
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    document.body.appendChild(notification);
    setTimeout(() => notification.remove(), 5000);
}

/**
 * Valida los datos de un producto antes de su inserción o actualización
 * @param {number} id - ID del producto
 * @param {string} nombre - Nombre del producto
 * @param {number} cantidad - Cantidad en inventario
 * @param {number} precio - Precio del producto
 * @param {boolean} validarNombre - Indica si se debe validar el nombre
 * @returns {boolean} - True si los datos son válidos, False en caso contrario
 */
function validarDatosProducto(id, nombre, cantidad, precio, validarNombre = true) {
    const errors = [];
    
    if (isNaN(id) || id <= 0) {
        errors.push('ID inválido. Debe ser un número positivo.');
    }
    if (validarNombre && (!nombre || !nombre.trim())) {
        errors.push('Nombre inválido. Este campo es obligatorio.');
    }
    if (isNaN(cantidad) || cantidad < 0) {
        errors.push('Cantidad inválida. Debe ser un número no negativo.');
    }
    if (isNaN(precio) || precio <= 0 || !/^(\d+(\.\d{1,2})?)$/.test(precio)) {
        errors.push('Precio inválido. Debe ser positivo y con un máximo de dos decimales.');
    }
    
    if (errors.length > 0) {
        showNotification(errors.join('\n'), 'danger');
        return false;
    }
    return true;
}

/**
 * Formatea un número a formato de moneda USD
 * @param {number} amount - Cantidad a formatear
 * @returns {string} - Cantidad formateada en formato USD
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('es-PE', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

// ============= GESTIÓN DE FORMULARIOS Y UI =============

/**
 * Limpia un formulario y remueve las clases de validación
 * @param {string} formId - ID del formulario a limpiar
 */
function clearForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return;
    
    form.reset();
    form.classList.remove('was-validated');
    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
        input.classList.remove('is-invalid', 'is-valid');
    });
}

/**
 * Maneja las respuestas de las peticiones fetch
 * @param {Response} response - Respuesta del servidor
 * @throws {Error} Si la respuesta no es satisfactoria
 */
async function handleFetchResponse(response) {
    if (!response.ok) {
        const text = await response.text();
        throw new Error(text || `Error ${response.status}: ${response.statusText}`);
    }
    return response.text();
}

// ============= VARIABLES GLOBALES DE CONTROL =============

/**
 * Variables para control de paginación y ordenamiento
 * currentPage: Página actual en la tabla
 * itemsPerPage: Cantidad de items por página
 * currentSort: Configuración actual de ordenamiento
 * allProducts: Array con todos los productos cargados
 */
let currentPage = 1;
let itemsPerPage = 10;
let currentSort = { field: 'id', direction: 'asc' };
let allProducts = [];

// ============= FUNCIONES DE ORDENAMIENTO Y PAGINACIÓN =============

/**
 * Ordena la lista de productos según la configuración especificada
 * @param {Array} products - Lista de productos a ordenar
 * @param {Object} sortConfig - Configuración de ordenamiento {field, direction}
 */
function sortProducts(products, sortConfig) {
    return [...products].sort((a, b) => {
        let aValue = a[sortConfig.field];
        let bValue = b[sortConfig.field];
        
        // Convertir a números si el campo es id, cantidad o precio
        if (['id', 'cantidad', 'precio'].includes(sortConfig.field)) {
            aValue = Number(aValue);
            bValue = Number(bValue);
        }
        
        if (sortConfig.direction === 'asc') {
            return aValue > bValue ? 1 : -1;
        } else {
            return aValue < bValue ? 1 : -1;
        }
    });
}

/**
 * Actualiza los iconos de ordenamiento en los encabezados de la tabla
 * @param {Object} sortConfig - Configuración actual de ordenamiento
 */
function updateSortIcons(sortConfig) {
    document.querySelectorAll('.sortable i').forEach(icon => {
        icon.className = 'fas fa-sort';
    });
    
    const currentHeader = document.querySelector(`th[data-sort="${sortConfig.field}"] i`);
    if (currentHeader) {
        currentHeader.className = `fas fa-sort-${sortConfig.direction === 'asc' ? 'up' : 'down'}`;
    }
}

// ============= OPERACIONES CRUD =============

/**
 * Actualiza la información de paginación en la UI
 * @param {Array} filteredProducts - Lista de productos filtrados
 * Actualiza el contador de items y estado de los botones de navegación
 */
function updatePaginationInfo(filteredProducts) {
    const totalItems = filteredProducts.length;
    const startItem = totalItems === 0 ? 0 : (currentPage - 1) * itemsPerPage + 1;
    const endItem = Math.min(startItem + itemsPerPage - 1, totalItems);
    
    document.getElementById('startItem').textContent = startItem;
    document.getElementById('endItem').textContent = endItem;
    document.getElementById('totalItems').textContent = totalItems;
    
    // Actualizar estado de los botones de navegación
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = endItem >= totalItems;
}

/**
 * Carga la lista completa de productos desde el servidor
 * Realiza una petición GET a /api/productos y actualiza la UI
 * En caso de error, muestra una notificación al usuario
 */

async function cargarArchivo() {
    try {
        await fetch('/api/cargar');
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al cargar el archivo', 'danger');
    }
}

async function loadProductList() {
    try {
        const response = await fetch('/api/productos');
        const products = await response.json();
        allProducts = products;
        applyFiltersAndUpdate();
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al cargar los productos', 'danger');
    }
}

/**
 * Aplica los filtros actuales y actualiza la visualización de la tabla
 * - Aplica filtros de precio y cantidad
 * - Ordena los productos según la configuración actual
 * - Aplica la paginación
 * - Actualiza la UI con los resultados
 */
function applyFiltersAndUpdate() {
    let filteredProducts = [...allProducts];
    
    // Aplicar filtros existentes
    const minPrice = document.getElementById('minPrice').value;
    const maxPrice = document.getElementById('maxPrice').value;
    const minQuantity = document.getElementById('minQuantity').value;
    const maxQuantity = document.getElementById('maxQuantity').value;

    if (minPrice) filteredProducts = filteredProducts.filter(p => p.precio >= parseFloat(minPrice));
    if (maxPrice) filteredProducts = filteredProducts.filter(p => p.precio <= parseFloat(maxPrice));
    if (minQuantity) filteredProducts = filteredProducts.filter(p => p.cantidad >= parseInt(minQuantity));
    if (maxQuantity) filteredProducts = filteredProducts.filter(p => p.cantidad <= parseInt(maxQuantity));

    // Ordenar productos
    filteredProducts = sortProducts(filteredProducts, currentSort);

    // Aplicar paginación
    const startIndex = (currentPage - 1) * itemsPerPage;
    const paginatedProducts = filteredProducts.slice(startIndex, startIndex + itemsPerPage);

    // Actualizar la tabla
    const tableBody = document.querySelector('#productTable tbody');
    tableBody.innerHTML = '';

    if (paginatedProducts.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center">
                    <div class="alert alert-info m-0">
                        No hay productos que coincidan con los filtros
                    </div>
                </td>
            </tr>
        `;
    } else {
        paginatedProducts.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.id}</td>
                <td>${product.nombre}</td>
                <td>${product.cantidad}</td>
                <td>${formatCurrency(product.precio)}</td>
            `;
            tableBody.appendChild(row);
        });
    }

    // Actualizar información de paginación
    updatePaginationInfo(filteredProducts);
    // Actualizar iconos de ordenamiento
    updateSortIcons(currentSort);
}

// ============= GESTIÓN DE PRODUCTOS =============

/**
 * Maneja el evento de envío del formulario de nuevo producto
 * Valida los datos y realiza la petición POST al servidor
 * @param {Event} e - Evento del formulario
 */

// Evento para añadir un producto con validación de datos
document.getElementById('addProductForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    this.classList.add('was-validated');

    //const id = parseInt(document.getElementById('productId').value);
    const nombre = document.getElementById('productName').value;
    const cantidad = parseInt(document.getElementById('productQuantity').value);
    const precio = parseFloat(document.getElementById('productPrice').value);

    //if (!validarDatosProducto(id, nombre, cantidad, precio)) return;

    try {
        const response = await fetch('/api/productos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            //body: JSON.stringify({ id, nombre, cantidad, precio })
            body: JSON.stringify({ nombre, cantidad, precio })
        });
        const message = await handleFetchResponse(response);
        if (response.status === 200) {
            showNotification('Producto ya existe.');
        } else {
            const product = JSON.parse(message);
            document.getElementById('productId').value = product.id;
            document.getElementById('productId').classList.add('is-valid');
            showNotification('Producto añadido exitosamente');
            //clearForm('addProductForm');
            loadProductList();
        }
    } catch (error) {
        showNotification(error.message, 'danger');
    }
});

// Evento para actualizar un producto con validación de datos
document.getElementById('updateProductForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    this.classList.add('was-validated');

    const id = parseInt(document.getElementById('updateProductId').value);
    const nuevaCantidad = parseInt(document.getElementById('updateProductQuantity').value);
    const nuevoPrecio = parseFloat(document.getElementById('updateProductPrice').value);

    if (!validarDatosProducto(id, 'dummy', nuevaCantidad, nuevoPrecio, false)) return;

    try {
        const response = await fetch(`/api/productos/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ cantidad: nuevaCantidad, precio: nuevoPrecio })
        });
        const message = await handleFetchResponse(response);
        showNotification('Producto actualizado exitosamente');
        clearForm('updateProductForm');
        loadProductList();
    } catch (error) {
        showNotification(error.message, 'danger');
    }
});

/**
 * Busca un producto por ID
 * Realiza una petición GET al servidor y actualiza el formulario
 * con los datos del producto encontrado
 */
async function searchProduct() {
    const searchInput = document.getElementById('searchProductId');
    const id = parseInt(searchInput.value);
    
    if (!id || id <= 0) {
        showNotification('Por favor, ingrese un ID válido', 'warning');
        return;
    }

    try {
        const response = await fetch(`/api/productos/${id}`);
        if (!response.ok) throw new Error('Producto no encontrado');
        
        const product = await response.json();
        document.getElementById('searchResult').innerHTML = `
            <div class="table-responsive">
                <table class="table table-bordered mt-3">
                    <thead class="table-light">
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
                            <td>${formatCurrency(product.precio)}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        `;
    } catch (error) {
        document.getElementById('searchResult').innerHTML = `
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle me-2"></i>${error.message}
            </div>
        `;
        searchInput.value = '';
    }
}

/**
 * Elimina un producto del inventario
 * Realiza una petición DELETE al servidor y actualiza la tabla
 * Muestra notificaciones de éxito o error según corresponda
 */
async function deleteProduct() {
    const deleteInput = document.getElementById('deleteProductId');
    const productId = deleteInput.value;
    
    if (!productId || productId <= 0) {
        showNotification('Por favor, ingrese un ID válido', 'warning');
        return;
    }

    if (!confirm('¿Está seguro de que desea eliminar este producto?')) {
        return;
    }

    try {
        const response = await fetch(`/api/productos/${productId}`, {
            method: 'DELETE'
        });
        const message = await handleFetchResponse(response);
        showNotification('Producto eliminado exitosamente');
        deleteInput.value = '';
        
        // Limpiar búsqueda si coincide con el producto eliminado
        const searchResultId = document.getElementById('searchProductId').value;
        if (searchResultId === productId) {
            document.getElementById('searchResult').innerHTML = '';
            document.getElementById('searchProductId').value = '';
        }
        
        loadProductList();
    } catch (error) {
        showNotification(error.message, 'danger');
    }
}

// ============= GESTIÓN DE TEMA =============

/**
 * Inicializa y gestiona el tema de la aplicación (claro/oscuro)
 * - Carga la preferencia del usuario desde localStorage
 * - Aplica el tema correspondiente
 * - Configura el toggle para cambiar entre temas
 */
function initTheme() {
    const checkbox = document.getElementById('checkbox');
    if (!checkbox) return; // Si no existe el checkbox, salimos de la función

    const currentTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', currentTheme);
    checkbox.checked = currentTheme === 'dark';

    checkbox.addEventListener('change', () => {
        const theme = checkbox.checked ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
    });
}

// ============= FILTROS AVANZADOS =============

/**
 * Configuración de filtros activos
* Almacena los valores actuales de los filtros de precio y cantidad
*/
let activeFilters = {
    minPrice: null,
    maxPrice: null,
    minQuantity: null,
    maxQuantity: null
};

/**
 * Aplica los filtros a la lista de productos
 * @param {Array} products - Lista de productos a filtrar
 * @returns {Array} - Lista de productos filtrados
 */
function applyFilters(products) {
    if (!products) return [];
    
    return products.filter(product => {
        const price = parseFloat(product.precio);
        const quantity = parseInt(product.cantidad);
        
        if (activeFilters.minPrice && price < activeFilters.minPrice) return false;
        if (activeFilters.maxPrice && price > activeFilters.maxPrice) return false;
        if (activeFilters.minQuantity && quantity < activeFilters.minQuantity) return false;
        if (activeFilters.maxQuantity && quantity > activeFilters.maxQuantity) return false;
        
        return true;
    });
}

/**
 * Actualiza los valores de los filtros según la entrada del usuario
 * Valida y normaliza los valores ingresados
 */
function updateFilters() {
    const minPriceEl = document.getElementById('minPrice');
    const maxPriceEl = document.getElementById('maxPrice');
    const minQuantityEl = document.getElementById('minQuantity');
    const maxQuantityEl = document.getElementById('maxQuantity');

    if (!minPriceEl || !maxPriceEl || !minQuantityEl || !maxQuantityEl) return;

    activeFilters = {
        minPrice: parseFloat(minPriceEl.value) || null,
        maxPrice: parseFloat(maxPriceEl.value) || null,
        minQuantity: parseInt(minQuantityEl.value) || null,
        maxQuantity: parseInt(maxQuantityEl.value) || null
    };
    loadProductList();
}

/**
 * Limpia todos los filtros aplicados
 * Resetea los campos de filtro y actualiza la tabla
 */
function clearFilters() {
    const elements = ['minPrice', 'maxPrice', 'minQuantity', 'maxQuantity'];
    elements.forEach(id => {
        const element = document.getElementById(id);
        if (element) element.value = '';
    });

    activeFilters = {
        minPrice: null,
        maxPrice: null,
        minQuantity: null,
        maxQuantity: null
    };
    loadProductList();
}

// ============= EVENT LISTENERS =============

// Configuración inicial de la aplicación
document.addEventListener('DOMContentLoaded', async function() {
    // Inicializar tema
    initTheme();

    await cargarArchivo();

    // Cargar productos
    await loadProductList();
    
    // Inicializar event listeners para filtros
    const applyFiltersBtn = document.getElementById('applyFilters');
    const clearFiltersBtn = document.getElementById('clearFilters');
    
    if (applyFiltersBtn) {
        applyFiltersBtn.addEventListener('click', updateFilters);
    }
    
    if (clearFiltersBtn) {
        clearFiltersBtn.addEventListener('click', clearFilters);
    }
    
    // Inicializar tooltips
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(tooltip => new bootstrap.Tooltip(tooltip));
    
    // Event listeners para paginación
    document.getElementById('prevPage').addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            applyFiltersAndUpdate();
        }
    });

    document.getElementById('nextPage').addEventListener('click', () => {
        const totalItems = allProducts.length;
        const maxPage = Math.ceil(totalItems / itemsPerPage);
        if (currentPage < maxPage) {
            currentPage++;
            applyFiltersAndUpdate();
        }
    });

    document.getElementById('itemsPerPage').addEventListener('change', (e) => {
        itemsPerPage = parseInt(e.target.value);
        currentPage = 1; // Reset a la primera página
        applyFiltersAndUpdate();
    });

    // Event listeners para ordenamiento
    document.querySelectorAll('.sortable').forEach(header => {
        header.addEventListener('click', () => {
            const field = header.dataset.sort;
            if (currentSort.field === field) {
                currentSort.direction = currentSort.direction === 'asc' ? 'desc' : 'asc';
            } else {
                currentSort.field = field;
                currentSort.direction = 'asc';
            }
            applyFiltersAndUpdate();
        });
    });

    // Event listeners para filtros
    document.getElementById('applyFilters').addEventListener('click', () => {
        currentPage = 1; // Reset a la primera página al aplicar filtros
        applyFiltersAndUpdate();
    });

    document.getElementById('clearFilters').addEventListener('click', () => {
        document.getElementById('minPrice').value = '';
        document.getElementById('maxPrice').value = '';
        document.getElementById('minQuantity').value = '';
        document.getElementById('maxQuantity').value = '';
        currentPage = 1;
        applyFiltersAndUpdate();
    });
});
