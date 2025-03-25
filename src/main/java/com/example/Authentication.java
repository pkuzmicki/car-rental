package com.example;

public class Authentication {

    private UserRepository userRepository;
    private User currentUser;
    public Authentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean authenticate(String login, String password) {
        for (User user : userRepository.getUsers()) {
            //System.out.println(user.password +" "+ password);
            if (login.equals(user.login)) {
                if (password.equals(user.password)) {
                    currentUser = user;
                    return true;
                }
            }
        }
        return false;
    }
}
