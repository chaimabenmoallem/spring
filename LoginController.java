package com.epix.hawkadmin.controller;


import com.epix.hawkadmin.model.Login;
import com.epix.hawkadmin.model.LoginRequest;
import com.epix.hawkadmin.model.User;
import com.epix.hawkadmin.repository.LoginRepo;
import com.epix.hawkadmin.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/login" , method = RequestMethod.POST)
public class LoginController {

    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginRepo loginRepo;

    @Autowired
    public LoginController(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder, LoginRepo loginRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.loginRepo = loginRepo;
    }

    @Autowired
    private ObjectMapper objectMapper;

      @RequestMapping
     // @PostMapping("/login")
    public ResponseEntity<ObjectNode> loginUser(@RequestBody LoginRequest request )  {

        System.out.println("login data");
        System.out.printf("email" +" " +request.getEmail()+ " " );
        System.out.printf("password" +" " +request.getPassword()+ " ");


        User user = userRepo.findByEmail(request.getEmail());

          Login loginAttempt = new Login();  // This creates a new login attempt record.
          loginAttempt.setEmail(request.getEmail());
          loginAttempt.setPassword(request.getPassword());

        // Retrieve the user from Elasticsearch based on the provided email

          if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
              loginRepo.save(loginAttempt);
              // Passwords match, successful authentication
            return ResponseEntity.ok(objectMapper.createObjectNode().put("message","Authentication successful"));
        } else {
            // Passwords don't match, authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(objectMapper.createObjectNode().put("message","Invalid email or password"));
        }
    }
}
