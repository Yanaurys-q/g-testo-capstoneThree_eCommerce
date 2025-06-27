package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;

public interface OrdersDao
{
    Order createOrder(int userId, ShoppingCart cart);

}