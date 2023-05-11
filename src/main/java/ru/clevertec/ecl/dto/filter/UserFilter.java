package ru.clevertec.ecl.dto.filter;

import lombok.Data;

@Data
public class UserFilter {

    private String username;
    private String firstName;
    private String lastName;
    private String email;

}
