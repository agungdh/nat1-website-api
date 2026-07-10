package id.my.agungdh.nat1_website_api.mapper;

import id.my.agungdh.nat1_website_api.dto.PostDto;
import id.my.agungdh.nat1_website_api.entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, TagMapper.class})
public interface PostMapper {
    PostDto toDto(Post entity);
    List<PostDto> toDtoList(List<Post> entities);
}
