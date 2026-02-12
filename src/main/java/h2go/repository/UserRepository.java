package h2go.repository;

import h2go.model.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Page<User> findAllByDeletedAtIsNullOrderByIdAsc(Pageable pageable);

  Optional<User> findByEmailAndDeletedAtIsNull(String email);

  Optional<User> findByIdAndDeletedAtIsNull(String id);

  Optional<User> findByEmail(String username);

}
