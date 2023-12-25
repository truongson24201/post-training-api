package apisystem.posttraining.service.Impl;

import apisystem.posttraining.entity.Account;
import apisystem.posttraining.exception.BaseException;
import apisystem.posttraining.repository.AccountRepository;
import apisystem.posttraining.service.IContextHolder;
import apisystem.posttraining.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContextHolderImpl implements IContextHolder {
    private final AccountRepository accountRepository;

    public Boolean checkAuthentication(Authentication authentication){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return true;
        }
        return false;
    }
    @Override
    public String getUsernameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!checkAuthentication(authentication)){
            throw new BaseException(HttpStatus.UNAUTHORIZED, MessageResponse.UN_AUTHENTICATION);
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public List<String> getRoleFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!checkAuthentication(authentication)){
            throw new BaseException(HttpStatus.UNAUTHORIZED, MessageResponse.UN_AUTHENTICATION);
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // You can modify the code above to extract specific roles as needed
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public Account getAccount() {
        String username = getUsernameFromContext();
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,"Your account not found!"));
    }
}
