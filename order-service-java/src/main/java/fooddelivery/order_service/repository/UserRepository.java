package fooddelivery.order_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import fooddelivery.order_service.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // This allows us to find a user by email later if we need login
    User findByEmail(String email);
}