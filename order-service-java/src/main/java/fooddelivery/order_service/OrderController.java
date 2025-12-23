package fooddelivery.order_service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fooddelivery.order_service.model.Order;
import fooddelivery.order_service.repository.OrderRepository;

// ... imports ...

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // 1. CREATE ORDER (Updated to save Driver)
    @PostMapping
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> orderData) {
        String food = (String) orderData.get("food");
        double price = Double.parseDouble(orderData.get("price").toString());
        String customer = (String) orderData.get("customer");
        String driver = (String) orderData.get("driver"); // <--- Read Driver

        // Save everything to MongoDB
        Order newOrder = new Order(food, price, customer, driver);
        Order savedOrder = orderRepository.save(newOrder);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("orderId", savedOrder.getId());
        response.put("driver", savedOrder.getDriver());
        return response;
    }

    // 2. GET MY ORDERS (New Endpoint)
    @GetMapping("/user/{name}")
    public List<Order> getOrdersByCustomer(@PathVariable String name) {
        return orderRepository.findByCustomer(name);
    }

    // NEW ENDPOINT: Cancel Order
    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        Optional<Order> orderBox = orderRepository.findById(id);
        
        if (!orderBox.isPresent()) {
            return ResponseEntity.status(404).body("Order not found");
        }

        Order order = orderBox.get();

        // 1. Check if already cancelled
        if ("CANCELLED".equals(order.getStatus())) {
            return ResponseEntity.badRequest().body("Order is already cancelled!");
        }

        // 2. Calculate Time Difference (in Minutes)
        long minutesPassed = ChronoUnit.MINUTES.between(order.getOrderTime(), LocalDateTime.now());

        if (minutesPassed > 5) {
            return ResponseEntity.badRequest().body("❌ Cannot cancel! Time limit (5 mins) exceeded.");
        }

        // 3. Cancel the Order
        order.setStatus("CANCELLED");
        orderRepository.save(order);
        
        return ResponseEntity.ok("✅ Order Cancelled Successfully!");
    }
}