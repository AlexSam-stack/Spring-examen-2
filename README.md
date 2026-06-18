# Spring-examen-2
1. Instrucciones para Ejecutar el Proyecto
Asegúrese de tener instalados Java 17+ (o la versión correspondiente a su proyecto) y Maven. Ejecute los siguientes comandos en la terminal raíz del proyecto:

Bash
# 1. Limpiar el directorio 'target' y compilar el proyecto instalando las dependencias
mvn clean install

# 2. Levantar el servidor embebido de Spring Boot (Tomcat)
mvn spring-boot:run

2. Configuración de la Base de Datos (PostgreSQL)
Antes de iniciar la aplicación, configure sus credenciales en el archivo src/main/resources/application.properties y cree la base de datos ejecutando el siguiente comando en su gestor SQL (pgAdmin, DBeaver, etc.):

SQL
-- Crear la base de datos
CREATE DATABASE db_pedidos;

Para facilitar la evaluación y pruebas interactivas, puede acceder a la interfaz gráfica completa de Swagger UI desde el navegador web una vez el servidor esté corriendo:

URL de Swagger UI: http://localhost:8080/swagger-ui/index.html

Módulo: Clientes

POST /api/clientes
Permite registrar un nuevo cliente en el sistema.
GET /api/clientes/{id}
Permite buscar y obtener la información de un cliente mediante su identificador único.

Módulo: Productos

POST /api/productos
Permite registrar un nuevo producto en el inventario.
GET /api/productos
Permite listar todos los productos registrados en el catálogo.

Módulo: Pedidos

POST /api/pedidos
Permite registrar una nueva orden de compra junto con sus detalles.
GET /api/pedidos/{id}
Permite consultar la información completa de un pedido mediante su identificador.
GET /api/pedidos/cliente/{clienteId}
Permite obtener el historial de compras o pedidos asociados a un cliente específico.


4. Ejemplos de Request JSON (Carga de Datos)
Utilice los siguientes bloques de código JSON para probar el comportamiento de los endpoints de creación (POST):

A. Crear Cliente (POST /api/clientes)
JSON
{
  "nombre": "Carlos",
  "apellido": "Mendoza",
  "dni": "72145896",
  "correo": "carlos.mendoza@email.com"
}

B. Crear Producto (POST /api/productos)
JSON
{
  "nombre": "Teclado Mecánico RGB",
  "descripcion": "Teclado switch red idóneo para programación.",
  "precio": 89.90,
  "stock": 50,
}
C. Registrar Pedido (POST /api/pedidos)
(Asegúrese de haber ejecutado los dos inserts anteriores para que existan el clienteId: 1 y el productoId: 1).

JSON
{
  "clienteId": 1,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}


Tecnologías usadas
Java 21
Spring Boot
Maven
PostgreSQL
JUnit 5
Mockito
swaguer
