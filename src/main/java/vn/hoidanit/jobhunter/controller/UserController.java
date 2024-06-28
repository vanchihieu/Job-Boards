package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("create new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) throws IdInvalidException {
        // @RequestBody dùng để chuyển đổi dữ liệu từ JSON sang Object

        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + postManUser.getEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
        }

        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User users = this.userService.handleCreateUser(postManUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(users));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws IdInvalidException {
        User currentUser = this.userService.handleGetUser(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable long id) throws IdInvalidException {
        User users = this.userService.handleGetUser(id);
        if (users == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.convertToResUserDTO(users));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<User> spec, Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUsers(spec, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("update user by id")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User postManUser) throws IdInvalidException {
        User users = this.userService.handleUpdateUser(postManUser);
        if (users == null) {
            throw new IdInvalidException("User với id = " + postManUser.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(users));
    }
}
