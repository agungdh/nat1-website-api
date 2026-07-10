package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.dto.PostDto;
import id.my.agungdh.nat1_website_api.mapper.PostMapper;
import id.my.agungdh.nat1_website_api.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public List<PostDto> findAll() {
        return postMapper.toDtoList(postRepository.findAll());
    }

    public PostDto findByUuid(String uuid) {
        return postRepository.findByUuid(uuid)
                .map(postMapper::toDto)
                .orElse(null);
    }

    public PostDto findBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .map(postMapper::toDto)
                .orElse(null);
    }
}
