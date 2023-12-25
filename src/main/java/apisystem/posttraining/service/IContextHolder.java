package apisystem.posttraining.service;

import apisystem.posttraining.entity.Account;

import java.util.List;

public interface IContextHolder {
    public String getUsernameFromContext();
    public List<String> getRoleFromContext();
    //    public AdminManager getManager();
    public Account getAccount();
}
