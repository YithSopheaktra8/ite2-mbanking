package co.istad.mbanking.features.accounttype;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.features.accounttype.dto.AccountTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {

    Optional<AccountType> findByAliasIgnoreCase(String alias);

}
