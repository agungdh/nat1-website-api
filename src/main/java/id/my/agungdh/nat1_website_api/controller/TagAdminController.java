package id.my.agungdh.nat1_website_api.controller;

import id.my.agungdh.nat1_website_api.dto.PagedResponse;
import id.my.agungdh.nat1_website_api.dto.TagCreateDto;
import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.dto.TagUpdateDto;
import id.my.agungdh.nat1_website_api.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class TagAdminController {

    private final TagService tagService;

    @GetMapping
    public PagedResponse<TagDto> findAll(Pageable pageable) {
        return tagService.findAll(pageable);
    }

    @GetMapping("/{uuid}")
    public TagDto findByUuid(@PathVariable String uuid) {
        return tagService.findByUuid(uuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@Valid @RequestBody TagCreateDto dto) {
        return tagService.create(dto);
    }

    @PutMapping("/{uuid}")
    public TagDto update(@PathVariable String uuid, @Valid @RequestBody TagUpdateDto dto) {
        return tagService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String uuid) {
        tagService.delete(uuid);
    }
}
