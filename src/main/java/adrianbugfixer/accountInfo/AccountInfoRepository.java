package adrianbugfixer.accountInfo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long>{
	Optional<AccountInfo> findByHref(String href);
}
