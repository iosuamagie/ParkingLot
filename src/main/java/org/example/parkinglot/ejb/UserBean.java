package org.example.parkinglot.ejb;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.parkinglot.common.UserDto;
import org.example.parkinglot.entities.User;
import org.example.parkinglot.entities.UserGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UserBean {
    private static final Logger LOG = Logger.getLogger(UserBean.class.getName());

    @Inject
    PasswordBean passwordBean;

    @PersistenceContext
    EntityManager entityManager;

    public List<UserDto> findAllUsers() {
        LOG.info("findAllUsers");
        try {
            TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u", User.class);
            List<User> users = typedQuery.getResultList();
            return copyUsersToDto(users);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private List<UserDto> copyUsersToDto(List<User> users) {
        List<UserDto> dtoList = new ArrayList<>();

        if (users == null) {
            return dtoList;
        }

        for (User user : users) {
            UserDto dto = new UserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail()
            );
            dtoList.add(dto);
        }

        return dtoList;
    }

    public void createUser(String username, String email, String password, Collection<String> groups) {
        LOG.info("createUser");

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);

        // Hash the password using the injected PasswordBean
        newUser.setPassword(passwordBean.convertToSha256(password));

        entityManager.persist(newUser);

        // Assign the groups
        assignGroupsToUser(username, groups);
    }

    public void assignGroupToUser(String username, String group) {
        LOG.info("Assigning group " + group + " to user " + username);

        UserGroup userGroup = new UserGroup();
        userGroup.setUsername(username);
        userGroup.setUserGroup(group);

        entityManager.persist(userGroup);
    }

    // Modified to reuse the logic from assignGroupToUser
    private void assignGroupsToUser(String username, Collection<String> groups) {
        LOG.info("assignGroupsToUser");
        if (groups != null) {
            for (String group : groups) {
                // Reuse the singular method to avoid duplicating code
                assignGroupToUser(username, group);
            }
        }
    }

    public Collection<String> findUsernamesByUserIds(Collection<Long> userIds) {
        List<String> usernames = entityManager.createQuery("SELECT u.username FROM User u WHERE u.id IN :userIds", String.class)
                .setParameter("userIds", userIds)
                .getResultList();
        return usernames;
    }
}