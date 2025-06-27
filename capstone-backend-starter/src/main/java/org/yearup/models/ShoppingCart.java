package org.yearup.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class ShoppingCart extends Profile {
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

    public ShoppingCart()
    {
    }

    public Map<Integer, ShoppingCartItem> getItems()
    {
        return items;
    }

    // Add this:
    public Collection<ShoppingCartItem> getItemList()
    {
        return items.values();
    }

    public void setItems(Map<Integer, ShoppingCartItem> items)
    {
        this.items = items;
    }

    public void addItem(int productId, ShoppingCartItem item)
    {
        items.put(productId, item);
    }

    public void removeItem(int productId)
    {
        items.remove(productId);
    }

    public BigDecimal getTotal()
    {
        BigDecimal total = BigDecimal.ZERO;
        for (ShoppingCartItem item : items.values())
        {
            total = total.add(item.getLineTotal());
        }
        return total;
    }
}