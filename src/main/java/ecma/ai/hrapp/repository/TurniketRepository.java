package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Turniket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Turniket, UUID> {
    Optional<Turniket> findByNumber(String number);
    void deleteByNumber(String number);
    Optional<Turniket> findByOwnerId(UUID owner_id);
}
