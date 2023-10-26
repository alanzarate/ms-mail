package com.ucb.edu.msmail.dto;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class EmailOrderDto {
    String item;
    Double amount;
}
