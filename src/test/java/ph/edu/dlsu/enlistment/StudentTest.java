package ph.edu.dlsu.enlistment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ph.edu.dlsu.enlistment.Days.MTH;
import static ph.edu.dlsu.enlistment.Days.TF;
import static ph.edu.dlsu.enlistment.Days.WS;
import static ph.edu.dlsu.enlistment.Period.H0830;
import static ph.edu.dlsu.enlistment.Period.H1000;
import static ph.edu.dlsu.enlistment.Period.H1130;

class StudentTest {
    private Room room1;
    private Room room2;
    private Schedule schedule1;
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
        schedule1 = new Schedule(MTH, H0830);
        schedule2 = new Schedule(TF, H1000);
        schedule3 = new Schedule(WS, H1130);
        schedule4 = new Schedule(MTH, H1000); 
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
        Schedule conflictingSchedule = new Schedule(MTH, H0830);
        Section conflictingSection = new Section("S104", conflictingSchedule, room2, subject3);
        assertFalse(student.enlist(conflictingSection));

        // Verify the conflicting section is not enlisted
        assertEquals(1, student.getEnlistedSections().size());
        assertEquals(section1, student.getEnlistedSections().get(0));
    }

    @Test
    void exceededCapacity() {
        // Enlist the first section
        assertTrue(student.enlist(section1));

        // Enlist another student in the same section to reach capacity
        Student anotherStudent = new Student(67890);
        assertTrue(anotherStudent.enlist(section1));

        // Try to enlist a third student in the same section, exceeding capacity
        Student thirdStudent = new Student(11111);
        assertFalse(thirdStudent.enlist(section1));

        // Verify the third student is not enlisted
        assertEquals(0, thirdStudent.getEnlistedSections().size());
    }

    @Test
    void cancel() {
        // Enlist the first section
        assertTrue(student.enlist(section1));

        // Verify the section is enlisted
        assertEquals(1, student.getEnlistedSections().size());
        assertEquals(section1, student.getEnlistedSections().get(0));

        // Cancel the enlistment
        assertTrue(student.cancelSection("S101"));

        // Verify the section is canceled
        assertEquals(0, student.getEnlistedSections().size());

        // Verify the room's current enrollment is updated
        assertEquals(0, room1.getCurrentEnrollment());

        // Try to cancel a section that is not enlisted
        assertFalse(student.cancelSection("S101"));
    }
    @Test
    void prerequisites() {
        // Try to enlist in a section without completing prerequisites
        assertFalse(student.enlist(section4));

        // Complete the prerequisite subject
        student.completeSubject(subject1);

        // Enlist in the section after completing prerequisites
        assertTrue(student.enlist(section2));

        // Verify the section is enlisted
        assertEquals(1, student.getEnlistedSections().size());
        assertEquals(section2, student.getEnlistedSections().get(0));
    }

    @Test
    void sameSubs() {
        // Enlist the first section
        assertTrue(student.enlist(section1));

        // Try to enlist another section with the same subject
        Section sameSubjectSection = new Section("CS105", schedule2, room2, subject1);
        assertFalse(student.enlist(sameSubjectSection));

        // Verify the section with the same subject is not enlisted
        assertEquals(1, student.getEnlistedSections().size());
        assertEquals(section1, student.getEnlistedSections().get(0));
    }

    @Test
    void assessment() {
        // Enlist multiple sections
        student.enlist(section1);
        student.enlist(section2);
        student.enlist(labSection);

        // Calculate the assessment
        BigDecimal assessment = student.assess();

        BigDecimal unitCost = new BigDecimal("2000");
        BigDecimal labFee = new BigDecimal("1000");
        BigDecimal miscFees = new BigDecimal("3000");
        BigDecimal vatRate = new BigDecimal("0.12");

        // Calculate the total cost of units
        BigDecimal totalUnitsCost = unitCost.multiply(new BigDecimal("8")); // 8 units

        // Add the lab fee
        BigDecimal totalCost = totalUnitsCost.add(labFee);

        // Add the miscellaneous fees
        totalCost = totalCost.add(miscFees);

        // Apply the VAT
        BigDecimal vat = totalCost.multiply(vatRate);
        totalCost = totalCost.add(vat);

        // Set the scale to 2 decimal places
        BigDecimal expectedAssessment = totalCost.setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedAssessment, assessment);
    }

    @Test
    void withLaboratory() {
        // Enlist the laboratory section
        assertTrue(student.enlist(labSection));

        // Verify the laboratory section is enlisted
        assertEquals(1, student.getEnlistedSections().size());
        assertEquals(labSection, student.getEnlistedSections().get(0));

        // Verify the room's current enrollment
        assertEquals(1, room2.getCurrentEnrollment());
    }
}