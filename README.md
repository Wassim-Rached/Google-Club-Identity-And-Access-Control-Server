# Google Club Unified Access System (UAS) - Identity Control Service (ICS)

## Overview

The **Identity Control Service (ICS)** is a core component of the Google Club Unified Access System (UAS), responsible for managing identity and access control across the Google Club ecosystem. ICS centralizes user profiles, permissions, and administrative control, enabling secure, cohesive access management for both users and administrators.

## Core Responsibilities

### Identity Management
- **User Profiles**: Manages user data, including account information and personal settings.
- **Permissions**: Configures and maintains permissions across applications, ensuring that users have appropriate access.
- **Administrative Controls**: Enables administrators to manage users, roles, and permissions from a centralized interface.

### Access Management
- **Authority Verification**: Verifies permissions for integrated applications, maintaining consistent access control.
- **Access Configuration**: Provides flexible controls for managing user roles and permissions based on application requirements.

## Technologies

ICS is developed with **Spring Boot**, a robust framework designed for building production-ready, stand-alone applications. Its powerful backend capabilities make it ideal for handling identity management and secure access controls within the UAS.

## API Endpoints

### General Purpose
- `GET /health-check`: Performs a health check for the ICS, ensuring the service is operational.

### Account Management
- `POST /create-account`: Creates a new user account within the UAS.
- `POST /request-reset-password`: Initiates a password reset process for a user.
- `POST /reset-password-with-token`: Resets a password using a verification token.
- `POST /request-email-verification`: Sends an email verification request.
- `GET /verify-email-with-token`: Verifies an email address using a token.
- `GET /get-my-account`: Retrieves the logged-in user's account details.
- `POST /change-my-password`: Allows users to change their password.
- `PUT /update-my-account`: Updates account details for the logged-in user.
- `DELETE /delete-my-account`: Deletes the user’s account.

### Accounts Console Management
- `GET /search-accounts`: Searches for user accounts based on specified criteria.
- `GET /get-account-by-id`: Retrieves account details by user ID.
- `POST /edit-authorities`: Edits an account's authorities and permissions.
- `POST /toggle-verify-identity`: Toggles identity verification status.
- `POST /toggle-membership`: Changes membership status.
- `POST /lock-account`: Locks a user’s account, restricting access.

### Role Management
- `GET /search-roles`: Searches for roles within the system.
- `POST /create-role`: Creates a new role.
- `GET /get-role-by-id`: Retrieves details of a specific role by ID.
- `POST /edit-role`: Edits an existing role.
- `DELETE /delete-role`: Deletes a specified role.

### Permission Management
- `GET /search-permissions`: Searches for permissions available in the system.
- `POST /create-permission`: Creates a new permission.
- `GET /get-permission-by-id`: Retrieves permission details by ID.
- `DELETE /delete-permission`: Deletes a specified permission.

## System Communication

ICS interacts seamlessly with other components in the UAS, including the **Central Authentication Service (CAS)** and the **Accounts Database**. This collaboration enables ICS to provide real-time access control, user management, and a unified identity framework for Google Club services.

## How ICS Fits into the Google Club Unified Access System

ICS serves as the identity and access management backbone of the UAS:
- Consolidates user profiles, permissions, and administrative control across all Google Club applications.
- Streamlines access management, enhancing security and reducing administrative complexity.
- Supports consistent identity management and access control for improved user experience and scalability.

## Current Maintainer

- **Wassim Rached** is the primary developer and maintainer of the ICS component within the Google Club UAS.

---

The **Identity Control Service (ICS)** provides comprehensive identity and access management for Google Club services, ensuring secure, centralized control of user profiles and permissions within the UAS ecosystem.
