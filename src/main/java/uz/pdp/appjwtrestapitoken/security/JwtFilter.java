package uz.pdp.appjwtrestapitoken.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.appjwtrestapitoken.service.MyAuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MyAuthService myAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
      //requestdan token olish
        String token = httpServletRequest.getHeader("Authorization");
        //request bearer dan boshlanganini tekshrdik
        if(token !=null && token.startsWith("Bearer")){
            //aynan tokeni ozini qirqib oldik
            token=token.substring(7);
            //tokenni validatsyadan otqazdik(Token buzulmaganligi hack qilinmaganligini tekshradi)
            boolean validateToken = jwtProvider.validateToken(token);
            if(validateToken){
                //tokenni ichidan usernameni oldik
                String userName = jwtProvider.getUserNameFromToken(token);
                //username orqali userdetailsni oldik
                UserDetails userDetails = myAuthService.loadUserByUsername(userName);
                //userdetails orqali authentication yaratib oldik
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //systemaga kim kirganini ornatdik
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
