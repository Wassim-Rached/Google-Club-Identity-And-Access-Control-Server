package com.ics.services;


import com.ics.enums.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("securityService")
public class SecurityService {
    public boolean hasAuthority(Authority ...authorities){

        // get the authorities of the current user
        Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        // check if the user has the required authority
        for( Authority authority : authorities){
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
            if(userAuthorities.contains(simpleGrantedAuthority)){
                return true;
            }
        }

        // return false if the user does not have the required authority
        return false;
    }
}