# Authentication Service

This service is responsible for handling user authentication and authorization.

## Features

-   **User Registration:** Allows new users to register with a username and password.
-   **User Login:** Authenticates users and issues a JSON Web Token (JWT) upon successful login.
-   **JWT-based Authentication:** Secures endpoints by requiring a valid JWT in the `Authorization` header.

## Endpoints

-   `POST /api/auth/register`: Registers a new user. Requires a JSON body with `username` and `password`.
-   `POST /api/auth/login`: Authenticates a user and returns a JWT. Requires a JSON body with `username` and `password`.
