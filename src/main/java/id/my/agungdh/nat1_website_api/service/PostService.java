package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.dto.PagedResponse;
import id.my.agungdh.nat1_website_api.dto.PostDto;
import id.my.agungdh.nat1_website_api.entity.Post;
import id.my.agungdh.nat1_website_api.mapper.PostMapper;
import id.my.agungdh.nat1_website_api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PagedResponse<PostDto> findAll(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);
        return new PagedResponse<>(
                postMapper.toDtoList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
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

    public PagedResponse<PostDto> findAllByCategorySlug(String categorySlug, Pageable pageable) {
        Page<Post> page = postRepository.findAllByCategorySlug(categorySlug, pageable);
        return new PagedResponse<>(
                postMapper.toDtoList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    public PagedResponse<PostDto> findAllByTagSlug(String tagSlug, Pageable pageable) {
        Page<Post> page = postRepository.findAllByTagSlug(tagSlug, pageable);
        return new PagedResponse<>(
                postMapper.toDtoList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
