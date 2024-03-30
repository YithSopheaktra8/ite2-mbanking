package co.istad.mbanking.features.user;

import co.istad.mbanking.features.user.dto.UserCreateRequest;
import co.istad.mbanking.features.user.dto.UserEditRequest;
import co.istad.mbanking.features.user.dto.UserPasswordRequest;

public interface UserService {
    void createNew(UserCreateRequest userCreateRequest);

    void changeUserPassword(UserPasswordRequest userPasswordRequest);

    void editUserProfile(UserEditRequest userEditRequest, String uuid);
}
