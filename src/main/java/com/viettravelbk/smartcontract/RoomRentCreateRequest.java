package com.viettravelbk.smartcontract;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RoomRentCreateRequest {
    private String roomRent;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Invalid Account Address")
    @NotBlank(message = "Account Address cannot be blank")
    private String renterAddress;
}
