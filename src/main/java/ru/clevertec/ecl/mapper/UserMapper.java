package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

import java.util.List;

@Mapper(uses = OrderMapper.class)
public interface UserMapper {

    User mapToUser(UserRequest userRequest);
    User mapToUser(UserFilter userFilter);
    UserResponse mapToUserResponse(User user);
    UserOrderResponse mapToUserOrderResponse(Order order);
    List<UserResponse> mapToUserResponses(List<User> users);
    List<UserOrderResponse> mapToUserOrderResponses(List<Order> orders);
}
