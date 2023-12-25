package apisystem.posttraining.DTO.Account;

import lombok.Data;

@Data
public class RequestLogin {
    private String username;
    private String password;
}
