package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleGetUser(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public List<User> handleGetAllUsers() {
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User user) {
        User userToUpdate = this.handleGetUser(user.getId());
        if(userToUpdate != null){
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPassword(user.getPassword());
        }
        return this.userRepository.save(userToUpdate);
    }
}
