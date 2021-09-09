package com.taskmanager.apicontroller;


import com.taskmanager.model.AuthRequest;
import com.taskmanager.model.User;
import com.taskmanager.service.UserServiceImpl;
import com.taskmanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/login")
    public LinkedHashMap<String, String> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("Invalid username/password");
        }
        User user = userService.getUserByEmail(authRequest.getEmail());
        LinkedHashMap<String, String> user_details = new LinkedHashMap<String, String>();
        user_details.put("json_token", jwtUtil.generateToken(authRequest.getEmail()));
        user_details.put("id", String.valueOf(user.getId()));
        user_details.put("email", user.getEmail());
        return user_details;
    }


}
