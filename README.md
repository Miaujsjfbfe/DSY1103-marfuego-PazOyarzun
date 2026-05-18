# MarFuego — DSY1103 Desarrollo FullStack 1

## Descripción

MarFuego Colectivo presenta problemas en la coordinación entre cocina, caja y reservas, generando errores en pedidos, disponibilidad de platos y control de mesas entre sus distintos locales.

Para solucionar esto, se desarrolló un sistema basado en microservicios con Spring Boot, permitiendo gestionar locales, mesas, menús, pedidos y reservas mediante APIs REST independientes y conectadas entre sí.

---

## Equipo

| Nombre | GitHub        |
|--------|---------------|
| Paz Oyarzún | @Miaujsjfbfe  |

---

## Microservicios Implementados

| # | Microservicio | Puerto | Descripción                               |
|---|---------------|--------|-------------------------------------------|
| 1 | ms-locales | 8081 | Gestión de locales y mesas disponibles, permitiendo verificar mesas libres u ocupadas para reservas |
| 2 | ms-menu | 8082 | Administración de platos, incluyendo precios, disponibilidad y locales asociados |
| 3 | ms-pedidos | 8083 | Gestión de pedidos y detalle de pedidos, validando disponibilidad de platos mediante comunicación con ms-menu |
| 4 | ms-reservas | 8084 | Gestión de reservas de mesas, consultando disponibilidad en ms-locales antes de confirmar |
---


## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Hibernate
- Maven
- MySQL
- WebClient / OpenFeign
- SLF4J
- IntelliJ IDEA
- Postman

---

## Cómo Ejecutar el Proyecto
1. Clonar el repositorio: `git clone [URL]`
2. Configurar la base de datos en `application.properties`
3. Ejecutar cada microservicio: `./mvnw spring-boot:run`

---

## Estado del Proyecto

🔄 En desarrollo — EP2 2025
