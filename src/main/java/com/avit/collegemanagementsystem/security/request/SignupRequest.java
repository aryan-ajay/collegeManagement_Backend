package com.avit.collegemanagementsystem.security.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    private String name;
    private String phone;
    private String username;
    private String email;
    private Set<String> role;
    private String password;


}
