/*package com.epix.hawkadmin.services;

//import com.epix.hawkadmin.repository.LoginRepo;
import com.epix.hawkadmin.model.Register;
import com.epix.hawkadmin.repository.RegisterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {



    private  RegisterRepo registerRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(RegisterRepo registerRepo, PasswordEncoder passwordEncoder) {
        this.registerRepo= registerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserRegistered(String email) {
        return registerRepo.findByEmail(email);
    }

    public void registerUser(Register registrationRequest) {
        String name = registrationRequest.getName();
        String email = registrationRequest.getEmail();
        String password = passwordEncoder.encode(registrationRequest.getPassword());
        String confirmPassword = passwordEncoder.encode(registrationRequest.getConfirmPassword());

        Register register = new Register(name, email, password, confirmPassword);
        registerRepo.save(register);
    }


    }*/

      /*  private static List<Login> users = new ArrayList<>();

        static {
            // Add static users
            Login user = new Login();
            user.setEmail("chaima@gmail.com");
            user.setPassword("chaima");
            users.add(user);

        }

        static {
            // Add static users
            Login user = new Login();
            user.setEmail("raja@gmail.com");
            user.setPassword("raja");
            users.add(user);

        }

        public static List<Login> getUsers() {
            return users;
        }

        public static void addUser(Login user) {
            users.add(user);
        }


}*/
