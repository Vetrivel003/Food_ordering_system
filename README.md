# 🍽️ Sapaadu — Food Ordering System

> A backend-focused food ordering platform built with Spring Boot — featuring JWT auth, role-based access, multi-restaurant cart, and a Streamlit demo frontend.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=flat&logo=jsonwebtokens&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=flat&logo=swagger&logoColor=black)
![Streamlit](https://img.shields.io/badge/Streamlit-Frontend-FF4B4B?style=flat&logo=streamlit&logoColor=white)

---

## 📖 About

**Sapaadu** (சாப்பாடு — meaning "Food" in Tamil) is a production-style food ordering backend that demonstrates real-world engineering patterns. From JWT token rotation to multi-restaurant cart management, every layer is built with clean architecture and industry-standard security in mind.

This is not just a CRUD app — it's a complete system that showcases how food ordering platforms work under the hood.

---

## ⚙️ Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 17+ | Core language |
| Spring Boot | Application framework |
| Spring Security | Authentication & authorization |
| JWT (Access + Refresh) | Stateless session management |
| Hibernate / JPA | ORM layer |
| MySQL 8 | Persistent storage |

### Frontend (Demo)
| Technology | Purpose |
|---|---|
| Python | Frontend language |
| Streamlit | UI framework |
| Requests | REST API client |

### Tools
| Tool | Purpose |
|---|---|
| Git | Feature branch workflow |
| Swagger / OpenAPI | API documentation |
| Postman | API testing |

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────┐
│           Streamlit Frontend              │
│  Register │ Login │ Cart │ Orders │ Menu  │
└─────────────────┬────────────────────────┘
                  │ REST API (JSON)
┌─────────────────▼────────────────────────┐
│         Spring Boot REST API              │
│                                           │
│  AuthController   RestaurantController    │
│  CartController   OrderController         │
└─────────────────┬────────────────────────┘
                  │
┌─────────────────▼────────────────────────┐
│       Spring Security (JWT Filter)        │
│  Access Token Validation                  │
│  Role-Based Access Control                │
│  Refresh Token Rotation                   │
└─────────────────┬────────────────────────┘
                  │
┌─────────────────▼────────────────────────┐
│              MySQL Database               │
│                                           │
│  users    restaurants    menu_items       │
│  carts    cart_items     orders           │
│  order_items   refresh_tokens             │
└──────────────────────────────────────────┘
```

---

## 🔐 Security Features

| Feature | Status |
|---|---|
| JWT-based authentication | ✅ |
| Access + Refresh token mechanism | ✅ |
| Refresh token rotation | ✅ |
| Logout with token revocation | ✅ |
| Role-based access control (USER / ADMIN) | ✅ |
| Stateless session management | ✅ |
| Secure API endpoints via Spring Security | ✅ |

---

## 🛒 Core Features

### 👤 Authentication
- User registration with validation
- Login — returns JWT access + refresh token pair
- Secure logout — refresh token blacklisted
- Token refresh — rotate without re-login

### 🍴 Restaurants
- View all approved restaurants
- Browse menu items per restaurant

### 🛍️ Cart System
- Add items to cart
- Multi-restaurant cart support
- Grouped cart view by restaurant
- Update / remove cart items

### 💳 Checkout
- Preview full order before confirming
- Confirm order per restaurant
- Clear cart after successful order

### 📦 Orders
- View complete order history
- View detailed order items per order

---

## 🔄 Complete User Flow

```
 Register
    ↓
 Login → JWT tokens issued
    ↓
 View Restaurants
    ↓
 Browse Menu Items
    ↓
 Add to Cart (multi-restaurant support)
    ↓
 Preview Checkout
    ↓
 Confirm Order
    ↓
 View Order History
    ↓
 Logout → token revoked
```

---

## 📡 API Reference

### Auth Endpoints — Public

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Register new user |
| `POST` | `/api/auth/login` | Login — returns JWT pair |
| `POST` | `/api/auth/refresh` | Rotate refresh token |
| `POST` | `/api/auth/logout` | Revoke refresh token |

### Restaurant Endpoints — USER Role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/restaurants` | List all approved restaurants |
| `GET` | `/api/restaurants/{id}/menu` | View menu for a restaurant |

### Cart Endpoints — USER Role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/cart/add` | Add item to cart |
| `GET` | `/api/cart` | View current cart (grouped) |
| `PUT` | `/api/cart/update` | Update cart item quantity |
| `DELETE` | `/api/cart/remove/{itemId}` | Remove item from cart |

### Order Endpoints — USER Role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/orders/preview` | Preview checkout before confirming |
| `POST` | `/api/orders/confirm` | Confirm order |
| `GET` | `/api/orders` | View order history |
| `GET` | `/api/orders/{id}` | View detailed order items |

### Admin Endpoints — ADMIN Role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/admin/restaurants/approve` | Approve a restaurant |
| `GET` | `/api/admin/orders` | View all orders |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+
- Python 3.9+ (for Streamlit)

### 1. Clone the repository

```bash
git clone https://github.com/your-username/sapaadu.git
cd sapaadu
```

### 2. Setup MySQL

```sql
CREATE DATABASE sapaadu_db;
```

Then run the schema:

```bash
mysql -u root -p sapaadu_db < src/main/resources/schema.sql
```

### 3. Configure `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sapaadu_db
spring.datasource.username=root
spring.datasource.password=yourpassword

jwt.secret=your-256-bit-secret-key
jwt.access-token-expiry=900000
jwt.refresh-token-expiry=604800000
```

### 4. Run the backend

```bash
mvn spring-boot:run
```

Backend starts at `http://localhost:8080`

### 5. View API docs

```
http://localhost:8080/swagger-ui/index.html
```

### 6. Run Streamlit frontend

```bash
cd frontend
pip install streamlit requests
streamlit run app.py
```

Frontend starts at `http://localhost:8501`

---

## 🧪 Sample Credentials

Register a new user or use the demo credentials:

```
Email:    test@example.com
Password: password
```

---

## 🧪 Postman Testing Order

```
1. POST /api/auth/register     → Create account
2. POST /api/auth/login        → Get JWT tokens
3. GET  /api/restaurants       → Browse restaurants
4. GET  /api/restaurants/{id}/menu → View menu
5. POST /api/cart/add          → Add items to cart
6. GET  /api/cart              → Review cart
7. GET  /api/orders/preview    → Preview checkout
8. POST /api/orders/confirm    → Place order
9. GET  /api/orders            → View order history
10. POST /api/auth/logout      → Logout
```

---

## 🏆 Key Highlights

- **Real-world backend architecture** — clean separation of concerns across controller, service, and repository layers
- **Production-like security** — JWT rotation, token blacklisting, role-based guards
- **Multi-restaurant cart** — handles items from different restaurants in a single session
- **Checkout flow** — preview before confirm, not just one-click order
- **Complete API documentation** — Swagger UI for every endpoint
- **End-to-end demo** — Streamlit frontend connects to live backend, not mocked

---

## 🔮 Future Enhancements

- [ ] Pagination & filtering for restaurants and orders
- [ ] Payment gateway integration (Razorpay / Stripe)
- [ ] Admin dashboard with analytics
- [ ] Real-time order tracking
- [ ] Push notifications
- [ ] CI/CD pipeline with GitHub Actions
- [ ] Docker containerization

---

## 👨‍💻 Author

**Vetrivel A**
B.Tech — AI & Data Science
Sri Eshwar College of Engineering, 2026

Aspiring Backend Engineer 🚀

<div align="center">
  Built with ❤️ using Spring Boot + MySQL + Streamlit
  <br/>
  <i>சாப்பாடு — Because good food deserves good code 🍛</i>
</div>
