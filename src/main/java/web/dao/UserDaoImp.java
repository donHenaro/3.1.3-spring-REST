package web.dao;

import org.springframework.stereotype.Repository;
import web.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager em;

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("from User u JOIN FETCH u.roles where u.username = :username", User.class);
        return query.setParameter("username", username).getSingleResult();
    }

}