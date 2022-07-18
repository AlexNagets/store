package com.store.controller;

import com.store.models.UserDto;
import com.store.service.IUserService;
import com.store.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RegistrationController {
    @Autowired
    private IUserService userService;

    @PostMapping("registration")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) {
        userService.registerNewUserAccount(userDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> defaultPage() {
        Map<String, String> map = new HashMap<>();
        map.put("JSESSIONID", RequestContextHolder.currentRequestAttributes().getSessionId());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
