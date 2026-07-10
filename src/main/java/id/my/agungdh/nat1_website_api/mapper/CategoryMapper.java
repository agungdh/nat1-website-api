package id.my.agungdh.nat1_website_api.mapper;

import id.my.agungdh.nat1_website_api.dto.CategoryDto;
import id.my.agungdh.nat1_website_api.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category entity);
    List<CategoryDto> toDtoList(List<Category> entities);
}
