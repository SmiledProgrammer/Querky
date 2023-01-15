package pl.szinton.querky.service.rest;

public interface IUserService {

    // Temporary
    void createUser(String email);

    boolean userExists(String email);
}
