package com.dao;

import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.exception.OrderNotFoundException;
import com.model.Order;
import com.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public void addOrder(Order order) throws DatabaseException, InvalidInputException {
        // Validate input
        if (order.getCustomerId() <= 0 || order.getOrderDate() == null || order.getStatus() == null) {
            throw new InvalidInputException("Invalid input: customerId, orderDate, and status are required.");
        }

        String query = "INSERT INTO OrderTable (customer_id, order_date, status) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getCustomerId());
            statement.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            statement.setString(3, order.getStatus());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding order", e);
        }
    }

    public Order getOrderById(int orderId) throws DatabaseException, OrderNotFoundException, InvalidInputException {
        if (orderId <= 0) {
            throw new InvalidInputException("Invalid order ID.");
        }

        String query = "SELECT * FROM OrderTable WHERE order_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToOrder(resultSet);
            } else {
                throw new OrderNotFoundException("Order not found with ID: " + orderId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving order", e);
        }
    }

    public List<Order> getAllOrders() throws DatabaseException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM OrderTable";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                orders.add(mapResultSetToOrder(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving orders", e);
        }
        return orders;
    }

    public void updateOrder(Order order) throws DatabaseException, InvalidInputException {
        if (order.getOrderId() <= 0 || order.getCustomerId() <= 0 || order.getOrderDate() == null || order.getStatus() == null) {
            throw new InvalidInputException("Invalid input: orderId, customerId, orderDate, and status are required.");
        }

        String query = "UPDATE OrderTable SET customer_id = ?, order_date = ?, status = ? WHERE order_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order.getCustomerId());
            statement.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            statement.setString(3, order.getStatus());
            statement.setInt(4, order.getOrderId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new OrderNotFoundException("Order not found with ID: " + order.getOrderId());
            }
        } catch (SQLException | OrderNotFoundException e) {
            throw new DatabaseException("Error updating order", e);
        }
    }

    public void deleteOrder(int orderId) throws DatabaseException, InvalidInputException, OrderNotFoundException {
        if (orderId <= 0) {
            throw new InvalidInputException("Invalid order ID.");
        }

        String query = "DELETE FROM OrderTable WHERE order_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new OrderNotFoundException("Order not found with ID: " + orderId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting order", e);
        }
    }

    private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getInt("order_id"));
        order.setCustomerId(resultSet.getInt("customer_id"));
        order.setOrderDate(resultSet.getDate("order_date"));
        order.setStatus(resultSet.getString("status"));
        return order;
    }
}
