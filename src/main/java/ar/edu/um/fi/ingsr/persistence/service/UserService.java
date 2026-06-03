package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.User;
import ar.edu.um.fi.ingsr.persistence.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;          // cambio: inyección por constructor

    public UserService(UserRepository userRepository) {   // cambio: constructor en vez de @Autowired
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    // ELIMINADO: findByEmailUser — cargaba todos los usuarios en memoria y filtraba en Java

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found: " + email));
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }
}