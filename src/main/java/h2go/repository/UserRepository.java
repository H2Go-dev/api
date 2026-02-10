package h2go.repository;

import h2go.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmailAndDeletedAtIsNull(String email);

  Optional<User> findByIdAndDeletedAtIsNull(String id);

  List<User> findByDeletedAtIsNull();

  Optional<User> findByEmail(String username);
}
