package fooddelivery.order_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fooddelivery.order_service.model.User;
import fooddelivery.order_service.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // --- REGISTER ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        // 1. Check if user already exists
        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists with this email!");
        }

        // 2. Create new user
        User newUser = new User(
            body.get("name"), 
            email, 
            body.get("mobile"), 
            body.get("address")
        );

        User savedUser = userRepository.save(newUser);
        System.out.println("JAVA SERVER: ✅ Saved User to DB: " + savedUser.getName());
        return ResponseEntity.ok(savedUser);
    }

    // --- LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        System.out.println("JAVA SERVER: 🔍 Attempting login for: " + email);

        User user = userRepository.findByEmail(email);

        if (user != null) {
            System.out.println("JAVA SERVER: ✅ User found!");
            return ResponseEntity.ok(user);
        } else {
            System.out.println("JAVA SERVER: ❌ User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Please Sign Up.");
        }
    }
}