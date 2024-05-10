package com.pi.farmease.dto.requests;
import com.pi.farmease.entities.enumerations.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    private String firstname ;
    private String lastname ;
    private String email ;
    private String password ;
    private Role role ;
}