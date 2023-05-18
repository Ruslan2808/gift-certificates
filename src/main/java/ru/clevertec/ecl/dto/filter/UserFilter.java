package ru.clevertec.ecl.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter {

    private String username;
    private String firstName;
    private String lastName;
    private String email;

}
