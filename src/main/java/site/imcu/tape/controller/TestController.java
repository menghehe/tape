package site.imcu.tape.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.imcu.tape.pojo.ResponseData;

/**
 * @author: MengHe
 * @date: 2020/2/27 14:52
 */
//@RestController
public class TestController {

//    @PostMapping("/user/login")
    public ResponseData login(){
        return ResponseData.builder().code(-1).message("cc").build();
    }
}
