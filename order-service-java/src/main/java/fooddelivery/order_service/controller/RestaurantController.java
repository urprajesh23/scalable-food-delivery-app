package fooddelivery.order_service.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fooddelivery.order_service.model.Restaurant;
import fooddelivery.order_service.repository.RestaurantRepository;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // 1. GET ALL (Home Page)
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // 2. SEED DATA (Dummy Data)
    @PostMapping("/seed")
    public String seedRestaurants() {
        if (restaurantRepository.count() > 0) {
            return "Database already has restaurants!";
        }
        List<Restaurant> list = Arrays.asList(
            new Restaurant("Pizza Hut", "Chennai", "https://t.ly/1rW_2", Arrays.asList(
                new Restaurant.MenuItem("Pepperoni Pizza", 450),
                new Restaurant.MenuItem("Margherita Pizza", 300)
            )),
            new Restaurant("Burger King", "Bangalore", "https://t.ly/2qX_9", Arrays.asList(
                new Restaurant.MenuItem("Whopper", 199),
                new Restaurant.MenuItem("Fries", 99)
            )),
            new Restaurant("KFC", "Mumbai", "https://t.ly/3yZ_5", Arrays.asList(
                new Restaurant.MenuItem("Bucket Chicken", 600),
                new Restaurant.MenuItem("Zinger Burger", 180)
            ))
        );
        restaurantRepository.saveAll(list);
        return "✅ Added Mock Restaurants!";
    }

    // 3. ADD RESTAURANT (Admin)
    @PostMapping("/add")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // 4. DELETE RESTAURANT (Admin) - THIS WAS LIKELY MISSING
    @DeleteMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable String id) {
        restaurantRepository.deleteById(id);
        return "✅ Restaurant Deleted!";
    }

    // 5. TOGGLE STATUS (Admin) - THIS WAS LIKELY MISSING
    @PutMapping("/toggle-status/{id}")
    public Restaurant toggleStatus(@PathVariable String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        restaurant.setOpen(!restaurant.isOpen()); // Flip True/False
        return restaurantRepository.save(restaurant);
    }
}