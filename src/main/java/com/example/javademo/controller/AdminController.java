package com.example.javademo.controller;


import com.example.javademo.dto.UpdateUserDTO;
import com.example.javademo.model.ResponseResult;
import com.example.javademo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/update")
    public ResponseEntity<ResponseResult> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO){
            System.out.println(updateUserDTO);
            userService.update(updateUserDTO);
            return ResponseEntity.ok((new ResponseResult()).success(null));
    }
}
