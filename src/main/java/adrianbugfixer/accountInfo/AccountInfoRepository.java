package adrianbugfixer.accountInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long>{

}
