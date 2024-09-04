package com.service;

import com.dao.CustomerDAO;
import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.model.Customer;

public class CustomerService {

    private final CustomerDAO customerDAO = new CustomerDAO();

    public void addCustomer(Customer customer) throws DatabaseException, InvalidInputException {
        customerDAO.addCustomer(customer);
    }

    public Customer getCustomerById(int customerId) throws DatabaseException, InvalidInputException {
        return customerDAO.getCustomerById(customerId);
    }

    public void updateCustomer(Customer customer) throws DatabaseException, InvalidInputException {
        customerDAO.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId) throws DatabaseException, InvalidInputException {
        customerDAO.deleteCustomer(customerId);
    }
}
