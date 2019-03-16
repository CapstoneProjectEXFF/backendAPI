package com.capstone.exff.controllers;

import com.capstone.exff.entities.ImageEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.ImageServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ImageController {

    private final ImageServices imageServices;

    @Autowired
    public ImageController(ImageServices imageServices) {
        this.imageServices = imageServices;
    }

    @GetMapping("/image/{itemId:[\\d]+}")
    public ResponseEntity getImagesByItemId(@PathVariable("itemId") int itemId){
        try {
            List<ImageEntity> result = imageServices.getImagesByItemId(itemId);
            if (result == null) {
                return new ResponseEntity("no image found", HttpStatus.OK);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
