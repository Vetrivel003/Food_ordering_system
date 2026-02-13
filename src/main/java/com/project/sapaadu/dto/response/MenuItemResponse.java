package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuItemResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
}
