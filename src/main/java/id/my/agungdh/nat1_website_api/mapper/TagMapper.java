package id.my.agungdh.nat1_website_api.mapper;

import id.my.agungdh.nat1_website_api.dto.TagCreateDto;
import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.dto.TagUpdateDto;
import id.my.agungdh.nat1_website_api.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag entity);
    List<TagDto> toDtoList(List<Tag> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "posts", ignore = true)
    Tag toEntity(TagCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "posts", ignore = true)
    void updateEntity(TagUpdateDto dto, @MappingTarget Tag entity);
}
