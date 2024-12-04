# Sistema de Manejo de Inventarios

## Descripción
Este proyecto es un sistema de gestión de inventarios desarrollado como parte del curso de Estructura de Datos. La aplicación permite administrar eficientemente el control de productos, existencias y movimientos de inventario.

## Características
- Gestión de productos
- Control de existencias
- Registro de entradas y salidas
- Generación de reportes
- Interfaz intuitiva

## Tecnologías Utilizadas
- Java
- Estructuras de datos avanzadas
- Interfaz gráfica de usuario

## Instalación
1. Clonar el repositorio
2. Abrir el proyecto en su IDE preferido
3. Ejecutar la aplicación

## Dependencias
1. Tener instalado y configurado Maven en el equipo

Tutorial de instalación:
https://www.youtube.com/watch?v=rl5-yyrmp-0

## Ejecución
1. Una vez clonado el repositorio abrir la terminal del proyecto
2. En la terminal ingresar el comando de instalación de las dependencias:

mvn clean install
3. Una vez instaladas navegar a la carpeta manejo-inventario

cd manejo-inventario
4. Ahora en la terminal de la carpeta ingresar el sigueinte comando:

mvn exec:java
5. Una vez ingresado cerca al final del mensaje de la terminal habra una dirección de localhost en la cual se estara ejecutando el proyecto

Ejemplo: 

[Thread-2] INFO org.eclipse.jetty.server.AbstractConnector - Started ServerConnector@526860db{HTTP/1.1, (http/1.1)}{0.0.0.0:4567}
En ese caso deberia ingresar al:

http://localhost:4567

(Usar el ejermplo de referencia, la dirección siempre variara)

## Uso
El sistema permite:
- Agregar nuevos productos
- Actualizar existencias
- Consultar inventario
- Generar reportes de movimientos

## Contribuciones
Las contribuciones son bienvenidas. Para cambios importantes, por favor abra primero un issue para discutir lo que le gustaría cambiar.

## Autores
- Carlos Montenegro
- Max Astuhuaman
- Andre Barrantes
- Albert Huaman

## Licencia
Este proyecto está bajo la Licencia MIT.
