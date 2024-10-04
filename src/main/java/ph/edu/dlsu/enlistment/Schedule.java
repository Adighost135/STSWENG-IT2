package enlistment;

class Schedule {
    private String days;
    private String period;

    public Schedule(String days, String period) {
        this.days = days;
        this.period = period;
    }

    public boolean isConflict(Schedule other) {
        return this.days.equals(other.days) && this.period.equals(other.period);
    }

    @Override
    public String toString(){
        // TF H0830, WS H1000
        return days + " " + period;
    }
}


enum Days {
    MTH, TF, WS
}

enum Period{
    H0830, H1000, H1130, H1300, H1430, H1600
}