package com.tfms.service;

import com.tfms.model.User;

import java.util.List;

public interface UserService {
    
    List<User> getAllUsers();
    
    User getUserById(Long id);
    
    User getUserByUsername(String username);
    
    User createUser(User user);
    
    User updateUser(Long id, User userDetails);
    
    void deleteUser(Long id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}