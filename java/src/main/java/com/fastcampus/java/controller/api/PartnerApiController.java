package com.fastcampus.java.controller.api;

import com.fastcampus.java.controller.CrudController;
import com.fastcampus.java.model.entity.Partner;
import com.fastcampus.java.model.network.request.PartnerAPiRequest;
import com.fastcampus.java.model.network.response.PartnerAPiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partner")
public class PartnerApiController extends CrudController<PartnerAPiRequest, PartnerAPiResponse, Partner> {

}
