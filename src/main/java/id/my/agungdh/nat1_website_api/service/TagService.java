package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.mapper.TagMapper;
import id.my.agungdh.nat1_website_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

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
