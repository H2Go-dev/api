package h2go.repository;

import h2go.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findByIdAndDeletedAtIsNull(String id);
}
