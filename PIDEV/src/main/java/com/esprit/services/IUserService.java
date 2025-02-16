package com.esprit.services;


import java.util.List;

public interface IUserService<T> {
    boolean registerUser(T user);
    boolean isEmailExists(String email);
    boolean authenticateUser(String email, String password);
    String hashPassword(String password);
    T getUserById(int id);
    List<T> getAllUsers();
    boolean deleteUser(int id);
    boolean updateUser(T user);
}