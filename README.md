# 🍽️ Canteenly

<div align="center">

### Modern Food Ordering Application for Campus Canteens

Built with **Kotlin**, **Jetpack Compose**, and **Firebase**

<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
<img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
<img src="https://img.shields.io/badge/Android-34A853?style=for-the-badge&logo=android&logoColor=white" />

</div>

---

## 📖 About

Canteenly is a modern Android-based food ordering application designed to simplify food ordering experiences within campus environments.

Users can browse canteens, discover menu items, customize orders, manage carts, complete checkout processes, and track order statuses through a clean and intuitive interface.

---

## ✨ Features

### 🔐 Authentication
- Firebase Authentication
- User Registration
- User Login
- Profile Management

### 🍔 Food Ordering
- Browse Available Canteens
- Explore Menus
- Best Seller Recommendations
- Category Filtering

### 🛒 Cart Management
- Add To Cart
- Update Item Quantity
- Remove Items
- Multi-Canteen Support
- Real-Time Cart Summary

### 📦 Checkout
- Delivery Address Selection
- Delivery Method Selection
- Payment Method Selection
- Tax Calculation
- Delivery Fee Calculation
- Order Summary

### 🚚 Order Tracking
- Active Orders
- Order History
- Order Status Tracking
- Estimated Delivery Time

### 👤 Account Management
- Edit Profile
- Change Address
- View Orders
- Logout

---

## 🏗️ Tech Stack

| Category | Technology |
|-----------|------------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | Repository Pattern |
| Database | Firebase Realtime Database |
| Authentication | Firebase Authentication |
| Image Loading | Coil |
| Navigation | Navigation Compose |

---

## 📂 Project Structure

```text
com.example.canteenlyapp
│
├── data
│   ├── model
│   ├── repository
│   └── dummy
│
├── ui
│   ├── components
│   ├── navigation
│   ├── screen
│   └── theme
│
├── utils
│
└── MainActivity.kt
```

---

## 🔥 Firebase Structure

```text
users
├── userId
│   ├── fullName
│   ├── email
│   ├── address
│   └── photoUrl

canteens
├── canteenId
│   ├── name
│   ├── imageKey
│   └── deliveryFee

menus
├── menuId
│   ├── name
│   ├── price
│   ├── imageKey
│   └── category

orders
├── orderId
│   ├── userId
│   ├── items
│   ├── subtotal
│   ├── deliveryFee
│   ├── tax
│   ├── total
│   ├── status
│   └── createdAt
```

---

## 📱 Application Screens

| Screen | Description |
|----------|------------|
| Splash | Application startup screen |
| Login | User authentication |
| Register | User registration |
| Home | Browse canteens and menus |
| Canteen Detail | View canteen menus |
| Cart | Manage selected items |
| Checkout | Complete order process |
| Order Success | Order confirmation |
| Orders | Active and historical orders |
| Account | User profile management |

---

## 📸 Application Preview

### Home Screen

<img src="screenshots/home.png" width="250"/>

### Cart Screen

<img src="screenshots/cart.png" width="250"/>

### Checkout Screen

<img src="screenshots/checkout.png" width="250"/>

### Orders Screen

<img src="screenshots/orders.png" width="250"/>

### Account Screen

<img src="screenshots/account.png" width="250"/>

---

## 🚀 Installation

### Clone Repository

```bash
git clone https://github.com/your-username/canteenly.git
```

### Open Project

Open the project using Android Studio.

### Configure Firebase

1. Create Firebase Project
2. Add Android Application
3. Download `google-services.json`
4. Place it inside:

```text
app/google-services.json
```

### Run Application

```bash
./gradlew assembleDebug
```

or simply click **Run ▶** from Android Studio.

---

## 👨‍💻 Developer

Developed using Kotlin, Jetpack Compose, and Firebase as a modern Android mobile application.

---

<div align="center">

### Made with using Kotlin & Jetpack Compose

</div>
