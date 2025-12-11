# Etapa 1: Construcción (Build)
# Usamos una imagen oficial de Maven con JDK 17 para compilar el código
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo pom.xml padre (raíz) para heredar configuraciones comunes
COPY pom.xml .

# Copiamos el pom.xml específico del microservicio de solicitudes
COPY solicitudes-service/pom.xml solicitudes-service/

# Copiamos el código fuente del microservicio
COPY solicitudes-service/src solicitudes-service/src

# Ejecutamos Maven para compilar y empaquetar el proyecto
# -f: indica la ruta del pom.xml
# clean package: limpia target y empaqueta el jar
# -DskipTests: omite la ejecución de pruebas para agilizar el build
RUN mvn -f solicitudes-service/pom.xml clean package -DskipTests

# Etapa 2: Ejecución (Run)
# Usamos una imagen ligera de JRE 17 (solo entorno de ejecución) para producción
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo para la aplicación
WORKDIR /app

# Copiamos el archivo .jar generado en la etapa anterior (build)
# --from=build: referencia a la etapa 1
COPY --from=build /app/solicitudes-service/target/*.jar app.jar

# Exponemos el puerto 8082 donde escucha este microservicio
EXPOSE 8082

# Definimos el comando de inicio de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
