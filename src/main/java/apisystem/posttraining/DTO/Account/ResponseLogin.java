package apisystem.posttraining.DTO.Account;

import apisystem.posttraining.entity.enumreration.ERole;
import lombok.Data;

import java.util.List;

@Data
public class ResponseLogin {
    private String username;
    private String accessToken;
    private List<ERole> roles;

    public ResponseLogin(String username, String accessToken, List<ERole> roles) {
        this.username = username;
        this.accessToken = accessToken;
        this.roles = roles;
    }

    public ResponseLogin() {
    }
}
