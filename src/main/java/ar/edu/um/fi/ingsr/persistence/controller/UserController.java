package ar.edu.um.fi.ingsr.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import ar.edu.um.fi.ingsr.persistence.domain.User;
import ar.edu.um.fi.ingsr.persistence.dto.UserDTO;
import ar.edu.um.fi.ingsr.persistence.service.UserService;


@RestController
@RequestMapping("/api/v1/db/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User save(@Valid @RequestBody UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPasswordHash(userDTO.getPasswordHash());
        return userService.save(user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public User findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }
}
