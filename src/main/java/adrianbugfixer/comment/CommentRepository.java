package adrianbugfixer.comment;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import adrianbugfixer.accountInfo.AccountInfo;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Collection<Comment> findByWebsite_Uri(String uri);
	Collection<Comment> findByWebsiteId(Long websiteId);
	Collection<AccountInfo> findByAccountInfoHref(String href);
}
