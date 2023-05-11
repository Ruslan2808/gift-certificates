package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.dto.response.OrderUserResponse;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

import java.util.List;

@Mapper(uses = GiftCertificateMapper.class)
public interface OrderMapper {
    Order mapToOrder(OrderFilter orderFilter);
    OrderResponse mapToOrderResponse(Order order);
    OrderUserResponse mapToOrderUserResponse(User user);
    List<OrderResponse> mapToOrderResponses(List<Order> orders);
}
