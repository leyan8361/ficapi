package com.fic.service.controller.api.v1;

import com.fic.service.Vo.ResponseVo;
import com.fic.service.controller.BootPageController;
import com.fic.service.service.BootPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bootPage")
@Api(description = "Api-获取引导页")
public class ApiBootPageController {

    private final Logger log = LoggerFactory.getLogger(BootPageController.class);

    @Autowired
    BootPageService bootPageService;

    @GetMapping("/getPages")
    @ApiOperation("获取需要Show的引导页")
    public ResponseEntity getAll() {
        log.debug(" bootPage getPages !!!");
        ResponseVo result = bootPageService.getPages();
        return ResponseEntity.ok(result);
    }


}
