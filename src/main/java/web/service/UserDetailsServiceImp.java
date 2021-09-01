package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.model.User;


@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserService us;

    @Override
    public UserDetails loadUserByUsername(String searchStr) throws UsernameNotFoundException {
        User user = us.findByUsername(searchStr); //Поиск по логину
        if(user != null){
            return (UserDetails) user;
        }
        user = us.findByEmail(searchStr);
        if(user != null){
            return (UserDetails) user;
        }

        throw new  UsernameNotFoundException("Пользователь не существует");
    }

}

