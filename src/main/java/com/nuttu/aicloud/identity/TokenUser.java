package com.nuttu.aicloud.identity;

import com.nuttu.aicloud.model.user.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class TokenUser extends org.springframework.security.core.userdetails.User {
    private User user;

    //For returning a normal user
    public TokenUser(User user) {
        //token username 使用 user 的 uuid
        super( user.getUuid(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()) );
        //super(user.getUserName(), user.getPassword(), true, true, true, true,  AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getRole() {
        return user.getRole().toString();
    }
}
