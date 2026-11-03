package com.scoretracker.dto.dashboard;

public class ChartDataPoint {
    private String name;
    private Number value;
    private Number secondaryValue;

    public ChartDataPoint() {
    }

    public ChartDataPoint(String name, Number value) {
        this.name = name;
        this.value = value;
    }

    public ChartDataPoint(String name, Number value, Number secondaryValue) {
        this.name = name;
        this.value = value;
        this.secondaryValue = secondaryValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public Number getSecondaryValue() {
        return secondaryValue;
    }

    public void setSecondaryValue(Number secondaryValue) {
        this.secondaryValue = secondaryValue;
    }
}

