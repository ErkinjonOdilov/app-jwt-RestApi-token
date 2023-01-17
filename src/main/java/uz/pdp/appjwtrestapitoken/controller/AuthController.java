package uz.pdp.appjwtrestapitoken.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.appjwtrestapitoken.payload.LoginDto;
import uz.pdp.appjwtrestapitoken.security.JwtProvider;
import uz.pdp.appjwtrestapitoken.service.MyAuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    MyAuthService myAuthService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public HttpEntity<?> loginToSystem(@RequestBody LoginDto loginDto) {
        try {
//        UserDetails userDetails = myAuthService.loadUserByUsername(loginDto.getUsername());
//        boolean matches = passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword());
//        if(matches){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            String token = jwtProvider.generateToken(loginDto.getUsername());
            return ResponseEntity.ok(token);
//            return ResponseEntity.ok(jwtProvider.generateToken(userDetails.getUsername()));
//       }
        } catch (BadCredentialsException exception) {

            return ResponseEntity.status(401).body("Login yoki Parol Xato");
        }
    }
}