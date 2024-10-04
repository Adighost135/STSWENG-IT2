package ph.edu.dlsu.enlistment;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isAlphanumeric;

class Room {
    private final String roomName;
    private final int capacity;
    private int currentEnrollment = 0;

    Room (String roomName, int capacity){
        Objects.requireNonNull(roomName);


        Validate.isTrue(isAlphanumeric(roomName), "roomName must be alphanumeric, was: "
                + roomName);
        Validate.isTrue(capacity > 0, "capacity must be greater than zero, was: "
                + capacity);
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public boolean canEnlist() {
        return currentEnrollment < capacity;
    }

    public boolean enlist() {
        if (canEnlist()) {
            currentEnrollment++;
            return true;
        }
        return false;
    }

    public void cancel() {
        if (currentEnrollment > 0) {
            currentEnrollment--;
        }
    }

    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString(){
        return roomName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;

        return Objects.equals(roomName, room.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomName);
    }
}
