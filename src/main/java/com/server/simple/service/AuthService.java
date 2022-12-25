package com.server.simple.service;

import com.server.simple.domain.Session;
import com.server.simple.domain.User;
import com.server.simple.exception.InvalidSignInInformation;
import com.server.simple.repository.UserRepository;
import com.server.simple.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signin(Login login){
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSignInInformation::new);

        Session session = user.addSession();

        return session.getAccessToken();

    }

}
