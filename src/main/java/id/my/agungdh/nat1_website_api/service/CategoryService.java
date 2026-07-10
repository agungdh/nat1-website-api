package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.dto.CategoryDto;
import id.my.agungdh.nat1_website_api.mapper.CategoryMapper;
import id.my.agungdh.nat1_website_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> findAll() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    public CategoryDto findByUuid(String uuid) {
        return categoryRepository.findByUuid(uuid)
                .map(categoryMapper::toDto)
                .orElse(null);
    }

    public CategoryDto findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(categoryMapper::toDto)
                .orElse(null);
    }
}
