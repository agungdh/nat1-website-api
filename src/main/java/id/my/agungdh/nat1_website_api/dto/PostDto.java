package id.my.agungdh.nat1_website_api.dto;

import java.util.List;

public record PostDto(
        String uuid,
        String title,
        String slug,
        String content,
        String excerpt,
        String featuredImage,
        Long publishedAt,
        String status,
        CategoryDto category,
        List<TagDto> tags
) {}
