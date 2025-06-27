package org.yearup.controllers;

import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController
{
    @Autowired
    private CategoryDao categoryDao;

    @GetMapping
    public List<Category> getAllCategories()
    {
        return categoryDao.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable int id)
    {
        return categoryDao.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Category addCategory(@RequestBody Category category)
    {
        return categoryDao.create(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        categoryDao.update(id, category);
        return category;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
    }
}