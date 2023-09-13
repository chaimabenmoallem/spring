package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.model.LoginRequest;
import com.epix.hawkadmin.model.User;
import com.epix.hawkadmin.model.RegistrationRequest;
import com.epix.hawkadmin.repository.UserRepo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:4200" , methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD})
@RestController
@RequestMapping(value = "/register" , method = RequestMethod.POST)


public class UserController {
    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    private ObjectMapper objectMapper;


    /*@GetMapping("/users" )
    public List<User> getUsers() {
        return (List<User>) userRepo.findAll();
    }*/

    @RequestMapping
    //@PostMapping("/register" )
    public ResponseEntity<ObjectNode> registerUser(@RequestBody RegistrationRequest request ) {

        System.out.println("registration data");
        System.out.printf("name" + " " + request.getName()+ " " );
        System.out.printf("email" +" " +request.getEmail()+ " " );
        System.out.printf("password" +" " +request.getPassword()+ " ");
        System.out.printf("confirmPassword" +" " +request.getConfirmPassword()+ " " );

        // Validate registration data
        if (!isEmailUnique(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.createObjectNode().put("message","Email already exists"));

        }

        if (!isPasswordValid(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.createObjectNode().put("message","Invalid password"));
        }

        //check if the passwords match


        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.createObjectNode().put("message","Passwords do not match"));
        }

        // Create User entity and save to repository
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setConfirmPassword(request.getConfirmPassword());

        //  Hash the password using BCryptPasswordEncoder
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        String hashedConfirmPassword = passwordEncoder.encode(request.getConfirmPassword());
        user.setConfirmPassword(hashedConfirmPassword);

        User savedUser = userRepo.save(user);


        if (savedUser != null) {

                System.out.println("Saved user ID: " + savedUser.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.createObjectNode().put("message", "User registered successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(objectMapper.createObjectNode().put("message", "Error registering user"));
        }
    }

    @GetMapping("/registration")
    public ResponseEntity<String> checkService() {
        return ResponseEntity.ok("Service is running");
    }


  /*  @PostMapping("/login")
    public ResponseEntity<ObjectNode> loginUser(@RequestBody LoginRequest request )  {

        System.out.println("login data");
        System.out.printf("email" +" " +request.getEmail()+ " " );
        System.out.printf("password" +" " +request.getPassword()+ " ");


        User user = userRepo.findByEmail(request.getEmail());

        // Retrieve the user from Elasticsearch based on the provided email

        if (user != null && user.getPassword().equals(request.getPassword())) {
            // Passwords match, successful authentication
            return ResponseEntity.ok(objectMapper.createObjectNode().put("message","Authentication successful"));
        } else {
            // Passwords don't match, authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(objectMapper.createObjectNode().put("message","Invalid email or password"));
        }
    }*/


    private boolean isEmailUnique(String email) {
        User existingUser = userRepo.findByEmail(email);
        return existingUser == null;
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$");
       /* The password must contain at least one digit.
        The password must contain at least one letter.
        The password must contain at least one special character from the set [@#$%^&+=!].
        The password must be at least 8 characters long.*/
    }
}








    /*private  RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody Register registrationRequest,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Return validation errors as response
            String errorMessage = getErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        String email = registrationRequest.getEmail();

        // Check if user is already registered
        if (registerService.isUserRegistered(email)) {
            return ResponseEntity.badRequest().body("User with email " + email + " already exists");
        }

        // Register the user
        registerService.registerUser(registrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    private String getErrorMessage(BindingResult bindingResult) {
        // Construct a user-friendly error message from the binding result
        // This can be customized based on your specific validation requirements
        StringBuilder errorMessage = new StringBuilder();
        bindingResult.getFieldErrors().forEach(error ->
                errorMessage.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ")
        );
        return errorMessage.toString();

    }*/

