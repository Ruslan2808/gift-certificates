package ru.clevertec.ecl.dto.tag;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {

    @Size(min = 1, message = "Name cannot be empty")
    private String name;

}
