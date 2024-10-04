package ph.edu.dlsu.enlistment;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isAlphanumeric;
import static org.apache.commons.lang3.StringUtils.isBlank;
import org.apache.commons.lang3.Validate;
class Section{
    private final String sectionId;
    private final Schedule schedule;
    private final Room room;
    private final Subject subject;

    Section(String sectionId, Schedule schedule, Room room, Subject subject) {
        Objects.requireNonNull(sectionId);
        // Objects.requireNonNull(schedule);
        Objects.requireNonNull(room);
        Objects.requireNonNull(subject);

        isBlank(sectionId);
        Validate.isTrue(isAlphanumeric(sectionId), "sectionId must be alphanumeric, was: "
                + sectionId);
        this.sectionId = sectionId;
        this.schedule = schedule;
        this.room = room;
        this.subject = subject;
    }

    public boolean isConflict(Section otherSection) {
        return this.schedule.isConflict(otherSection.schedule);
    }

    public String getSectionId() {
        return sectionId;
    }

    public Room getRoom() {
        return room;
    }

    public Subject getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return sectionId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section section)) return false;

        return Objects.equals(sectionId, section.sectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sectionId);
    }






}
