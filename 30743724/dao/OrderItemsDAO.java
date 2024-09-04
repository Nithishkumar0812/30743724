package com.dao;

import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.exception.OrderNotFoundException;
import com.model.OrderItem;
import com.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsDAO {

    // Method to add a new order item
    public void addOrderItem(OrderItem orderItem) throws DatabaseException, InvalidInputException {
        // Validate input
        if (orderItem.getOrderId() <= 0 || orderItem.getProductId() <= 0 || orderItem.getQuantity() <= 0 || orderItem.getPrice() < 0) {
            throw new InvalidInputException("Invalid input: orderId, productId, quantity, and price must be positive values.");
        }

        String query = "INSERT INTO OrderItems (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getPrice());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding order item.", e);
        }
    }

    // Method to get order items by order ID
    public List<OrderItem> getOrderItemsByOrderId(int orderId) throws DatabaseException, OrderNotFoundException, InvalidInputException {
        if (orderId <= 0) {
            throw new InvalidInputException("Invalid order ID.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM OrderItems WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderItemId(resultSet.getInt("order_item_id"));
                orderItem.setOrderId(resultSet.getInt("order_id"));
                orderItem.setProductId(resultSet.getInt("product_id"));
                orderItem.setQuantity(resultSet.getInt("quantity"));
                orderItem.setPrice(resultSet.getDouble("price"));

                orderItems.add(orderItem);
            }

            if (!hasResults) {
                throw new OrderNotFoundException("No order items found for order ID: " + orderId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving order items.", e);
        }

        return orderItems;
    }

    // Method to update an order item
    public void updateOrderItem(OrderItem orderItem) throws DatabaseException, InvalidInputException, OrderNotFoundException {
        if (orderItem.getOrderItemId() <= 0 || orderItem.getProductId() <= 0 || orderItem.getQuantity() <= 0 || orderItem.getPrice() < 0) {
            throw new InvalidInputException("Invalid input: orderItemId, productId, quantity, and price must be positive values.");
        }

        String query = "UPDATE OrderItems SET product_id = ?, quantity = ?, price = ? WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderItem.getProductId());
            statement.setInt(2, orderItem.getQuantity());
            statement.setDouble(3, orderItem.getPrice());
            statement.setInt(4, orderItem.getOrderItemId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new OrderNotFoundException("Order item not found with ID: " + orderItem.getOrderItemId());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error updating order item.", e);
        }
    }

    // Method to delete an order item by ID
    public void deleteOrderItem(int orderItemId) throws DatabaseException, InvalidInputException, OrderNotFoundException {
        if (orderItemId <= 0) {
            throw new InvalidInputException("Invalid order item ID.");
        }

        String query = "DELETE FROM OrderItems WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderItemId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new OrderNotFoundException("Order item not found with ID: " + orderItemId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting order item.", e);
        }
    }
}
