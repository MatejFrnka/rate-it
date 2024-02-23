package it.rate.webapp.dtos;

import it.rate.webapp.config.Constraints;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record SignupUserInDTO(
    @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email address")
        String email,
    @NotBlank(message = "Password cannot be empty")
        @Pattern(
            regexp = Constraints.PASSWORD_REGEX,
            message =
                "Password needs to be at least 8 characters long and contain at least one uppercase letter and one digit")
        String password,
    @NotBlank(message = "Username cannot be empty")
        @Length(
            min = Constraints.MIN_USERNAME_LENGTH,
            max = Constraints.MAX_USERNAME_LENGTH,
            message =
                "Username has to be between "
                    + Constraints.MIN_USERNAME_LENGTH
                    + " and "
                    + Constraints.MAX_USERNAME_LENGTH
                    + " characters long")
        String username) {}
