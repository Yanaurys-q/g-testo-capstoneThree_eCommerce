# EasyShop E-Commerce API 

## Project Description

This project is an enhanced backend API for EasyShop, an online e-commerce store, developed using Java with Spring Boot and MySQL. The API manages product information, categories, user authentication, and shopping cart functionalities, providing robust support for EasyShop's frontend operations.

### Features Implemented

* User Authentication

* User registration and login with JWT token authentication.

* Product Management

- Display products by category

* Search and filter products by category, price range, and color

* Admin functionality to add, update, and delete products

* Category Management

* CRUD operations for product categories

* Secured access (Admins only)

  #### Bug Fixes

- Corrected product duplication issue during updates

- Fixed inaccuracies in product search results through refined query logic

API Endpoints
Authentication
Register: POST /register

json
Copy
Edit
{
"username": "admin",
"password": "password",
"confirmPassword": "password",
"role": "ADMIN"
}
Login: POST /login

json
Copy
Edit
{
"username": "admin",
"password": "password"
}
Returns a JWT token, which must be included as a Bearer token in subsequent requests.


### Interesting Code Snippet
    public List<Product> searchProducts(Integer cat, BigDecimal minPrice, BigDecimal maxPrice, String color) {
    StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1 ");
    List<Object> params = new ArrayList<>();
    if (cat != null) { sql.append("AND category_id = ? "); params.add(cat); }
    if (minPrice != null) { sql.append("AND price >= ? "); params.add(minPrice); }
    if (maxPrice != null) { sql.append("AND price <= ? "); params.add(maxPrice); }
    if (color != null && !color.isBlank()) { sql.append("AND color = ? "); params.add(color); }
    return jdbcTemplate.query(sql.toString(), params.toArray(), productRowMapper);
}

