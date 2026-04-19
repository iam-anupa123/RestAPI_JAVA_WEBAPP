package com.smartcampus.dao;

import com.smartcampus.model.Sensor;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO extends GenericDAO<Sensor> {

    @Override
    public void create(Sensor sensor) {
        // Adds a new sensor to the static map using its ID as the key
        storage.put(sensor.getId(), sensor);
    }

    @Override
    public Sensor getById(String id) {
        // Retrieves a sensor and casts it back to the Sensor type
        return (Sensor) storage.get(id);
    }

    @Override
    public List<Sensor> getAll() {
        List<Sensor> sensors = new ArrayList<>();
        // Loops through the static storage to find all objects that are instances of Sensor
        for (Object obj : storage.values()) {
            if (obj instanceof Sensor) {
                sensors.add((Sensor) obj);
            }
        }
        return sensors;
    }

    @Override
    public void delete(String id) {
        // Removes the sensor from memory
        storage.remove(id);
    }
}