package pl.szinton.querky.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterUserDto {
    @NotBlank
    @Size(min = 6, max = 20)
    @Pattern(regexp = "[A-Za-z0-9]+")
    private String username;
}
