package id.my.agungdh.nat1_website_api.dto;

import jakarta.validation.constraints.NotBlank;

public record TagCreateDto(
        @NotBlank String slug,
        @NotBlank String name
) {}
