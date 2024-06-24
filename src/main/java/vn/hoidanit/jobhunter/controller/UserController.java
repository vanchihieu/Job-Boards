package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User postManUser) {
        // @RequestBody dùng để chuyển đổi dữ liệu từ JSON sang Object
        User user = this.userService.handleCreateUser(postManUser);

        return user;
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable long id) {
        return this.userService.handleGetUser(id);
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.handleGetAllUsers();
    }
}
