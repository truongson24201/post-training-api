package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.Account.RequestLogin;
import apisystem.posttraining.service.ICommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final ICommonService commonService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody RequestLogin requestLogin){
        return commonService.login(requestLogin);
    }
}
