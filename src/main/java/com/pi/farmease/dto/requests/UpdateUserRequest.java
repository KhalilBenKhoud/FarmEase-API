package com.pi.farmease.dto.requests;


import com.pi.farmease.entities.enumerations.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String firstname ;
    private String lastname ;
    private String email ;
    private String password ;
    private Role role ;
}
