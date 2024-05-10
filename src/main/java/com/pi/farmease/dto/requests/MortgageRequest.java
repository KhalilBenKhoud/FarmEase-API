package com.pi.farmease.dto.requests;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MortgageRequest {
    private String description_mortgage;
    private Long duration_mortgage;
    private Double prize_mortgage;
    private Double month_payment;
    private String category_mortgage;
    private String type_mortgage;
    private Long price_mortgage;
    private List<Long> materiels; // Assuming materiels are represented by their IDs
    private String land_description;
    private float interest;
}
