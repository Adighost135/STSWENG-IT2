package ph.edu.dlsu.enlistment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.noNullElements;

class Student {
    // make immutable
    private final int studentNo;
    private final List<Section> enlistedSections;
    private final List<Subject> completedSubjects;

    Student(int studentNo, Collection<Section> enlistedSections, Collection<Subject> completedSubjects) {
        isTrue(studentNo >= 0, "studentNo must be non-negative; was " + studentNo);
        Objects.requireNonNull(enlistedSections);
        Objects.requireNonNull(completedSubjects);
        this.studentNo = studentNo;
        this.enlistedSections = new ArrayList<>();
        this.completedSubjects = new ArrayList<>();
        noNullElements(enlistedSections);
    }

    public boolean enlist(Section section) {
        for (Section enlistedSection : enlistedSections) {
            if (enlistedSection.getSectionId().equals(section.getSectionId())) {
                return false;
            }
            if (enlistedSection.isConflict(section)) {
                return false;
            }
            if (enlistedSection.getSubject().getSubjectId().equals(section.getSubject().getSubjectId())) {
                return false;
            }
        }
        for (Subject prerequisite : section.getSubject().getPrerequisites()) {
            if (!completedSubjects.contains(prerequisite)) {
                return false;
            }
        }
        if (section.getRoom().enlist()) {
            enlistedSections.add(section);
            return true;
        }
        return false;
    }

    public boolean cancelSection(String sectionId) {
        for (Section section : enlistedSections) {
            if (section.getSectionId().equals(sectionId)) {
                section.getRoom().cancel();
                enlistedSections.remove(section);
                return true;
            }
        }
        return false;
    }

    public List<Section> getEnlistedSections() {
        return enlistedSections;
    }

    public void completeSubject(Subject subject) {
        completedSubjects.add(subject);
    }

    public BigDecimal assess() {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal unitCost = new BigDecimal("2000");
        BigDecimal labFee = new BigDecimal("1000");
        BigDecimal miscFees = new BigDecimal("3000");
        BigDecimal vatRate = new BigDecimal("0.12");

        for (Section section : enlistedSections) {
            Subject subject = section.getSubject();
            total = total.add(unitCost.multiply(new BigDecimal(subject.getUnits())));
            if (subject.isLaboratory()) {
                total = total.add(labFee);
            }
        }

        total = total.add(miscFees);
        BigDecimal vat = total.multiply(vatRate);
        total = total.add(vat);

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString(){
        return "Student# " + studentNo;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;

        return studentNo == student.studentNo;
    }

    @Override
    public int hashCode() {
        return studentNo;
    }
}


