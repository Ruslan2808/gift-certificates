package ru.clevertec.ecl.util.tag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.util.TestBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "tagRequest")
public class TagRequestTestBuilder implements TestBuilder<TagRequest> {

    private String name = "";

    @Override
    public TagRequest build() {
        final TagRequest tagRequest = new TagRequest();
        tagRequest.setName(name);
        return tagRequest;
    }
}
