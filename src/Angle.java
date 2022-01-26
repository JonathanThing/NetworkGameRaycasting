/**
 * Represents an angle
 *
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0
 * @since 01-01-2022
 */
public class Angle {

    private double angle;

    /**
     * A constructor for the angle class
     *
     * @param angle The value of the angle
     */
    public Angle(double angle) {
        this.angle = checkLimit(angle);
    }

    /**
     * checkLimit
     * This method checks the validity of an angle by making sure its value is between 0 and 2 pi
     *
     * @param angle The value of the angle
     * @return The (new) value of the angle
     */
    public static double checkLimit(double angle) {
        // If the value is too high return angle % 2 pi
        if (angle >= 2 * Math.PI) {
            return angle % (2 * Math.PI);
            // If the value is too high return angle + 2 pi
        } else if (angle < 0) {
            return angle + (2 * Math.PI);
        } else {
            return angle;
        }
    }

    /**
     * clone
     * Duplicates an angle
     *
     * @return The cloned angle
     */
    @Override
    public Angle clone() {
        return new Angle(angle);
    }

    /**
     * getValue
     * Returns the value of an angle
     *
     * @return The value
     */
    public double getValue() {
        return angle;
    }

    /**
     * setValue
     * Reassigns the value of an angle
     *
     * @param angle The value to set it to
     */
    public void setValue(double angle) {
        this.angle = checkLimit(angle);
    }

    /**
     * changeValue
     * Changes the value of an angle
     *
     * @param change The value to add by
     */
    public void changeValue(double change) {
        angle = checkLimit(angle + change);
    }

    /**
     * toString
     * Returns a String representation of the angle
     */
    public String toString() {
        return "Degree: " + Math.toDegrees(angle);
    }
}