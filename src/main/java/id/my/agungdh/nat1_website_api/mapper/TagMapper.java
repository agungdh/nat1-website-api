package id.my.agungdh.nat1_website_api.mapper;

import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag entity);
    List<TagDto> toDtoList(List<Tag> entities);
}
