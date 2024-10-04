package ph.edu.dlsu.enlistment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.noNullElements;

class Student {
    // make immutable
    private final int studentNo;
    private final Collection<Section> sections = new HashSet<>();

    Student(int studentNo) {
        isTrue(studentNo >= 0, "studentNo must be non-negative; was " + studentNo);
        this.enlistedSections = new ArrayList<>();
        this.completedSubjects = new ArrayList<>();
        Objects.requireNonNull(enlistedSections);
        Objects.requireNonNull(completedSubjects);
        this.studentNo = studentNo;
        noNullElements(enlistedSections);
    }

    void enlist(Section newSection) {
        Objects.requireNonNull(newSection);
        // isTrue(!sections.contains(section), "cannot enlist in same section: " + section);
        sections.forEach(currSection -> currSection.checkConflict(newSection));
        newSection.enlistStudent(); // check the room capacity
        sections.add(newSection);
    }

    void cancelEnlist(Section cancelSection){
        Objects.requireNonNull(cancelSection);
        if(!sections.contains(cancelSection)){
            throw new IllegalArgumentException("Student is not enlisted in this section, section " + cancelSection);
        }
        cancelSection.cancelEnlistment();
        sections.remove(cancelSection);
    }

    Collection<Section> getSections() {
        return new ArrayList<>(sections);
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


