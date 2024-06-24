package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        // @RequestBody dùng để chuyển đổi dữ liệu từ JSON sang Object
        User users = this.userService.handleCreateUser(postManUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User users = this.userService.handleGetUser(id);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.userService.handleGetAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User postManUser) {
        User users = this.userService.handleUpdateUser(postManUser);
        return ResponseEntity.ok(users);
    }
}
