package it.rate.webapp.models;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Embeddable
public class RoleId implements Serializable {
    private Long userId;
    private Long interestId;

    public RoleId(Long userId, Long interestId) {
        this.userId = userId;
        this.interestId = interestId;
    }
}
