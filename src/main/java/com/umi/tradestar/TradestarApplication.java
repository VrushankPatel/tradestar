package com.umi.tradestar;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Tradestar Trading Market Gateway Simulator.
 * This application simulates market behavior and helps test order flow using FIX, OUCH, and ITCH protocols.
 *
 * @author VrushankPatel
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Tradestar - Trading Market Gateway Simulator API",
        version = "1.0.0",
        description = "A backend simulator that exposes REST APIs to mimic market behavior and help test order flow using FIX, OUCH, and ITCH protocols.",
        contact = @Contact(
            name = "Vrushank Patel",
            email = "vrushankpatel5@gmail.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    )
)
public class TradestarApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradestarApplication.class, args);
    }
}