package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrdersDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class OrdersController
{
    private OrdersDao ordersDao;
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;

    @Autowired
    public OrdersController(OrdersDao ordersDao, ShoppingCartDao shoppingCartDao, UserDao userDao)
    {
        this.ordersDao = ordersDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    // POST
    @PostMapping
    public Order placeOrder(Principal principal)
    {
        try
        {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            ShoppingCart cart = shoppingCartDao.getCartForUser(userId);
            if (cart == null || cart.getItems().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty.");
            }

            Order newOrder = ordersDao.createOrder(userId, cart);

            shoppingCartDao.clearCart(userId);

            return newOrder;
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}