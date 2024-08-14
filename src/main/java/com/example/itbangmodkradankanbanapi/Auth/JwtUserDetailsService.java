package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.user.UserDataRepository;
import com.example.itbangmodkradankanbanapi.user.UserdataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDataRepository userDataRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserdataEntity user = userDataRepository.findByUsername(userName);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, userName+ " does not exist !!"); }
//        List<GrantedAuthority> roles = new ArrayList<>();
//        GrantedAuthority grantedAuthority = new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getRole();
//            }
//        };
//        roles.add(grantedAuthority);

        UserDetails userDetails = new AuthUser(userName, user.getPassword());
        return userDetails;
    }
}
