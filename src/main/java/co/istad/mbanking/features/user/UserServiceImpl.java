package co.istad.mbanking.features.user;

import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.user.dto.UserCreateRequest;
import co.istad.mbanking.features.user.dto.UserEditRequest;
import co.istad.mbanking.features.user.dto.UserPasswordRequest;
import co.istad.mbanking.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public void createNew(UserCreateRequest request) {
        User user = userMapper.fromUserCreateRequest(request);

        if(userRepository.existsByNationalCardId(request.nationalCardId())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "National card id is already existed!"
            );
        }

        if(userRepository.existsByPhoneNumber(request.phoneNumber())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Phone number is already existed!"
            );
        }

        if(userRepository.existsByStudentIdCard(request.studentIdCard())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student card id is already existed!"
            );
        }

        if(!request.password().equals(request.confirmedPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password does not match"
            );
        }

        // add role
        List<Role> roleList = new ArrayList<>();
        Role role = roleRepository.findByName("USER")
                        .orElseThrow(()-> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User role does not exist!"
                        ));
        // add role to roleList
        roleList.add(role);

        user.setUuid(UUID.randomUUID().toString());
        user.setProfileImage("Avatar.png");
        user.setCreatedAt(LocalDateTime.now());
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(roleList);

        userRepository.save(user);
    }

    @Override
    public void changeUserPassword(UserPasswordRequest userPasswordRequest) {

        if(!userRepository.existsByName(userPasswordRequest.name())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "This user "+userPasswordRequest.name()+" is not exist!"
            );
        }

        User user = userRepository.findByPassword(userPasswordRequest.oldPassword())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Password does not match!"
                ));

        if(!userPasswordRequest.NewPassword().equals(userPasswordRequest.confirmedNewPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password does not match!"
            );
        }

        user.setPassword(userPasswordRequest.NewPassword());

        userRepository.save(user);

    }

    @Override
    public void editUserProfile(UserEditRequest userEditRequest , String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User does not exist!"
                ));

        user.setCityOrProvince(userEditRequest.cityOrProvince());
        user.setKhanOrDistrict(userEditRequest.khanOrDistrict());
        user.setSangkatOrCommune(userEditRequest.sangkatOrCommune());
        user.setVillage(userEditRequest.village());
        user.setStreet(userEditRequest.street());
        user.setEmployeeType(userEditRequest.employeeType());
        user.setPosition(userEditRequest.position());
        user.setCompanyName(userEditRequest.companyName());
        user.setMainSourceOfIncome(userEditRequest.mainSourceOfIncome());
        user.setMonthlyIncomeRange(userEditRequest.monthlyIncomeRange());

        userRepository.save(user);

    }
}
