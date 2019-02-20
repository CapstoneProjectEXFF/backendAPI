package com.capstone.exff.controllers;

import com.capstone.exff.entities.CategoryEntity;
import com.capstone.exff.services.CategoryServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryServices categoryServices;

    @Autowired
    public CategoryController(CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @GetMapping(value = "/category")
    public ResponseEntity loadAllCategory(){
        try {
            List<CategoryEntity> categoryList = categoryServices.loadAllCategory();
            if (categoryList == null) {
                return new ResponseEntity("no category found", HttpStatus.OK);
            } else {
                return new ResponseEntity(categoryList, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
