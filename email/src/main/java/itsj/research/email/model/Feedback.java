package itsj.research.email.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Feedback {

    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Min(4)
    private String feedback;
}
