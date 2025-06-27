package org.yearup.data.mysql;

import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MySqlCategoryDao implements CategoryDao
{
    private final JdbcTemplate jdbcTemplate;

    public MySqlCategoryDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Category> categoryMapper = new RowMapper<Category>()
    {
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Category category = new Category();
            category.setCategoryId(rs.getInt("category_id"));
            category.setName(rs.getString("name"));
            category.setDescription(rs.getString("description"));
            return category;
        }
    };

    @Override
    public List<Category> findAll()
    {
        return jdbcTemplate.query("SELECT * FROM categories", categoryMapper);
    }

    @Override
    public Category getById(int categoryId)
    {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM categories WHERE category_id = ?", categoryMapper, categoryId);
    }

    @Override
    public Category create(Category category)
    {
        jdbcTemplate.update(
                "INSERT INTO categories (name, description) VALUES (?, ?)",
                category.getName(), category.getDescription());
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        jdbcTemplate.update(
                "UPDATE categories SET name=?, description=? WHERE category_id=?",
                category.getName(), category.getDescription(), categoryId);
    }

    @Override
    public void delete(int categoryId)
    {
        jdbcTemplate.update("DELETE FROM categories WHERE category_id=?", categoryId);
    }
}