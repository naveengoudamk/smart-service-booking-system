# 🚀 Smart Service Booking System

A full-stack **service booking platform** built with **Java, Spring Boot, Spring MVC, Spring Data JPA, Hibernate, and MySQL**.

The application allows customers to discover home services, create bookings, and manage their booking history. It also provides administrative functionality for managing users, services, and bookings.

> 🎯 The project is designed to simulate a real-world home-service platform similar to platforms such as Urban Company.

---

## 📌 Overview

The **Smart Service Booking System** provides a centralized platform where customers can book home services such as:

- 🔧 Electrician
- 🚰 Plumber
- 🧹 Cleaning
- 🛠️ Home Maintenance
- 🔨 Other household services

The system follows a layered backend architecture using Spring Boot and exposes REST APIs that can be consumed by a web frontend or tools such as Postman.

---

# ✨ Key Features

## 👤 Customer

- User registration
- User login
- Password encryption using BCrypt
- View available services
- Book a service
- View booking history
- Manage personal bookings

## 🧑‍💼 Admin

- Manage users
- Add services
- Remove services
- View available services
- View all bookings
- Manage the overall service-booking system

## 🛠️ Service Provider

The service-provider module is planned for future implementation.

Planned functionality:

- View assigned bookings
- Accept or reject bookings
- Update service status
- Manage service availability

---

# 🏗️ Application Architecture

The backend follows a layered architecture:

```text
                    ┌──────────────────────┐
                    │      Frontend        │
                    │ HTML / CSS / JS      │
                    └──────────┬───────────┘
                               │
                               │ REST API
                               ▼
                    ┌──────────────────────┐
                    │    Controller Layer  │
                    │   Spring REST API    │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │     Service Layer    │
                    │ Business Logic       │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Repository Layer   │
                    │ Spring Data JPA      │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │       MySQL          │
                    │      Database        │
                    └──────────────────────┘
