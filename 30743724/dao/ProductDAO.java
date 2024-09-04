package com.dao;

import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.exception.ProductNotFoundException;
import com.model.Product;
import com.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Method to add a new product
    public void addProduct(Product product) throws DatabaseException, InvalidInputException {
        // Validate input
        if (product.getName() == null || product.getName().trim().isEmpty() ||
                product.getPrice() < 0 || product.getStockQuantity() < 0) {
            throw new InvalidInputException("Invalid input: name must not be empty and price and stock_quantity must be non-negative.");
        }

        String query = "INSERT INTO Product (name, description, price, stock_quantity) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding product.", e);
        }
    }

    // Method to get a product by ID
    public Product getProductById(int productId) throws DatabaseException, ProductNotFoundException, InvalidInputException {
        if (productId <= 0) {
            throw new InvalidInputException("Invalid product ID.");
        }

        String query = "SELECT * FROM Product WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToProduct(resultSet);
            } else {
                throw new ProductNotFoundException("Product not found with ID: " + productId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving product.", e);
        }
    }

    // Method to get all products
    public List<Product> getAllProducts() throws DatabaseException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving products.", e);
        }

        return products;
    }

    // Method to update a product
    public void updateProduct(Product product) throws DatabaseException, InvalidInputException, ProductNotFoundException {
        if (product.getProductId() <= 0 || product.getName() == null || product.getName().trim().isEmpty() ||
                product.getPrice() < 0 || product.getStockQuantity() < 0) {
            throw new InvalidInputException("Invalid input: productId must be positive and name must not be empty. Price and stock_quantity must be non-negative.");
        }

        String query = "UPDATE Product SET name = ?, description = ?, price = ?, stock_quantity = ? WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setInt(5, product.getProductId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ProductNotFoundException("Product not found with ID: " + product.getProductId());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error updating product.", e);
        }
    }

    // Method to delete a product by ID
    public void deleteProduct(int productId) throws DatabaseException, InvalidInputException, ProductNotFoundException {
        if (productId <= 0) {
            throw new InvalidInputException("Invalid product ID.");
        }

        String query = "DELETE FROM Product WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ProductNotFoundException("Product not found with ID: " + productId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting product.", e);
        }
    }

    private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setProductId(resultSet.getInt("product_id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getDouble("price"));
        product.setStockQuantity(resultSet.getInt("stock_quantity"));
        return product;
    }
}
