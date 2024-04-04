package com.impacto.visual.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    int id;
    String username;
    String persontype;
    String documenttype;
    String documentnumber;
    String firstname;
    String lastname;
    String address;
    String email;
    String phone;
}