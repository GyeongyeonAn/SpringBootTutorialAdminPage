package com.fastcampus.java.controller.api;

import com.fastcampus.java.controller.CrudController;
import com.fastcampus.java.model.entity.AdminUser;
import com.fastcampus.java.model.network.request.AdminUserApiRequest;
import com.fastcampus.java.model.network.response.AdminUserApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminUser")
public class AdminUserApiController extends CrudController<AdminUserApiRequest, AdminUserApiResponse, AdminUser> {

}
