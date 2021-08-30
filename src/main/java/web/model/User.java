package web.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @Column(name = "name")
   private String name;
   @Column(name = "username", unique = true, nullable = false)
   private String username;
   @Column(name = "email")
   private String email;
   @Column(name = "password")
   private String password;

   @ManyToMany  //(fetch = FetchType.LAZY)  <----- LAZY по умолчанию
   @JoinTable(name = "users_roles",
           joinColumns = @JoinColumn(name = "users_id"),
           inverseJoinColumns = @JoinColumn(name = "roles_id"))
   private Set<Role> roles;

   public User() {}

   public User(String name, String username, String email, String password, Set<Role> roles) {
      this.name = name;
      this.username = username;
      this.email = email;
      this.password = password;
      this.roles = roles;
   }

   @Override
   public String toString() {
      return String.format("[Id] Name = [%d] %s " +
                           "Login = %s Email = %s " +
                           "Pass = [%s] Roles=%s \n-------------------",
                           id, name, username, email, password, roles);
   }

   public Long getId() {
      return id;
   }
   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   @Override
   public String getUsername() {
      return username;
   }
   public void setUsername(String username) {
      this.username = username;
   }

   public String getEmail() {
      return email;
   }
   public void setEmail(String email) {
      this.email = email;
   }

   @Override
   public String getPassword() {
      return password;
   }
   public void setPassword(String password){
      this.password=password;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return roles;
   }
   public Set<Role> getRoles() {
      return roles;
   }
   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }
   @Override
   public boolean isAccountNonLocked() {
      return true;
   }
   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }
   @Override
   public boolean isEnabled() {
      return true;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return id.equals(user.id) && name.equals(user.name) && username.equals(user.username) && Objects.equals(email, user.email) && password.equals(user.password) && roles.equals(user.roles);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, username, email, password, roles);
   }
}
