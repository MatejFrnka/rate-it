package it.rate.webapp.models;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class RatingId implements Serializable {
    private Long userId;
    private Long placeId;
    private Long criterionId;

    public RatingId(Long userId, Long placeId, Long criterionId) {
        this.userId = userId;
        this.placeId = placeId;
        this.criterionId = criterionId;
    }
}
