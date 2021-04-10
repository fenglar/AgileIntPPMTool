package pl.marcin.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marcin.ppmtool.domain.User;
import pl.marcin.ppmtool.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);

        if(user==null) new UsernameNotFoundException("User not found");
        return user;
    }

    @Transactional
    public User loadUserById (Long id) {
        User user = userRepository.getById(id);
        if(user==null) new UsernameNotFoundException("User not found");
        return user;
    }
}
