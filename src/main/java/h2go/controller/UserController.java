package h2go.controller;

import h2go.dto.UserCreationDTO;
import h2go.dto.UserRetrievalDTO;
import h2go.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserRetrievalDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void createUser(@Valid @RequestBody UserCreationDTO userCreationDTO){
        userService.createUser(userCreationDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public UserRetrievalDTO getUserById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }

}
