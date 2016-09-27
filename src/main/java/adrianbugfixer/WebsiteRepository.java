package adrianbugfixer;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<Website, Long> {
	Optional<Website> findByUri(String uri);
	Optional<Website> findById(Long websiteId);
}
