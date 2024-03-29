package co.istad.mbanking.init;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.domain.Role;
import co.istad.mbanking.features.accounttype.AccountTypeRepository;
import co.istad.mbanking.features.cardtype.CardTypeRepository;
import co.istad.mbanking.features.user.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final RoleRepository roleRepository;
    private final CardTypeRepository cardTypeRepository;
    private final AccountTypeRepository accountTypeRepository;

    @PostConstruct
    void initRole() {
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

            roleRepository.saveAll(List.of(user, admin, customer, staff));
        }
    }


    @PostConstruct
    void initCardType(){
        if(cardTypeRepository.count() < 1){
            CardType visa = new CardType();
            visa.setName("Visa");
            visa.setIsDeleted(false);

            CardType masterCard = new CardType();
            masterCard.setName("Mastercard");
            masterCard.setIsDeleted(false);

            cardTypeRepository.saveAll(List.of(visa,masterCard));
        }
    }

    @PostConstruct
    void initAccountType(){
        if(accountTypeRepository.count() < 1){
            AccountType payroll = new AccountType();
            payroll.setName("Payroll");
            payroll.setAlias("payroll");
            payroll.setIsDeleted(false);
            payroll.setDescription("this is payroll account type");

            AccountType saving = new AccountType();
            saving.setName("Saving");
            saving.setAlias("saving");
            saving.setIsDeleted(false);
            saving.setDescription("this is saving account type");

            accountTypeRepository.saveAll(List.of(payroll,saving));
        }
    }
}
