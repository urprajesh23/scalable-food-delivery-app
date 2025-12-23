package fooddelivery.order_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import fooddelivery.order_service.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    // This is empty because Spring Boot automagically writes the code for "save", "delete", "find" for us!
    List<Order> findByCustomer(String customer);
}   