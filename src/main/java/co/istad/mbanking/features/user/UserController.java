package co.istad.mbanking.features.user;

import co.istad.mbanking.base.BaseResponse;
import co.istad.mbanking.base.BasedMessage;
import co.istad.mbanking.features.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Slf4j
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

    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDetailsResponse> findAll(){
        return userService.findAll();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> findList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "2") int limit
    ){
        return userService.findList(page,limit);
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse findByUuid(@PathVariable String uuid){
        return userService.findByUuid(uuid);
    }

    @PutMapping("/{uuid}/block")
    @ResponseStatus(HttpStatus.OK)
    public BasedMessage blockByUuid(@PathVariable String uuid){
        return userService.blockByUuid(uuid);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public BasedMessage deleteByUuid(@PathVariable String uuid){
        return userService.deleteByUuid(uuid);
    }

    @PutMapping("/{uuid}/disable")
    @ResponseStatus(HttpStatus.OK)
    public BasedMessage disableByUuid(@PathVariable String uuid){
        return userService.disableUserByUuid(uuid);
    }

    @PutMapping("/{uuid}/enable")
    @ResponseStatus(HttpStatus.OK)
    public BasedMessage enableByUuid(@PathVariable String uuid){
        return userService.enableUserByUuid(uuid);
    }

    @PutMapping("/{uuid}/profile-image")
    public BaseResponse<?> updateProfileImage(@PathVariable String uuid,
                                           @RequestBody @Valid UserProfileImageRequest userProfileImageRequest){

        String newProfileImageUri = userService.updateProfileImage(uuid, userProfileImageRequest.mediaName());
        log.info("image url {}",newProfileImageUri);


        return BaseResponse.builder()
                .payload(newProfileImageUri)
                .build();
    }



}
