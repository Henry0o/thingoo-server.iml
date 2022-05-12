package com.nuttu.aicloud.api.user;

import com.google.common.base.Strings;
import com.nuttu.aicloud.model.response.OperationResponse;
import com.nuttu.aicloud.model.response.PageResponse;
import com.nuttu.aicloud.model.user.User;
import com.nuttu.aicloud.model.user.Role;
import com.nuttu.aicloud.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@Api(tags = { "User" })
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    //TODO:: find users by email and name
    private Specification<User> getWhereClause(String email, String name) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (!email.isEmpty()) {
                    predicate.getExpressions().add(
                            cb.like(r.<String>get("email"), "%" + email + "%")
                    );

                }

                if (!name.isEmpty()) {
                    predicate.getExpressions().add(
                            cb.like(r.<String>get("name"), "%" + name + "%")
                    );
                }
                return predicate;
            }
        };
    }

    @ApiOperation(value = "Gets user list information", response = Page.class)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public PageResponse getUserList(
            @ApiParam(value = "") @RequestParam(value = "email", defaultValue = "", required = false) String email,
            @ApiParam(value = "") @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @ApiParam(value = "") @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "20", required = false) Integer size,
            @ApiParam(value = "") @RequestParam(value = "sort", defaultValue = "createdAt", required = false) String sort,
            Pageable pageable) {
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);
        Page<User> r;
        if (email.isEmpty() && name.isEmpty())
            r = userRepository.findAll(pageable);
        else {
            //
            log.info("Find users by email: {} and name: {}\n", email, name);
            r = userRepository.findAll(Specifications.where(getWhereClause(email, name)), pageable);
        }
        resp.setPageStats(r, true);
        return resp;
    }

    @ApiOperation(value = "Add new user", response = OperationResponse.class)
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = { "application/json" })
    public OperationResponse postUser(@RequestBody User user, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        // user.setUserId(loggedInUserId);
        // resp.setLoggedInUserId(loggedInUserId);
        user.setActive(true);
        User r = userRepository.save(user);

        if (r.getUuid() != "") {
            resp.setStatus(200);
            resp.setMessage("Record Added");
            resp.setData(r);
        } else {
            resp.setStatus(400);
            resp.setMessage("Unable to add Record");
        }
        return resp;
    }

    @ApiOperation(value = "Add new user", response = OperationResponse.class)
    @RequestMapping(value = "/account/register", method = RequestMethod.POST, produces = { "application/json" })
    public OperationResponse newUser(@RequestBody User user, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String email = user.getEmail();
        User u = userRepository.findOneByEmail(email).orElseGet(() -> new User());
        System.out.println("user:==" + u);
        if (u.getEmail() == "") {
            if(user.getRole()==Role.UNKNOWN) user.setRole(Role.USER);    //缺省USESR权限
            User r = userRepository.save(user);
            if (r.getUuid() != "") {
                resp.setStatus(200);
                resp.setMessage("注册成功");
                resp.setData(r);
            } else {
                resp.setStatus(400);
                resp.setMessage("用户注册失败");
            }
        } else {
            resp.setStatus(450);
            resp.setMessage("该用户已存在");
        }
        return resp;
    }

    @ApiOperation(value = "Gets current user information", response = OperationResponse.class)
    @RequestMapping(value = "/users/{uuid}", method = RequestMethod.GET, produces = { "application/json" })
    public OperationResponse getUser(@PathVariable String uuid, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);

        User user;
        boolean provideUserDetails = false;

        if (Strings.isNullOrEmpty(uuid)) {
            provideUserDetails = true;
            user = userService.getLoggedInUser();
        } else if (loggedInUserId.equals(uuid)) {
            provideUserDetails = true;
            user = userService.getLoggedInUser();
        } else {
            // TODO: Check if the current user is superuser then provide the details of requested user
            provideUserDetails = true;
            user = userRepository.findOneByUuid(uuid).orElse(null);
        }

        if (provideUserDetails && user != null) {
            resp.setStatus(200);
            resp.setData(user);
        } else if (user == null) {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        } else {
            resp.setStatus(401);
            log.info("401, provideUserDetails={}\n", provideUserDetails);
            resp.setMessage("No Access");
        }

        return resp;
    }

    @ApiOperation(value = "Edit current user information", response = OperationResponse.class)
    @RequestMapping(value = "/users/{uuid}", method = RequestMethod.PUT)
    public OperationResponse putUser(@PathVariable String uuid, @RequestBody User user) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);

        //这里权限问题，USER==>ADMIN之类的当前没有考虑，需要更多的考虑
        if (userRepository.exists(uuid)) {  //用户存在
            User cu = userRepository.findOneByEmail(user.getEmail()).orElseGet(() -> null);
            if (cu!=null && !cu.getUuid().equals(uuid)) {   //email存在且不是该用户的email，也就是修改了email且已有其他用户使用该email
                resp.setStatus(450);
                resp.setMessage("Duplicate email");
            } else {
                if (cu==null) cu = userRepository.findOne(uuid);

                if (user.getRole()==Role.UNKNOWN) user.setRole(cu.getRole());
                if (user.getEmail().isEmpty()) user.setEmail(cu.getEmail());
                if (user.getName().isEmpty()) user.setName(cu.getName());
                if (user.getPassword()==null || user.getPassword().isEmpty()) user.setPassword(cu.getPassword());

                user.setUuid(uuid);
                user.setActive(cu.isActive());
                User r = this.userRepository.save(user);
                resp.setStatus(200);
                resp.setMessage("Record Updated");
                resp.setData(r);
            }
        } else {    //无此用户
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Delete current user", response = OperationResponse.class)
    @RequestMapping(value = "/users/{uuid}", method = RequestMethod.DELETE)
    public OperationResponse deleteUser(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);

        if (this.userRepository.exists(uuid)) {
            User r = userRepository.findOneByUuid(uuid).orElse(null);
            this.userRepository.delete(uuid);
            resp.setStatus(200);
            resp.setMessage("Record Deleted");
            resp.setData(r);
        } else {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }
        return resp;
    }

    @RequestMapping(value = "/account/activate", method = RequestMethod.GET)
    public OperationResponse actiUser(HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String email = req.getParameter("email");
        User u = userRepository.findOneByEmail(email).orElseGet(() -> new User());

        System.out.println(u);
        if (u != null) {
            if (u.isActive() == false)
                u.setActive(true);
            User r = userRepository.save(u);

            if (r.getUuid() != "") {
                resp.setStatus(200);
                resp.setMessage("Record Updated");
            } else {
                resp.setStatus(400);
                resp.setMessage("Unable to Update Record");
            }
        } else {
            resp.setStatus(450);
            resp.setMessage("The mailbox is not registered (the mailbox address does not exist)!");
        }
        return resp;
    }

    @RequestMapping(value = "/account/deactivate", method = RequestMethod.GET)
    public OperationResponse deactiUser(HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String email = req.getParameter("email");
        User u = userRepository.findOneByEmail(email).orElseGet(() -> new User());

        System.out.println(u);
        if (u != null) {
            if (u.isActive() == true)
                u.setActive(false);
            User r = userRepository.save(u);

            if (r.getUuid() != "") {
                resp.setStatus(200);
                resp.setMessage("Record Updated");
            } else {
                resp.setStatus(400);
                resp.setMessage("Unable to Update Record");
            }
        } else {
            resp.setStatus(450);
            resp.setMessage("The mailbox is not registered (the mailbox address does not exist)!");
        }
        return resp;
    }

}
