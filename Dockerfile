# Usamos una imagen base ligera con Java 17, que es la version de tu proyecto
FROM eclipse-temurin:17-jdk-alpine

# Definimos un volumen temporal
VOLUME /tmp

# Copiamos el archivo JAR compilado por Maven dentro de la imagen
COPY target/*.jar app.jar

# Exponemos el puerto donde corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicacion
ENTRYPOINT ["java","-jar","/app.jar"]