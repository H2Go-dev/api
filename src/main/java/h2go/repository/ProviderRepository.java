package h2go.repository;


import h2go.model.Provider;
import h2go.model.enums.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, String> {

    Optional<Provider> findByIdAndDeletedAtIsNull(String id);

    Page<Provider> findAllByDeletedAtIsNullOrderByIdAsc(Pageable pageable);

    Page<Provider> findByRegistrationStatusAndDeletedAtIsNull(RegistrationStatus status, Pageable pageable);

}
