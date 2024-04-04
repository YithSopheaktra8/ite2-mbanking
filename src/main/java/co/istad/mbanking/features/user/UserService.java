package co.istad.mbanking.features.user;

import co.istad.mbanking.base.BasedMessage;
import co.istad.mbanking.features.user.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    void createNew(UserCreateRequest userCreateRequest);

    void changeUserPassword(UserPasswordRequest userPasswordRequest);

    UserResponse editUserProfile(UserEditRequest userEditRequest, String uuid);

    List<UserDetailsResponse> findAll();

    UserResponse findByUuid(String uuid);

    BasedMessage blockByUuid(String uuid);

    BasedMessage deleteByUuid(String uuid);

    BasedMessage disableUserByUuid(String uuid);

    BasedMessage enableUserByUuid(String uuid);

    Page<UserResponse> findList(int page, int limit);

    String updateProfileImage(String uuid,String mediaName);
}
