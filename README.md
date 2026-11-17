# Food Delivery Backend

A REST API for a food delivery app, built with Spring Boot. Restaurant owners
manage restaurants and menus, customers place orders, and access is protected
with JWT authentication and role-based authorization.

## Tech Stack

Java 21, Spring Boot, Spring Data JPA (Hibernate), Spring Security, JWT (jjwt),
BCrypt, H2 (in-memory database), Swagger/OpenAPI (springdoc), JUnit 5, Mockito, Maven

## Features

- Register and login, with passwords hashed using BCrypt
- JWT authentication (stateless) with roles: CUSTOMER, RESTAURANT_OWNER, ADMIN
- Restaurant and menu CRUD, with pagination, sorting and city filter
- Ownership checks: an owner can change only their own restaurants and menus
- Orders with a price snapshot and stock check, inside one `@Transactional` method
  (if any item fails, nothing is saved)
- Order status flow (PLACED -> PREPARING -> OUT_FOR_DELIVERY -> DELIVERED) with
  validated transitions, and customer cancel that restores stock
- DTOs and mappers, request validation, global exception handling
- Unit tests for the order service (JUnit 5 and Mockito)

## Project Structure

```
controller -> service -> repository -> database
     |           |
    dto       mapper / entity

security  : JWT filter, security config, ownership check
exception : custom exceptions and global handler
config    : password encoder, Swagger
```

## How to Run

1. Install Java 21.
2. Clone the repository and open the folder.
3. Run `./mvnw spring-boot:run` (on Windows: `mvnw.cmd spring-boot:run`).
4. Open Swagger UI: http://localhost:8080/swagger-ui/index.html

Optional: set the environment variable `JWT_SECRET` (at least 32 characters).
A development default is used if it is not set. Never use the default in a
deployed app.

Data is stored in memory (H2), so it resets every time the app restarts.

## How to Test with Swagger or Postman

1. Register an owner: `POST /api/auth/register` with `"role":"RESTAURANT_OWNER"`.
2. Login: `POST /api/auth/login`, then copy the `token`.
3. Send the token as `Authorization: Bearer <token>` (in Swagger, use the Authorize button).
4. Create a restaurant, add menu items, then register and login as a customer to place an order.

## API Endpoints

| Method | URL | Access |
|---|---|---|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public (returns a JWT) |
| GET | /api/restaurants?city=&page=&size=&sortBy= | Public |
| GET | /api/restaurants/{id} | Public |
| POST | /api/restaurants | Owner, Admin |
| PUT, DELETE | /api/restaurants/{id} | That restaurant's owner, Admin |
| GET | /api/restaurants/{id}/menu | Public |
| POST | /api/restaurants/{id}/menu | That restaurant's owner, Admin |
| PUT, DELETE | /api/restaurants/{id}/menu/{menuId} | That restaurant's owner, Admin |
| POST | /api/orders | Logged-in user |
| GET | /api/orders/my | Logged-in user |
| PATCH | /api/orders/{id}/cancel | The customer who placed it (only while PLACED) |
| PATCH | /api/orders/{id}/status | That restaurant's owner, Admin |

## Design Decisions

- **DTOs and mappers:** entities are never returned directly, so password hashes and
  internal fields do not leak, and the API does not depend on the table design.
- **Transactions:** placing an order runs in one `@Transactional` method, so if one
  item is out of stock, the earlier stock changes are rolled back.
- **Price snapshot:** each order item stores the price at the time of the order, so
  later price changes do not alter old orders.
- **Two levels of authorization:** roles decide what kind of user you are, and an
  ownership check decides whose data you may change.

## Known Limitations and Future Improvements

- Data is in memory (H2); move to MySQL or PostgreSQL with Flyway migrations
- A restaurant with menu items cannot be deleted yet; add soft delete
- Two customers ordering the last item at the same moment need locking (`@Version`)
- Add refresh tokens, integration tests, Docker and deployment
