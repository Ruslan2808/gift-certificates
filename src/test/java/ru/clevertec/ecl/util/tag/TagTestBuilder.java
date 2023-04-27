package ru.clevertec.ecl.util.tag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.util.TestBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "tag")
public class TagTestBuilder implements TestBuilder<Tag> {

    private Long id = 1L;
    private String name = "";

    @Override
    public Tag build() {
        final Tag tag = new Tag();

        tag.setId(id);
        tag.setName(name);

        return tag;
    }
}
