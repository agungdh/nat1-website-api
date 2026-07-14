package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.dto.TagCreateDto;
import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.dto.TagUpdateDto;
import id.my.agungdh.nat1_website_api.entity.Tag;
import id.my.agungdh.nat1_website_api.mapper.TagMapper;
import id.my.agungdh.nat1_website_api.repository.TagRepository;
import id.my.agungdh.nat1_website_api.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagDto> findAll() {
        return tagMapper.toDtoList(tagRepository.findAll());
    }

    public PagedResponse<TagDto> findAll(Pageable pageable) {
        Page<Tag> page = tagRepository.findAll(pageable);
        return new PagedResponse<>(
                tagMapper.toDtoList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
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

    public TagDto create(TagCreateDto dto) {
        Tag tag = tagMapper.toEntity(dto);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    public TagDto update(String uuid, TagUpdateDto dto) {
        Tag tag = tagRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tagMapper.updateEntity(dto, tag);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    public void delete(String uuid) {
        Tag tag = tagRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tag.softDelete(null);
        tagRepository.save(tag);
    }
}
