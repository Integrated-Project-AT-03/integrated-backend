package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import com.example.itbangmodkradankanbanapi.repositories.user.UserDataCenterRepository;
import com.example.itbangmodkradankanbanapi.entities.user.UserdataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDataCenterRepository userDataCenterRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserdataEntity user = userDataCenterRepository.findByUsername(userName);
        if(user == null) {
            throw new UnauthorizedLoginException("Username or Password is incorrect"); }
        UserDetails userDetails = new AuthUser(userName, user.getPassword());
        return userDetails;
    }

}
