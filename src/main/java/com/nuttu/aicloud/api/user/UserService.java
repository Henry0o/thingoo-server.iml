package com.nuttu.aicloud.api.user;

import com.nuttu.aicloud.identity.TokenUser;
import com.nuttu.aicloud.model.user.Role;
import com.nuttu.aicloud.model.user.User;
import com.nuttu.aicloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    /**
     * 获取的是用户的uuid
    */
    public String getLoggedInUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null){
            return "nosession";
        }
        return auth.getName();  //获取的是用户的uuid
    }

    public Role getLoggedInUserRole() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null){
            return Role.GUEST;
        }
        TokenUser user = (TokenUser)auth.getDetails();
        return Role.valueOf(user.getRole());
    }

    public User getLoggedInUser() {
        String loggedInUserId = this.getLoggedInUserId();
        User user = this.getUserByUuid(loggedInUserId);
        return user;
    }

    public User getUserByUuid(String uuid){
        User user = this.userRepository.findOneByUuid(uuid).orElseGet( () -> new User());
        return user;
    }
    public String getUuidByEmail(String email){
        User user = this.userRepository.findOneByEmail(email).orElseGet(()->new User());
        return user.getUuid();
    }


}
