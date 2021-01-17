package com.fastcampus.java.controller.api;

import com.fastcampus.java.controller.CrudController;
import com.fastcampus.java.model.entity.OrderDetail;
import com.fastcampus.java.model.network.request.OrderDetailApiRequest;
import com.fastcampus.java.model.network.response.OrderDetailApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orderDetail")
public class OrderDetailApiController extends CrudController<OrderDetailApiRequest, OrderDetailApiResponse, OrderDetail> {

}
