package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ViolationDTO {
    @JsonProperty("type")
    private String type;
    @JsonProperty("amount")
    private long amount;

    public ViolationDTO(String type, long amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ViolationDTO{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
