package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;

import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag mapToTag(TagRequest tagRequest);
    TagResponse mapToTagResponse(Tag tag);
    List<Tag> mapToTags(List<TagRequest> tagRequests);
    List<TagResponse> mapToTagResponses(List<Tag> tags);
}
