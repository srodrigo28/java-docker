### Estrutura do projeto
```
seu-projeto/
├─ database/
├─ src/
├─ Dockerfile
├─ docker-compose.yml
└─ pom.xml
```

### Dockerfile
```
FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml
```
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_db
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: app_db
    ports:
      - "3306:3306"
    volumes:
      - ./database:/var/lib/mysql

  app:
    build: .
    container_name: spring_app
    restart: always
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/app_db?useSSL=false&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
```

### application.properties
```
spring.datasource.url=jdbc:mysql://mysql:3306/app_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

docker compose up --build
docker compose down