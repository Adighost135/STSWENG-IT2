package ph.edu.dlsu.enlistment;

import org.apache.commons.lang3.*;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Objects;
import java.util.List;

public class Subject {
    private final String subjectId;
    private final int units;
    private final boolean isLaboratory;
    private final List<Subject> prerequisites;

    public Subject(String subjectId, int units, boolean isLaboratory, List<Subject> prerequisites) {
        Objects.requireNonNull(subjectId);

        Validate.isTrue(isAlphanumeric(subjectId), "subjectId must be alphanumeric, was: "
                + subjectId);
        Validate.isTrue(units >= 0, "units must be greater than or equal zero, was: "
                + units);
        this.subjectId = subjectId;
        this.units = units;
        this.isLaboratory = isLaboratory;
        this.prerequisites = prerequisites;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public int getUnits() {
        return units;
    }

    public boolean isLaboratory() {
        return isLaboratory;
    }

    public List<Subject> getPrerequisites() {
        return prerequisites;
    }
    @Override
    public String toString(){
        return subjectId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject subject)) return false;

        return units == subject.units && isLaboratory == subject.isLaboratory && Objects.equals(subjectId, subject.subjectId) && Objects.equals(prerequisites, subject.prerequisites);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(subjectId);
        result = 31 * result + units;
        result = 31 * result + Boolean.hashCode(isLaboratory);
        result = 31 * result + Objects.hashCode(prerequisites);
        return result;
    }
}
