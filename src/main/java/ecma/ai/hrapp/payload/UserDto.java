package ecma.ai.hrapp.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull
    private String username; //official

    @Email
    private String email;//alibekxudoyqulovofficial@gmail.com

    @NotNull
    private String position;//ROLE_MANAGER

    @NotNull
    private Integer roleId; //3 STAFF 2 manager
}
