package com.umi.tradestar.itch.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * ITCH message for order modifications
 * @author VrushankPatel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderModify extends ItchMessage {
    private String orderReference;
    private long newQuantity;
    private double newPrice;
} 