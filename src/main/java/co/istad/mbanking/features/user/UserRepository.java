package co.istad.mbanking.features.user;

import co.istad.mbanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByStudentIdCard(String studentIdCard);

    boolean existsByNationalCardId(String nationalCardId);

    boolean existsByName(String name);

    Optional<User> findByPassword(String password);

    @Query("SELECT u FROM User AS u WHERE u.uuid = :uuid")
    Optional<User> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    @Modifying
    @Query("UPDATE User AS u SET u.isBlocked = TRUE WHERE u.uuid = ?1")
    void blockByUuid(String uuid);

    @Modifying // for manipulate in jpa
    void deleteByUuid(String uuid);

    @Modifying
    @Query("UPDATE User AS u SET u.isDeleted = TRUE WHERE u.uuid = ?1")
    void disableByUuid(String uuid);

    @Modifying
    @Query("UPDATE User AS u SET u.isDeleted = FALSE WHERE u.uuid = ?1")
    void enableByUuid(String uuid);

    Optional<User> findByPhoneNumber(String username);
}
