package enlistment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {
    private Room room1;
    private Room room2;
    private Schedule schedule2;
    private Schedule schedule3;
    private Schedule schedule4;
    private Subject subject1;
    private Subject subject2;
    private Subject subject3;
    private Subject subject4;
    private Subject labSubject;
    private Section section1;
    private Section section2;
    private Section section3;
    private Section section4;
    private Section labSection;
    private Student student;

    @BeforeEach
    void setUp() {
        room1 = new Room("GK101", 2); // Room with capacity of 2
        room2 = new Room("GK102", 1); // Room with capacity of 1
        Schedule schedule1 = new Schedule("MT", "H8300");
        schedule2 = new Schedule("TF", "H1000");
        schedule3 = new Schedule("WS", "H1130");
        subject1 = new Subject("CSST101", 3, false, Collections.emptyList());
        subject2 = new Subject("CSST102", 3, false, Collections.emptyList());
        subject3 = new Subject("CSST103", 3, false, Collections.emptyList());
        subject4 = new Subject("CSST104", 3, false, Arrays.asList(subject1));
        labSubject = new Subject("LAB101", 2, true, Collections.emptyList());
        section1 = new Section("S101", schedule1, room1, subject1);
        section2 = new Section("S102", schedule2, room1, subject2);
        section3 = new Section("S103", schedule3, room2, subject3);
        section4 = new Section("S104", schedule4, room2, subject4);
        labSection = new Section("LAB101", schedule3, room2, labSubject);
        student = new Student(12345);
    }

    @Test
    void noConflictAvailable() {
        // Enlist the first section
        assertTrue(student.enlist(section1));

        // Verify the first section is enlisted
        assertEquals(1, student.getEnlistedSections().size());
        assertEquals(section1, student.getEnlistedSections().get(0));

        // Verify the room's current enrollment
        assertEquals(1, room1.getCurrentEnrollment());

        // Enlist the second section with no conflict and available capacity
        assertTrue(student.enlist(section2));

        // Verify the second section is enlisted
        assertEquals(2, student.getEnlistedSections().size());
        assertEquals(section2, student.getEnlistedSections().get(1));

        // Verify the room's current enrollment
        assertEquals(2, room1.getCurrentEnrollment());

        // Enlist the third section in a different room with available capacity
        assertTrue(student.enlist(section3));

        // Verify the third section is enlisted
        assertEquals(3, student.getEnlistedSections().size());
        assertEquals(section3, student.getEnlistedSections().get(2));

        // Verify the room's current enrollment
        assertEquals(1, room2.getCurrentEnrollment());
    }

    @Test
    void conflict() {
        // Enlist the first section
        assertTrue(student.enlist(section1));

        // Try to enlist another section with a conflicting schedule
        Schedule conflictingSchedule = new Schedule("MT", "H8300");
        Section conflictingSection = new Section("S104", conflictingSchedule, room2, subject3);
        assertFalse(student.enlist(conflictingSection));

        // Verify the conflicting section is not enlisted
        assertEquals(1, student.getEnlistedSections().size());
        assertEquals(section1, student.getEnlistedSections().get(0));
    }
}