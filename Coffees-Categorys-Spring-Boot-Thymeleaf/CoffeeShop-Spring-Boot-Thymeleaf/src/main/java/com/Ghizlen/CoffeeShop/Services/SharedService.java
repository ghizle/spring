package com.Ghizlen.CoffeeShop.Services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SharedService {

    public boolean isAdmin() {
        boolean isAdmin= false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            for (String role : roles) {
                if (role.equals("ROLE_ADMIN")) isAdmin = true;
            }
        }
        return isAdmin;
    }
}
