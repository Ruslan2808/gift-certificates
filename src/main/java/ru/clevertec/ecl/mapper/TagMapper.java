package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;

@Mapper
public interface TagMapper {
    Tag mapToTag(TagRequest tagRequest);
    Tag mapToTag(TagFilter tagFilter);
    TagResponse mapToTagResponse(Tag tag);
    List<Tag> mapToTags(List<TagRequest> tagRequests);
    List<TagResponse> mapToTagResponses(List<Tag> tags);
}
