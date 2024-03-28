package com.impacto.visual.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    String username;
    String password;
    String persontype;
    String documenttype;
    String documentnumber;
    String firstname;
    String lastname;
    String address; 
    String email;
    String phone;
}
