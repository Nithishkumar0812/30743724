Online Retail Order Management System

Project Description

The Online Retail Order Management System is a console-based application developed in Core Java with MySQL and JDBC. This application simulates an online retail management system, enabling users to manage products and orders efficiently. The system is built to showcase proficiency in Java, object-oriented programming, database interactions using JDBC, and exception handling.

Features

Product Management

Add New Products: Users can add new products to the database by specifying the product's name, description, price, and stock quantity.
View Product Details: Users can retrieve and view details of all the products available in the database.
Update Product Information: Users can update existing product information such as name, description, price, and stock quantity.
Remove Products: Users can remove a product from the database by specifying the product ID.

Customer Management

Add New Customers: Users can add new customers to the database by specifying their name, email, phone number, and address.
View Customer Details: Users can retrieve and view details of all customers.
Update Customer Information: Users can update existing customer information such as name, email, phone number, and address.
Remove Customers: Users can remove a customer from the database by specifying the customer ID.

Order Management

Place New Orders: Users can place a new order by selecting the customer and adding products to the order. The system automatically calculates the total price and updates the order status.
View Order Details: Users can view details of any order, including the products ordered, their quantities, and the total price.
Update Order Status: Users can update the status of an order (e.g., Processing, Shipped, Delivered, or Cancelled).
Cancel an Order: Users can cancel an order, and the system will update the order status to "Cancelled".

Database Schema

The system interacts with the following database tables:

Product Table
product_id (Primary Key)
name
description
price
stock_quantity

Customer Table
customer_id (Primary Key)
name
email
phone
address

Order Table
order_id (Primary Key)
customer_id (Foreign Key references Customer Table)
order_date
status (Processing, Shipped, Delivered, Cancelled)

OrderItems Table
order_item_id (Primary Key)
order_id (Foreign Key references Order Table)
product_id (Foreign Key references Product Table)
quantity
price

Installation and Setup

Prerequisites
Java Development Kit (JDK) 8 or higher
MySQL Server
A MySQL database named retail_order_management
MySQL JDBC Driver

Steps to Setup

Clone the Repository:

git clone https://github.com/yourusername/online-retail-order-management.git
cd online-retail-order-management

Create the Database:

CREATE DATABASE retail_order_management;
USE retail_order_management;
Create the Tables: Execute the SQL script provided in the database.sql file to create the necessary tables.

Configure the Database Connection:

Open DatabaseConnection.java in the com.cts package.
Update the URL, USER, and PASSWORD fields with your MySQL server credentials.
Compile and Run the Application:


javac -d bin src/com/cts/main/Main.java
java -cp bin:lib/mysql-connector-java-8.0.27.jar com.cts.main.Main

Usage

Main Menu: The application presents a menu for managing products and orders.
Product Management: Choose from options to add, view, update, or remove products.
Customer Management: Choose from options to add, view, update, or remove customers.
Order Management: Place a new order, view order details, update order status, or cancel an order.

Exception Handling

The application includes robust exception handling to manage database connection issues, invalid inputs, and other runtime errors, ensuring a smooth user experience.

Contributing
Contributions are welcome! Please fork this repository, make your changes, and submit a pull request.