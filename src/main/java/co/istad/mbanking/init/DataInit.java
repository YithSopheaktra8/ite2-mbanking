package co.istad.mbanking.init;

import co.istad.mbanking.domain.Role;
import co.istad.mbanking.features.user.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final RoleRepository roleRepository;

    @PostConstruct
    void init() {
        if (roleRepository.count() < 1) {
            // generate role
            Role user = new Role();
            user.setName("USER");

            Role admin = new Role();
            admin.setName("ADMIN");

            Role staff = new Role();
            staff.setName("STAFF");

            Role customer = new Role();
            customer.setName("CUSTOMER");

            roleRepository.saveAll(
                    List.of(
                            user,
                            admin,
                            customer,
                            staff
                    )
            );
        }
    }
}
