package ru.clevertec.ecl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserResponse {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

}
