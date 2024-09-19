package hampusborg.webservicesproject.repository;

import hampusborg.webservicesproject.model.MyUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<MyUser, String> {
    Optional<MyUser> findByUsername(String username);
}