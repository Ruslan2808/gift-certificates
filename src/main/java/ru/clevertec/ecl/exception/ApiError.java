package ru.clevertec.ecl.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private Integer status;
    private HttpStatus type;
    private List<String> messages;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timestamp;

}
