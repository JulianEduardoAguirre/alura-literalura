# ONE - Challenge 02 - LiterAlura
Implementación de un programa para la consulta de libros en , procesando la información de la API y guardado en una base de datos.

## Tecnologías Utilizadas
- Java
- SpringBoot
- Maven
- PostgreSQL

## Descripción
El programa presenta un menú de opciones para la petición de un libro en la API de __Gutendex__ y para hacer consultar a los registros que se van 
guardando en la base de datos. Tener en cuenta que la API mencionada no precisa el uso de algún tipo de autenticación, por lo que el programa se puede
ejecutar una vez cargado el proyecto en el IDE o luego de compilar/ejecutar.


## Configuración
De momento, el programa se debe ejecutar en CLI o en una terminal dentro del IDE elegido por el usuario. El desarrollo del mismo se realizó con IntelliJ IDEA.

## Requisitos
- JDK 17
- PostgreSQL
- IDE (IntelliJ IDEA recomendado)

## Instrucciones de instalación

1. Clonar o descargar el repositorio y abrirlo en IntelliJ IDEA, proceso en el cual, además, se instalarán las dependencias necesarias
2. Crear una nueva base de datos en su gestor Postgre con el nombre 'literalura'
3. Crear las siguientes variables de entorno del sistema:
    - DB_HOST -> Dominio donde se aloja la base de datos
    - DB_NAME -> Nombre de la base de datos (forohub)
    - DB_USER -> Nombre de usuario
    - DB_PASSWORD -> Contraseña

**De no querer usar variables de entorno, se pueden establecer los valores en el archivo 'aplication.properties' **

## Uso
Una vez cargado el programa y sus dependencias, al momento de ejecutar, se le presentará al usuario el siguiente menú:

					**** Bienvenido a LiterAlura ****
					Ingrese el número correspondiente a su consulta
					
					1)  Buscar libro por título
					2)  Listar libros registrados
					3)  Mostrar libros por idioma
					4)  Mostrar libro más descargado
					5)  Mostrar top descargas
					6)  Listar autores registrados
					7)  Mostrar autor más joven
					8)  Mostrar autor/es vivo/s en una fecha
					9)  Buscar autor por nombre
					10) Mostrar estadísticas
					11) Menú de idiomas

					0) Salir

- La primera opción realiza las consultar a la API en base a una palabra clave que ingrese el usuario.
- Opciones 2 a 9 realizan consultas específicas a la base de datos para obtención de información sobre __libros__ y __autores__.
- Opcion 10 permite obtener un resumen de la información alojada en la base de datos.
- Opcion 11 presenta un submenú, para el listado de libros según uno de los siguientes idiomas (de momento): Español, Inglés, Francés y Alemán.

