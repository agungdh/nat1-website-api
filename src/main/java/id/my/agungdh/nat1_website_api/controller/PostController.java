package id.my.agungdh.nat1_website_api.controller;

import id.my.agungdh.nat1_website_api.dto.PostDto;
import id.my.agungdh.nat1_website_api.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> findAll() {
        return postService.findAll();
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
