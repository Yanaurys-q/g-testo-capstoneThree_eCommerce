package org.yearup.data;

import org.yearup.models.Category;
import java.util.List;

public interface CategoriesDao
{
    List<Category> findAll();
    Category findById(int id);
    void create(Category category);
    void update(Category category);
    void delete(int id);
}