package com.abdul.SpringApiResponse.registration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Registration {

    private String name;
    private String email;
    private String password;
}
