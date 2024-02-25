package com.pi.farmease.dto.requests;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String email ;
    private String password ;
}
