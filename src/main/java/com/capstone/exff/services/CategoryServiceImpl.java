package com.capstone.exff.services;

import com.capstone.exff.entities.CategoryEntity;
import com.capstone.exff.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryServices {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }

    @Override
    public List<CategoryEntity> loadAllCategory() {
        return categoryRepository.findAll();
    }
}
