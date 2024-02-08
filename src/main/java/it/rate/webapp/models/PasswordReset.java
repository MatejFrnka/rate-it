package it.rate.webapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordReset {
  @Id @GeneratedValue private Long id;

  @NotBlank
  @Column(nullable = false)
  private String token;

  @OneToOne private AppUser user;

  private static final int EXPIRATION_MINUTES = 60;
  private LocalDateTime expiration;

  public PasswordReset(AppUser user, String token) {
    this.user = user;
    this.token = token;
    this.expiration  = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
  }

  public void updateToken(String token) {
    this.token = token;
    this.expiration = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
  }

  public static String getFormattedExpiration() {
    StringBuilder sb = new StringBuilder();
    int hours = EXPIRATION_MINUTES / 60;
    int minutes = EXPIRATION_MINUTES % 60;
    sb.append(hours > 0 ? hours + " hour" : "").append(hours > 1 ? "s" : "");
    if (hours > 0 && minutes > 0) {
      sb.append(" and ");
    }
    sb.append(minutes > 0 ? minutes + " minute" : "").append(minutes > 1 ? "s" : "");
    return sb.toString();
  }
}
