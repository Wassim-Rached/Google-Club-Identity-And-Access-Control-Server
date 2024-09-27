# Identity and Access Control Server (IAMC)

The **Identity and Access Control Server (IAMC)** is a backend service built with Spring Boot for handling user accounts, roles, and permissions management across multiple services. It centralizes the management of identities and access control policies within the system, ensuring secure and efficient authorization processes for all connected services.

## Features

### 1. **Account Management**
- **Account Creation**: Handles creating new user accounts with the appropriate default roles and permissions.
- **Account Updates**: Allows users or administrators to update account information, including usernames, emails, and other details.
- **Password Management**: Facilitates secure password management, including password updates and encryption.

### 2. **Role and Permission Management**
- **Role Creation and Assignment**: Supports the creation of roles that define the scope of permissions a user has. Roles can be assigned or removed from users.
- **Permission Management**: Enables administrators to create and manage fine-grained permissions (authorities) that define access to specific resources or actions.
- **Granting and Revoking Authorities**: Administrators can grant or revoke individual permissions or authorities from users, controlling their access to certain features.

### 3. **Authorization Control**
- **Centralized Role-Based Access Control (RBAC)**: Manages role-based access control (RBAC) for all services, ensuring consistent and secure permission structures across the system.
- **Permission-Based Access Control (PBAC)**: Facilitates fine-grained access control using permission-based policies, allowing for dynamic authorization based on specific user attributes and actions.
  
### 4. **Caching Control**
- **Efficient Caching**: Manages any caching related to roles, permissions, or authorization data, ensuring fast and reliable access to identity-related information for connected services.
- **Cache Invalidation**: Controls cache invalidation policies, ensuring that updates to roles, permissions, and user data are reflected promptly across all services.

### 5. **Security**
- **JWT-Based Authentication**: While not responsible for generating JWTs, IAMC validates and enforces security policies for requests containing JWTs issued by an external authentication server.
- **Public Key Validation**: Verifies JWTs using public keys, ensuring that only authorized users have access to the system.

## Key Responsibilities
- **Identity Management**: Centralized handling of user accounts, including creation, updates, and maintaining associated roles and permissions.
- **Access Control**: Implements and enforces access control policies for users based on their roles and permissions.
- **Caching Management**: Ensures efficient data caching and cache invalidation for roles and permissions to ensure real-time consistency in authorization across services.

## Usage
The **Identity and Access Control Server (IAMC)** is designed to work as a central authority for managing access control within a service-oriented architecture (SOA) or microservices environment. It allows other services to delegate identity and access management tasks, ensuring consistent role-based and permission-based control across the system.
