package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.mapper.TagMapper;
import id.my.agungdh.nat1_website_api.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    public List<TagDto> findAll() {
        return tagMapper.toDtoList(tagRepository.findAll());
    }

    public TagDto findByUuid(String uuid) {
        return tagRepository.findByUuid(uuid)
                .map(tagMapper::toDto)
                .orElse(null);
    }

    public TagDto findBySlug(String slug) {
        return tagRepository.findBySlug(slug)
                .map(tagMapper::toDto)
                .orElse(null);
    }
}
