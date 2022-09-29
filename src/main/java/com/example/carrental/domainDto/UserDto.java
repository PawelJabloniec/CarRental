package com.example.carrental.domainDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserDto {

    private String userLogin;
    private String userPassword;
    private String userName;
    private String userLastName;
    private String userEMail;
    private String userAddress;
    private Long userCarId;
    private String role;
    private String status;

}
