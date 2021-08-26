package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.dao.UserRepository;
import web.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

   private final UserRepository userRep;
   private final PasswordEncoder passEncoder;

   @Autowired
   public UserServiceImp(UserRepository userRep, PasswordEncoder passwordEncoder) {
      this.userRep = userRep;
      this.passEncoder = passwordEncoder;
   }

   @Override
   public void create(User user) {
      user.setPassword(passEncoder.encode(user.getPassword()));
      userRep.save(user);
   }

   @Override
   public void update(User user) {
      if(user.getPassword().length() != 0){
         user.setPassword(passEncoder.encode(user.getPassword()));
      }else {
         user.setPassword(findById(user.getId()).getPassword());
      }
      userRep.save(user);
   }

   @Override
   public void delete(User user) {
      userRep.delete(user);
   }

   @Override
   public void deleteById(Long id) {
         userRep.deleteById(id);
   }

   @Override
   public User findById(Long id) {
      Optional<User> opt = userRep.findById(id);
      return (User) opt.orElse(null);
   }

   @Override
   public List<User> listUsers() {
      return userRep.findAll();
   }

   @Override
   public User findByUsername(String username) {
      return userRep.findByUsername(username);
   }

}
