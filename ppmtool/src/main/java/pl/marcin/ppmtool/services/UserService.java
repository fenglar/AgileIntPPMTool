package pl.marcin.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.marcin.ppmtool.domain.User;
import pl.marcin.ppmtool.exceptions.UsernameAlreadyExistsException;
import pl.marcin.ppmtool.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());
            //make sure that password and confirmpassword match
            //we dont persist or show the confirmpassword
            return userRepository.save(newUser);
        } catch (Exception e) {
        throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }

    }


}
