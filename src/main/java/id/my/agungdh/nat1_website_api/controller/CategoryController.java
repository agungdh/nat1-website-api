package id.my.agungdh.nat1_website_api.controller;

import id.my.agungdh.nat1_website_api.dto.CategoryDto;
import id.my.agungdh.nat1_website_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{uuid}")
    public CategoryDto findByUuid(@PathVariable String uuid) {
        return categoryService.findByUuid(uuid);
    }

    @GetMapping("/slug/{slug}")
    public CategoryDto findBySlug(@PathVariable String slug) {
        return categoryService.findBySlug(slug);
    }
}
