package com;

import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.exception.OrderNotFoundException;
import com.exception.ProductNotFoundException;
import com.model.Customer;
import com.model.Order;
import com.model.OrderItem;
import com.model.Product;
import com.service.CustomerService;
import com.service.OrderService;
import com.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final ProductService productService = new ProductService();
    private static final OrderService orderService = new OrderService();
    private static final CustomerService customerService = new CustomerService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            try {
                showMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        manageProducts();
                        break;
                    case 2:
                        manageOrders();
                        break;
                    case 3:
                        manageCustomers();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (DatabaseException | InvalidInputException | OrderNotFoundException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (ProductNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void showMenu() {
        System.out.println("Online Retail Order Management System");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Orders");
        System.out.println("3. Manage Customers");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void manageProducts() throws DatabaseException, InvalidInputException, ProductNotFoundException {
        while (true) {
            System.out.println("Product Management");
            System.out.println("1. Add Product");
            System.out.println("2. View Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageOrders() throws DatabaseException, InvalidInputException, OrderNotFoundException, ProductNotFoundException {
        while (true) {
            System.out.println("Order Management");
            System.out.println("1. Place Order");
            System.out.println("2. View Order");
            System.out.println("3. Update Order Status");
            System.out.println("4. Cancel Order");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    placeOrder();
                    break;
                case 2:
                    viewOrder();
                    break;
                case 3:
                    updateOrderStatus();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageCustomers() throws DatabaseException, InvalidInputException {
        while (true) {
            System.out.println("Customer Management");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customer");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewCustomer();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct() throws DatabaseException, InvalidInputException {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product description: ");
        String description = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter stock quantity: ");
        int stockQuantity = Integer.parseInt(scanner.nextLine());

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);

        productService.addProduct(product);
        System.out.println("Product added successfully.");
    }

    private static void viewProduct() throws DatabaseException, InvalidInputException, ProductNotFoundException {
        System.out.print("Enter product ID: ");
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productService.getProductById(productId);
        if (product != null) {
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Stock Quantity: " + product.getStockQuantity());
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void updateProduct() throws DatabaseException, InvalidInputException, ProductNotFoundException {
        System.out.print("Enter product ID to update: ");
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productService.getProductById(productId);
        if (product != null) {
            System.out.print("Enter new name: ");
            product.setName(scanner.nextLine());
            System.out.print("Enter new description: ");
            product.setDescription(scanner.nextLine());
            System.out.print("Enter new price: ");
            product.setPrice(Double.parseDouble(scanner.nextLine()));
            System.out.print("Enter new stock quantity: ");
            product.setStockQuantity(Integer.parseInt(scanner.nextLine()));

            productService.updateProduct(product);
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void deleteProduct() throws DatabaseException, InvalidInputException, ProductNotFoundException {
        System.out.print("Enter product ID to delete: ");
        int productId = Integer.parseInt(scanner.nextLine());
        productService.deleteProduct(productId);
        System.out.println("Product deleted successfully.");
    }

    private static void placeOrder() throws DatabaseException, InvalidInputException, ProductNotFoundException {
        System.out.print("Enter customer ID: ");
        int customerId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter order date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        java.sql.Date orderDate;
        try {
            orderDate = java.sql.Date.valueOf(dateString);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format.");
            return;
        }

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setOrderDate(orderDate);
        order.setStatus("Processing");

        orderService.addOrder(order);
        System.out.println("Order placed successfully with Order ID: " + order.getOrderId());

        System.out.print("Enter number of items: ");
        int numItems = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numItems; i++) {
            System.out.print("Enter product ID for item " + (i + 1) + ": ");
            int productId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            Product product = productService.getProductById(productId);
            if (product == null) {
                System.out.println("Product not found.");
                continue;
            }

            double price = product.getPrice();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setProductId(productId);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price);

            orderService.addOrderItem(orderItem);
            System.out.println("Item added to order.");
        }
    }

    private static void viewOrder() throws DatabaseException, InvalidInputException, OrderNotFoundException {
        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Customer ID: " + order.getCustomerId());
            System.out.println("Order Date: " + order.getOrderDate());
            System.out.println("Status: " + order.getStatus());

            List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);
            if (items != null && !items.isEmpty()) {
                for (OrderItem item : items) {
                    System.out.println("Product ID: " + item.getProductId());
                    System.out.println("Quantity: " + item.getQuantity());
                    System.out.println("Price: $" + item.getPrice());
                    System.out.println("----");
                }
            } else {
                System.out.println("No items found for this order.");
            }
        } else {
            System.out.println("Order not found.");
        }
    }

    private static void updateOrderStatus() throws DatabaseException, InvalidInputException, OrderNotFoundException {
        System.out.print("Enter order ID to update: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            System.out.print("Enter new status: ");
            String status = scanner.nextLine();
            order.setStatus(status);
            orderService.updateOrder(order);
            System.out.println("Order status updated successfully.");
        } else {
            System.out.println("Order not found.");
        }
    }

    private static void cancelOrder() throws DatabaseException, OrderNotFoundException, InvalidInputException {
        System.out.print("Enter order ID to cancel: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        orderService.deleteOrder(orderId);
        System.out.println("Order canceled successfully.");
    }

    private static void addCustomer() throws DatabaseException, InvalidInputException {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        System.out.print("Enter customer phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter customer address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phoneNumber);
        customer.setAddress(address);

        customerService.addCustomer(customer);
        System.out.println("Customer added successfully with ID: " + customer.getCustomerId());
    }

    private static void viewCustomer() throws DatabaseException, InvalidInputException {
        System.out.print("Enter customer ID: ");
        int customerId = Integer.parseInt(scanner.nextLine());
        Customer customer = customerService.getCustomerById(customerId);
        if (customer != null) {
            System.out.println("Customer ID: " + customer.getCustomerId());
            System.out.println("Name: " + customer.getName());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Phone Number: " + customer.getPhone());
            System.out.println("Address: " + customer.getAddress());
        } else {
            System.out.println("Customer not found.");
        }
    }

    private static void updateCustomer() throws DatabaseException, InvalidInputException {
        System.out.print("Enter customer ID to update: ");
        int customerId = Integer.parseInt(scanner.nextLine());
        Customer customer = customerService.getCustomerById(customerId);
        if (customer != null) {
            System.out.print("Enter new name: ");
            customer.setName(scanner.nextLine());
            System.out.print("Enter new email: ");
            customer.setEmail(scanner.nextLine());
            System.out.print("Enter new phone number: ");
            customer.setPhone(scanner.nextLine());
            System.out.print("Enter new address: ");
            customer.setAddress(scanner.nextLine());

            customerService.updateCustomer(customer);
            System.out.println("Customer updated successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    private static void deleteCustomer() throws DatabaseException, InvalidInputException {
        System.out.print("Enter customer ID to delete: ");
        int customerId = Integer.parseInt(scanner.nextLine());
        customerService.deleteCustomer(customerId);
        System.out.println("Customer deleted successfully.");
    }
}
