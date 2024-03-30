package co.istad.mbanking.features.user;

import co.istad.mbanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByStudentIdCard(String studentIdCard);

    boolean existsByNationalCardId(String nationalCardId);

    boolean existsByName(String name);

    Optional<User> findByPassword(String password);

    Optional<User> findByUuid(String uuid);

}
