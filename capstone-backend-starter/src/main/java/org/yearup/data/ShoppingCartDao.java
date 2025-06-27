package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getCartForUser(int userId);
    ShoppingCart getByUserId(int userId);
    void addProduct(int userId, int productId);
    void updateProductQuantity(int userId, int productId, int quantity);
    void clearCart(int userId);

}
