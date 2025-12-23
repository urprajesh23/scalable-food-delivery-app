package fooddelivery.order_service.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty; // <--- 1. NEW IMPORT

@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    private String name;
    private String location;
    private String imageUrl; // URL to an image of the restaurant
    private List<MenuItem> menu; // A list of food items
    
    // 2. NEW ANNOTATION: Forces JSON to be "isOpen" instead of "open"
    @JsonProperty("isOpen") 
    private boolean isOpen = true; // Default to Open    

    // Nested Class for Menu Items
    public static class MenuItem {
        private String name;
        private double price;

        public MenuItem() {}
        public MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
        }
        // Getters/Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    // Constructors
    public Restaurant() {}
    public Restaurant(String name, String location, String imageUrl, List<MenuItem> menu) {
        this.name = name;
        this.location = location;
        this.imageUrl = imageUrl;
        this.menu = menu;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<MenuItem> getMenu() { return menu; }
    public void setMenu(List<MenuItem> menu) { this.menu = menu; }
    
    // Getter for boolean
    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }
}