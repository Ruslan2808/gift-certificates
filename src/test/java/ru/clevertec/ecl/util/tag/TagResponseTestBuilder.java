package ru.clevertec.ecl.util.tag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.util.TestBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "tagResponse")
public class TagResponseTestBuilder implements TestBuilder<TagResponse> {

    private Long id = 1L;
    private String name = "";

    @Override
    public TagResponse build() {
        final TagResponse tagResponse = new TagResponse();

        tagResponse.setId(id);
        tagResponse.setName(name);

        return tagResponse;
    }
}
