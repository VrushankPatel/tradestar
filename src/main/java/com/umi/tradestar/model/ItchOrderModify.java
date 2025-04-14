package com.umi.tradestar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderModify {
    private String orderReference;
    private long quantity;
    private double price;
} 