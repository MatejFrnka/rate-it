package it.rate.webapp.dtos;

import it.rate.webapp.models.Role;

public record InterestUserDTO(Long userId, String userName) {
    public InterestUserDTO(Role role) {
        this(role.getAppUser().getId(), role.getAppUser().getUsername());
    }
}
