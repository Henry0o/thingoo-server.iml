package com.nuttu.aicloud.api;

import com.nuttu.aicloud.model.session.SessionItem;
import com.nuttu.aicloud.model.session.SessionResponse;
import com.nuttu.aicloud.model.user.LoginRequest;
import com.nuttu.aicloud.model.user.User;
import com.nuttu.aicloud.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
This is a dummy rest controller, for the purpose of documentation (/session) path is map to a filter
 - This will only be invoked if security is disabled
 - If Security is enabled then SessionFilter.java is invoked
 - Enabling and Disabling Security is done at config/application.properties 'security.ignored=/**'
*/

@RestController
@Api(tags = {"Authentication"})
public class SessionController {

    @Autowired
    private UserRepository userRepository;

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Will return a security token, which must be passed in every request", response = SessionResponse.class) })
    @RequestMapping(value = "/token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SessionResponse newSession(@RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
        System.out.format("\n /Token Called username=%s\n", login.getUsername());
        User user = userRepository.findOneByUuidAndPassword(login.getUsername(), login.getPassword()).orElse(null);
        System.out.println(user);
        SessionResponse resp = new SessionResponse();
        SessionItem sessionItem = new SessionItem();

        System.out.println("准备登录");
        if (user != null){
            System.out.format("\n /User Details=%s\n", user.getName());
            sessionItem.setToken("xxx.xxx.xxx");
            sessionItem.setUserId(user.getUuid());
            sessionItem.setName(user.getName());
            sessionItem.setEmail(user.getEmail());
//            sessionItem.setRole(user.getRole());

            resp.setStatus(200);
            resp.setMessage("Dummy Login Success");
            resp.setData(sessionItem);
      }
      else{
            resp.setStatus(400);
            resp.setMessage("Login Failed");
      }

      return resp;
  }

}
