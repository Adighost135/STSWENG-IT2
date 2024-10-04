package ph.edu.dlsu.enlistment;

import java.util.Objects;

enum Days {
    MTH, TF, WS
}

enum Period{
    H0830, H1000, H1130, H1300, H1430, H1600
}
record Schedule(Days days, Period period) {

    Schedule{
        Objects.requireNonNull(days);
        Objects.requireNonNull(period);
    }
    
    public boolean isConflict(Schedule other) {
        return this.days.equals(other.days) && this.period.equals(other.period);
    }

    @Override
    public String toString() {
        return days + " " + period;
    }
}

