package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

public class ShoppingCartItem
{
    private Product product;
    private int quantity;

    public ShoppingCartItem()
    {
    }

    public ShoppingCartItem(Product product, int quantity)
    {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }


    public int getProductId()
    {
        return product != null ? product.getProductId() : 0;
    }

    public BigDecimal getUnitPrice()
    {
        return product != null ? product.getPrice() : BigDecimal.ZERO;
    }

    public BigDecimal getLineTotal()
    {
        if (product == null)
            return BigDecimal.ZERO;
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}