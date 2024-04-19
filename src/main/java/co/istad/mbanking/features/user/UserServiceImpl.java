package co.istad.mbanking.features.user;

import co.istad.mbanking.base.BasedMessage;
import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.user.dto.*;
import co.istad.mbanking.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PasswordEncoder encoder;

    @Value("${media.base-uri}")
    private String mediaUri;

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

        // set default role USER when create user
        List<Role> roleList = new ArrayList<>();
        Role role = roleRepository.findByName("USER")
                        .orElseThrow(()-> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User role does not exist!"
                        ));

        // set dynamic roles when create user
        request.roles()
                .forEach(r -> {
                    Role role1 = roleRepository.findByName(r.getName())
                            .orElseThrow(()-> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "User role does not exist!"
                            ));
                    roleList.add(role1);
                });

        // add role to roleList
        roleList.add(role);

        user.setUuid(UUID.randomUUID().toString());
        user.setProfileImage("Avatar.png");
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(roleList);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);

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
    public UserResponse editUserProfile(UserEditRequest userEditRequest , String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User does not exist!"
                ));

        userMapper.fromUserEditRequest(userEditRequest,user);

        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserDetailsResponse> findAll() {

        List<User> users = userRepository.findAll();

        return userMapper.toUserDetailResponseList(users);
    }

    @Override
    public UserResponse findByUuid(String uuid) {

        if(!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public BasedMessage blockByUuid(String uuid) {
        if(!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }

        userRepository.blockByUuid(uuid);
        return new BasedMessage("User has been block");

    }

    @Override
    @Transactional
    public BasedMessage deleteByUuid(String uuid) {

        if(!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }

        userRepository.deleteByUuid(uuid);

        return new BasedMessage("User has been deleted");
    }

    @Transactional
    @Override
    public BasedMessage disableUserByUuid(String uuid) {
        if(!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }

        userRepository.disableByUuid(uuid);
        return new BasedMessage("User has been disable");
    }

    @Transactional
    @Override
    public BasedMessage enableUserByUuid(String uuid) {
        if(!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }

        userRepository.enableByUuid(uuid);
        return new BasedMessage("User has been enable");
    }

    @Override
    public Page<UserResponse> findList(int page, int limit) {

        // create page request object
        PageRequest pageRequest = PageRequest.of(page,limit);
        // invoke findAll
        Page<User> users = userRepository.findAll(pageRequest);
        //map result of pagination
        return users.map(user -> userMapper.toUserResponse(user));
    }

    @Override
    public String updateProfileImage(String uuid, String mediaName) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(()->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User not found!")
                );

        user.setProfileImage(mediaName);
        userRepository.save(user);

        return mediaUri +"IMAGE/"+mediaName;
    }


}
