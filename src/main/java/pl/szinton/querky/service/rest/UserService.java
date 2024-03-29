package pl.szinton.querky.service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szinton.querky.dto.RegisterUserDto;
import pl.szinton.querky.dto.UserDto;
import pl.szinton.querky.entity.User;
import pl.szinton.querky.exception.ResourceNotFoundException;
import pl.szinton.querky.repo.UserRepository;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    // Temporary
    @Override
    public void createUser(String email) {
        User newUser = User.builder()
                .username(email)
                .email(email)
                .build();
        userRepository.save(newUser);
    }

    // For future use case
    public void registerUser(@Valid RegisterUserDto userDto) {
//        String email = OAuth2Utils.getCurrentUserEmail();
        String email = ""; // TODO: fix
        User newUser = User.builder()
                .username(userDto.getUsername())
                .email(email)
                .build();
        userRepository.save(newUser);
    }

    public UserDto getUser(Long id) {
        User userRecord = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Could not find user with ID of \"" + id + "\"."));
        // TODO: map User to UserDto
        return null;
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
