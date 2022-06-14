package pl.szinton.querky.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szinton.querky.dto.RegisterUserDto;
import pl.szinton.querky.dto.UserDto;
import pl.szinton.querky.entity.User;
import pl.szinton.querky.exception.ResourceNotFoundException;
import pl.szinton.querky.repo.UserRepository;
import pl.szinton.querky.utils.OAuth2Utils;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registerUser(@Valid RegisterUserDto userDto) {
        String email = OAuth2Utils.getCurrentUserEmail();
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
}
