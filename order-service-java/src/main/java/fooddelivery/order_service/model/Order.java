package fooddelivery.order_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime; // <--- Import this

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String food;
    private double price;
    private String customer;
    private String driver;
    
    // NEW FIELDS
    private String status; 
    private LocalDateTime orderTime; 

    public Order() {}

    public Order(String food, double price, String customer, String driver) {
        this.food = food;
        this.price = price;
        this.customer = customer;
        this.driver = driver;
        this.status = "PLACED"; // Default status
        this.orderTime = LocalDateTime.now(); // Set time immediately
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFood() { return food; }
    public void setFood(String food) { this.food = food; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }
    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }

    // NEW Getters/Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
}