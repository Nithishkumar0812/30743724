package com.service;

import com.dao.OrderDAO;
import com.dao.OrderItemsDAO;
import com.exception.DatabaseException;
import com.exception.InvalidInputException;
import com.exception.OrderNotFoundException;
import com.model.Order;
import com.model.OrderItem;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemsDAO orderItemsDAO = new OrderItemsDAO();

    public void addOrder(Order order) throws DatabaseException, InvalidInputException {
        orderDAO.addOrder(order);
    }

    public Order getOrderById(int orderId) throws DatabaseException, OrderNotFoundException, InvalidInputException {
        return orderDAO.getOrderById(orderId);
    }

    public List<Order> getAllOrders() throws DatabaseException {
        return orderDAO.getAllOrders();
    }

    public void updateOrder(Order order) throws DatabaseException, InvalidInputException {
        orderDAO.updateOrder(order);
    }

    public void deleteOrder(int orderId) throws DatabaseException, OrderNotFoundException, InvalidInputException {
        orderDAO.deleteOrder(orderId);
    }

    public void addOrderItem(OrderItem orderItem) throws DatabaseException, InvalidInputException {
        orderItemsDAO.addOrderItem(orderItem);
    }

    public void deleteOrderItem(int orderItemId) throws DatabaseException, InvalidInputException, OrderNotFoundException {
        orderItemsDAO.deleteOrderItem(orderItemId);
    }

    // Method to get order items by order ID
    public List<OrderItem> getOrderItemsByOrderId(int orderId) throws DatabaseException, InvalidInputException, OrderNotFoundException {
        return orderItemsDAO.getOrderItemsByOrderId(orderId);
    }
}
