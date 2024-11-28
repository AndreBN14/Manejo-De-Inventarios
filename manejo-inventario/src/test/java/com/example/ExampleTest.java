package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de ejemplo para demostrar cómo escribir pruebas unitarias
 */
public class ExampleTest {
    
    @BeforeEach
    void setUp() {
        // Aquí puedes inicializar objetos que necesites para tus pruebas
    }

    @Test
    void ejemploDePrueba() {
        // Arrange (Preparar)
        int numero1 = 2;
        int numero2 = 3;
        
        // Act (Actuar)
        int resultado = numero1 + numero2;
        
        // Assert (Verificar)
        assertEquals(5, resultado, "La suma de 2 + 3 debe ser 5");
    }

    @Test
    void ejemploDePruebaConExcepcion() {
        // Ejemplo de cómo probar que se lance una excepción
        assertThrows(IllegalArgumentException.class, () -> {
            // Código que debería lanzar la excepción
            throw new IllegalArgumentException("Ejemplo de excepción");
        });
    }
}
