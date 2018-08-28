package org.iproduct.spring.restmvc.web;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.spring.restmvc.exception.InvalidEntityIdException;
import org.iproduct.spring.restmvc.model.User;
import org.iproduct.spring.restmvc.service.UserService;
import org.iproduct.spring.restmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> getUsers() {
        return service.getUsers();
    }

    @GetMapping("{id}")
    public User getUsers(@PathVariable String id) {
        return service.getUserById(id);
    }

    @DeleteMapping("{id}")
    public User deleteUsers(@PathVariable String id) {
        return service.deleteUser(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
//                                                 UriComponentsBuilder uriComponentsBuilder, HttpServletRequest req) {
        User created = service.createUser(user);
        URI location = MvcUriComponentsBuilder.fromMethodName(UserController.class, "createUser", User.class)
                .pathSegment("{id}").buildAndExpand(created.getId()).toUri() ;
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .pathSegment("{id}").buildAndExpand(created.getId()).toUri() ;
//        URI location = uriComponentsBuilder.path(req.getServletPath())
//                .pathSegment("{id}").buildAndExpand(created.getId()).toUri() ;
        log.info("User created: {}", location);
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> addUser(@PathVariable String id, @RequestBody User user) {
        if(!user.getId().equals(id)) throw new InvalidEntityIdException(
                String.format("User ID=%s from path is different from Entity ID=%s", id, user.getId()));
        User updated = service.updateUser(user);
        log.info("User updated: {}", updated);
        return ResponseEntity.ok(updated);
    }

}