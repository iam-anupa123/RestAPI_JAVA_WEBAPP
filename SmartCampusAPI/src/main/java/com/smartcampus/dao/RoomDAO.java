package com.smartcampus.dao;
import com.smartcampus.model.Room;
import java.util.*;
import java.util.stream.Collectors;

public class RoomDAO extends GenericDAO<Room> {
    @Override
    public void create(Room room) { storage.put(room.getId(), room); }
    @Override
    public Room getById(String id) { return (Room) storage.get(id); }
    @Override
    public List<Room> getAll() {
        return storage.values().stream().filter(o -> o instanceof Room)
                .map(o -> (Room) o).collect(Collectors.toList());
    }
    @Override
    public void delete(String id) { storage.remove(id); }
}