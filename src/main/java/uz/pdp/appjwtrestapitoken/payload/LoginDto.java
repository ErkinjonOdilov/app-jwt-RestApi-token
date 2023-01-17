package uz.pdp.appjwtrestapitoken.payload;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
}
