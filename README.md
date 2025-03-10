# Warehouse Management System

## Overview
This project implements a **Warehouse Management System** in **Java**, designed to manage customer orders, products, and inventory using a database. The system provides functionalities for adding, deleting, updating, and viewing clients, products, and orders through a graphical user interface (GUI).

---

## Features
- Add, update, and delete products
- Add, update, and delete clients
- Manage orders between clients and products
- Generate order invoices
- View product inventory
- Log activities in the system
- Input validation for data consistency
- Database connection using **JDBC**

---

## Technologies Used
- **Java**
- **Swing** (GUI)
- **MySQL** (Database)
- **JDBC** (Database connectivity)
- **DAO Pattern** (Data Access Object)
- **Reflection API**
- **Layered Architecture**

---

## How It Works
1. The application connects to a MySQL database.
2. Users can manage:
   - Clients
   - Products
   - Orders
3. The interface displays the current inventory and order information.
4. All operations are logged, and reports can be generated.
