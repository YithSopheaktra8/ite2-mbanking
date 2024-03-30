package co.istad.mbanking.features.user;

import co.istad.mbanking.features.user.dto.UserCreateRequest;
import co.istad.mbanking.features.user.dto.UserEditRequest;
import co.istad.mbanking.features.user.dto.UserPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNew(@Valid @RequestBody UserCreateRequest userCreateRequest){
        userService.createNew(userCreateRequest);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody UserPasswordRequest userPasswordRequest){
        userService.changeUserPassword(userPasswordRequest);
    }

    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public void editUserByUuid(@PathVariable String uuid, @RequestBody UserEditRequest userEditRequest){
        userService.editUserProfile(userEditRequest,uuid);
    }

}
