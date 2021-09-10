package web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(name = "User.Username.findWithRoles", value = "from User u JOIN FETCH u.roles where u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query(name = "User.Email.findWithRoles", value = "from User u JOIN FETCH u.roles where u.email = :email")
    User findByEmail(@Param("email") String email);

}
