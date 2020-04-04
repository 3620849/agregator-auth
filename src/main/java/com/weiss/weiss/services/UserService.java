package com.weiss.weiss.services;

import com.weiss.weiss.dao.UserDAO;
import com.weiss.weiss.model.Role;
import com.weiss.weiss.model.UserInfo;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UserService  {

    @Autowired
    private UserDAO userDao;
    @Autowired
    PasswordEncoder passwordEncoder;


    public UserInfo findUserByLogin(String login) throws UsernameNotFoundException {
        return userDao.findUserByLogin(login);
    }
    public UserInfo findUserByLogin(UserInfo user) throws UsernameNotFoundException {
        return userDao.findUserByLogin(user.getLogin());
    }
    public UserInfo findById(String id) {
        return userDao.findUserById(id);
    }
//    public UserInfo loadUserByUsername(UserInfo user) {
//        return userDao.findUserByName(user.getUsername());
//    }
//    public  UserInfo findUserByName(UserInfo  user) {
//        return userDao.findUserByName(user.getUsername());
//    }

    public void addNewUser(@NonNull UserInfo user) {
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.grantRole(Role.ROLE_USER);
        if(user.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDao.addUser(user);
    }

    public boolean isUserExist(UserInfo user) {
        try{
            userDao.findUserByName(user.getUsername());
        }catch (UsernameNotFoundException e ){
            return false;
        }
        return true;
    }
}
