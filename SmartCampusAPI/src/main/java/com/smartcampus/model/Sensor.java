package com.smartcampus.model;
import java.util.*;

public class Sensor {
    private String id;
    private String type;
    private String roomId;
    private double currentValue;
    private List<SensorReading> readings = new ArrayList<>();

    public Sensor() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double val) { this.currentValue = val; }
    public List<SensorReading> getReadings() { return readings; }
}