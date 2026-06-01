package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.um.fi.ingsr.persistence.repository.UserRepository;
import ar.edu.um.fi.ingsr.persistence.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false)
    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
//TODO ejemplo de mal aplicado por la inteligencia artificial
    public User findByEmailUser(String email) {
        return userRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
            
    }
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findFirstByEmail(email).orElse(null);
        
        } 
    
        public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = false)
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }

    

}
