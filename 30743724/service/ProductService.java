package com.service;
import com.dao.ProductDAO;
import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.exception.ProductNotFoundException;
import com.model.Product;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    public void addProduct(Product product) throws DatabaseException, InvalidInputException {
        productDAO.addProduct(product);
    }

    public Product getProductById(int productId) throws DatabaseException, InvalidInputException, ProductNotFoundException {
        return productDAO.getProductById(productId);
    }

    public List<Product> getAllProducts() throws DatabaseException {
        return productDAO.getAllProducts();
    }

    public void updateProduct(Product product) throws DatabaseException, InvalidInputException, ProductNotFoundException {
        productDAO.updateProduct(product);
    }

    public void deleteProduct(int productId) throws DatabaseException, InvalidInputException, ProductNotFoundException {
        productDAO.deleteProduct(productId);
    }
}

