public class Angle {
    private double angle;

    public Angle(double angle) {
        this.angle = checkLimit(angle);
    }

    // Limits the angle to between 0 and 2 pi
    public static double checkLimit(double angle) {
        if (angle >= 2 * Math.PI) {
            return angle % (2 * Math.PI);
        } else if (angle < 0) {
            return angle + (2 * Math.PI);
        } else {
            return angle;
        }
    }

    public Angle clone() {
        return new Angle(this.getValue());
    }

    public double getValue() {
        return this.angle;
    }

    public void setValue(double angle) {
        this.angle = checkLimit(angle);
    }

    public void changeValue(double change) {
        this.angle = checkLimit(angle + change);
    }

    public String toString() {
        return "Degree: " + Math.toDegrees(angle);
    }
}