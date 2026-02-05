package h2go.controller;

import h2go.dto.UserCreationDTO;
import h2go.dto.UserRetrievalDTO;
import h2go.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // ADMIN ONLY ENDPOINT
    @GetMapping
    public List<UserRetrievalDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    public void createUser(@Valid @RequestBody UserCreationDTO userCreationDTO){
        userService.createUser(userCreationDTO);
    }

}
