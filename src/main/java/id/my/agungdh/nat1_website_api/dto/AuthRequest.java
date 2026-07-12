package id.my.agungdh.nat1_website_api.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
