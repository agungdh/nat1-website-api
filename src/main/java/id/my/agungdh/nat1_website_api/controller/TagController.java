package id.my.agungdh.nat1_website_api.controller;

import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<TagDto> findAll() {
        return tagService.findAll();
    }

    @GetMapping("/{uuid}")
    public TagDto findByUuid(@PathVariable String uuid) {
        return tagService.findByUuid(uuid);
    }

    @GetMapping("/slug/{slug}")
    public TagDto findBySlug(@PathVariable String slug) {
        return tagService.findBySlug(slug);
    }
}
