package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import com.example.itbangmodkradankanbanapi.repositories.V2.userShare.UserDataRepository;
import com.example.itbangmodkradankanbanapi.entities.V2.userShare.UserdataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDataRepository userDataRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserdataEntity user = userDataRepository.findByUsername(userName);
        if(user == null) {
            throw new UnauthorizedLoginException("Username or Password is incorrect"); }
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
