package h2go.controller;

import h2go.dto.request.UserRegistrationRequest;
import h2go.dto.response.UserRetrievalResponse;
import h2go.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping
    public List<UserRetrievalResponse> getAllUsers(@RequestParam(defaultValue = "false") Boolean deleted) {
        return userService.getAllUsers(deleted);
    }

    @PostMapping
    public ResponseEntity<UserRetrievalResponse> createUser(
            @Valid
            @RequestBody
            UserRegistrationRequest userRegistrationRequest
    ) {
        UserRetrievalResponse userRetrievalResponse = userService.createUser(userRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRetrievalResponse);
    }

    @GetMapping("/{id}")
    public UserRetrievalResponse getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }

    @GetMapping("/profile")
    public UserRetrievalResponse profile(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserProfile(userDetails.getUsername());
    }

    @PutMapping()
    public UserRetrievalResponse updateUser(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @Valid
            @RequestBody
            UserRegistrationRequest userRegistrationRequest
    ) {
        return userService.updateUser(userDetails.getUsername(), userRegistrationRequest);
    }

}
