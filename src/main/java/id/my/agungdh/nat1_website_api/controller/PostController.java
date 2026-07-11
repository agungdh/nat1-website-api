package id.my.agungdh.nat1_website_api.controller;

import id.my.agungdh.nat1_website_api.dto.PagedResponse;
import id.my.agungdh.nat1_website_api.dto.PostDto;
import id.my.agungdh.nat1_website_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public PagedResponse<PostDto> findAll(Pageable pageable) {
        return postService.findAll(pageable);
    }

    @GetMapping("/{uuid}")
    public PostDto findByUuid(@PathVariable String uuid) {
        return postService.findByUuid(uuid);
    }

    @GetMapping("/slug/{slug}")
    public PostDto findBySlug(@PathVariable String slug) {
        return postService.findBySlug(slug);
    }
}
