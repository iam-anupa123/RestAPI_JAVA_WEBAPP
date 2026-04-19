package com.smartcampus.model;

public class SensorReading {
    private long timestamp;
    private double value;

    public SensorReading() {}
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long t) { this.timestamp = t; }
    public double getValue() { return value; }
    public void setValue(double v) { this.value = v; }
}