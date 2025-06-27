package org.yearup.data.mysql;

import org.yearup.data.OrdersDao;
import org.yearup.models.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MySqlOrdersDao extends MySqlDaoBase implements OrdersDao
{
    public MySqlOrdersDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile getByUserId(int userId) {
        return null;
    }

    @Override
    public Order createOrder(int userId, ShoppingCart cart)
    {
        String orderSql = "INSERT INTO orders (user_id, order_date, total_amount) VALUES (?, ?, ?)";
        String orderItemSql = "INSERT INTO order_line_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;
        ResultSet rs = null;

        try
        {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Insert the order
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            orderStmt.setBigDecimal(3, cart.getTotal());
            orderStmt.executeUpdate();

            rs = orderStmt.getGeneratedKeys();
            int orderId = 0;
            if (rs.next())
            {
                orderId = rs.getInt(1);
            }
            else
            {
                conn.rollback();
                throw new SQLException("Failed to insert order, no ID returned.");
            }

            // Insert the order line items
            itemStmt = conn.prepareStatement(orderItemSql);
            for (ShoppingCartItem item : cart.getItemList())
            {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProduct().getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setBigDecimal(4, item.getProduct().getPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            conn.commit();

            // Build and return the Order object
            Order order = new Order();
            order.setOrderId(orderId);
            order.setUserId(userId);
            order.setOrderDate(LocalDateTime.now());
            order.setTotal(cart.getTotal());

            List<OrderLineItem> orderItems = new ArrayList<>();
            for (ShoppingCartItem item : cart.getItemList())
            {
                OrderLineItem oli = new OrderLineItem();
                oli.setOrderId(orderId);
                oli.setProductId(item.getProduct().getProductId());
                oli.setQuantity(item.getQuantity());
                oli.setUnitPrice(item.getProduct().getPrice());
                orderItems.add(oli);
            }
            order.setLineItems(orderItems);

            return order;
        }
        catch (SQLException e)
        {
            if (conn != null)
            {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException(e);
        }
        finally
        {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (orderStmt != null) orderStmt.close(); } catch (SQLException ignored) {}
            try { if (itemStmt != null) itemStmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }
}