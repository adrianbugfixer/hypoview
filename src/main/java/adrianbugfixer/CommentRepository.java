package adrianbugfixer;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Collection<Comment> findByWebsite_Uri(String uri);

	Collection<Comment> findByWebsiteId(Long websiteId);
}
