package com.dao;

import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.model.Customer;
import com.util.DBConnection;

import java.sql.*;

public class CustomerDAO {

    public void addCustomer(Customer customer) throws DatabaseException, InvalidInputException {
        // Input validation example
        if (customer.getName() == null || customer.getEmail() == null || customer.getPhone() == null) {
            throw new InvalidInputException("Customer name, email, and phone cannot be null.");
        }

        String query = "INSERT INTO Customer (name, email, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getAddress());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                customer.setCustomerId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding customer", e);
        }
    }

    public Customer getCustomerById(int customerId) throws DatabaseException, InvalidInputException {
        if (customerId <= 0) {
            throw new InvalidInputException("Invalid customer ID.");
        }

        String query = "SELECT * FROM Customer WHERE customer_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToCustomer(resultSet);
            } else {
                return null; // Consider handling the case where customer is not found
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving customer", e);
        }
    }

    public void updateCustomer(Customer customer) throws DatabaseException, InvalidInputException {
        if (customer.getCustomerId() <= 0 || customer.getName() == null || customer.getEmail() == null || customer.getPhone() == null) {
            throw new InvalidInputException("Invalid customer data for update.");
        }

        String query = "UPDATE Customer SET name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getAddress());
            statement.setInt(5, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating customer", e);
        }
    }

    public void deleteCustomer(int customerId) throws DatabaseException, InvalidInputException {
        if (customerId <= 0) {
            throw new InvalidInputException("Invalid customer ID.");
        }

        String query = "DELETE FROM Customer WHERE customer_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting customer", e);
        }
    }

    private Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setName(resultSet.getString("name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setAddress(resultSet.getString("address"));
        return customer;
    }
}
