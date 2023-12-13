package it.rate.webapp.dtos;

public record SignupUserOutDTO(String email, String username) {
  public SignupUserOutDTO(SignupUserInDTO userDTO) {
    this(userDTO.email(), userDTO.username());
  }
}
