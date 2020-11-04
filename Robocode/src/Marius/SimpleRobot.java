package Marius;

import robocode.AdvancedRobot;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.PI;

/**
 * Utility class for dealing with angles.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 * @author Tobias Zimmermann (adaption)
 */
class Utils {
    private final static double TWO_PI = 2 * PI;
    private final static double THREE_PI_OVER_TWO = 3 * PI / 2;
    private final static double PI_OVER_TWO = PI / 2;
    static final double NEAR_DELTA = .00001;

    // Hide the default constructor as this class only provides static method
    private Utils() {}

    /**
     * Normalizes an angle to an absolute angle.
     * The normalized angle will be in the range from 0 to 360, where 360
     * itself is not included.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [0,360[
     */
    static double normalAbsoluteAngle(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }

    /**
     * Normalizes an angle to a relative angle.
     * The normalized angle will be in the range from -180 to 180, where 180
     * itself is not included.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [-180,180[
     */
    static double normalRelativeAngle(double angle) {
        return (angle %= 360) >= 0 ? (angle < 180) ? angle : angle - 360 : (angle >= -180) ? angle : angle + 360;
    }

    /**
     * Normalizes an angle to be near an absolute angle.
     * The normalized angle will be in the range from 0 to 360, where 360
     * itself is not included.
     * If the normalized angle is near to 0, 90, 180, 270 or 360, that
     * angle will be returned. The {@link #isNear(double, double) isNear}
     * method is used for defining when the angle is near one of angles listed
     * above.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [0,360[
     * @see #normalAbsoluteAngle(double)
     * @see #isNear(double, double)
     */
    static double normalNearAbsoluteAngle(double angle) {
        angle = (angle %= 360) >= 0 ? angle : (angle + 360);

        if (isNear(angle, 180)) {
            return 180;
        } else if (angle < 180) {
            if (isNear(angle, 0)) {
                return 0;
            } else if (isNear(angle, 90)) {
                return 90;
            }
        } else {
            if (isNear(angle, 270)) {
                return 270;
            } else if (isNear(angle, 360)) {
                return 0;
            }
        }
        return angle;
    }

    /**
     * Tests if the two {@code double} values are near to each other.
     * It is recommended to use this method instead of testing if the two
     * doubles are equal using an this expression: {@code value1 == value2}.
     * The reason being, that this expression might never become
     * {@code true} due to the precision of double values.
     * Whether or not the specified doubles are near to each other is defined by
     * the following expression:
     * {@code (Math.abs(value1 - value2) < .00001)}
     *
     * @param value1 the first double value
     * @param value2 the second double value
     * @return {@code true} if the two doubles are near to each other;
     *         {@code false} otherwise.
     */
    static boolean isNear(double value1, double value2) {
        return (Math.abs(value1 - value2) < NEAR_DELTA);
    }

    /**
     * Returns the trigonometric sine of an angle in degrees.  Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the
     * result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param val an angle, in degrees.
     * @return the sine of the argument.
     */
    static double sin(double val) {
        return Math.sin(Math.toRadians(val));
    }

    /**
     * Returns the trigonometric cosine of an angle in degrees. Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the
     * result is NaN.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param val an angle, in degrees.
     * @return the cosine of the argument.
     */
    static double cos(double val) {
        return Math.cos(Math.toRadians(val));
    }

    /**
     * Returns the trigonometric tangent of an angle in degrees.  Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the result
     * is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param val an angle, in degrees.
     * @return the tangent of the argument.
     */
    static double tan(double val) {
        return Math.tan(Math.toRadians(val));
    }

    /**
     * Returns the arc sine of a value; the returned angle is in the
     * range -90 through 90.  Special cases:
     * <ul><li>If the argument is NaN or its absolute value is greater
     * than 1, then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param val the value whose arc sine is to be returned.
     * @return the arc sine of the argument.
     */
    static double asin(double val) {
        return Math.toDegrees(Math.asin(val));
    }

    /**
     * Returns the arc cosine of a value; the returned angle is in the
     * range 0 through 180.  Special case:
     * <ul><li>If the argument is NaN or its absolute value is greater
     * than 1, then the result is NaN.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param val the value whose arc cosine is to be returned.
     * @return the arc cosine of the argument.
     */
    static double acos(double val) {
        return Math.toDegrees(Math.acos(val));
    }

    /**
     * Returns the arc tangent of a value; the returned angle is in the
     * range -90 through 90.  Special cases:
     * <ul><li>If the argument is NaN, then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param val the value whose arc tangent is to be returned.
     * @return the arc tangent of the argument.
     */
    static double atan(double val) {
        return Math.toDegrees(Math.atan(val));
    }

    /**
     * Returns the angle <i>theta</i> from the conversion of rectangular
     * coordinates ({@code x},&nbsp;{@code y}) to polar
     * coordinates (r,&nbsp;<i>theta</i>).
     * This method computes the phase <i>theta</i> by computing an arc tangent
     * of {@code y/x} in the range of -180 to 180. Special
     * cases:
     * <ul><li>If either argument is NaN, then the result is NaN.
     * <li>If the first argument is positive zero and the second argument
     * is positive, or the first argument is positive and finite and the
     * second argument is positive infinity, then the result is positive
     * zero.
     * <li>If the first argument is negative zero and the second argument
     * is positive, or the first argument is negative and finite and the
     * second argument is positive infinity, then the result is negative zero.
     * <li>If the first argument is positive zero and the second argument
     * is negative, or the first argument is positive and finite and the
     * second argument is negative infinity, then the result is 180.
     * <li>If the first argument is negative zero and the second argument
     * is negative, or the first argument is negative and finite and the
     * second argument is negative infinity, then the result is -180.
     * <li>If the first argument is positive and the second argument is
     * positive zero or negative zero, or the first argument is positive
     * infinity and the second argument is finite, then the result is 90.
     * <li>If the first argument is negative and the second argument is
     * positive zero or negative zero, or the first argument is negative
     * infinity and the second argument is finite, then the result is -90.
     * <li>If both arguments are positive infinity, then the result is 45.
     * <li>If the first argument is positive infinity and the second argument
     * is negative infinity, then the result is 135.
     * <li>If the first argument is negative infinity and the second argument
     * is positive infinity, then the result is -45.
     * <li>If both arguments are negative infinity, then the result is -135.
     * </ul>
     * <p>The computed result must be within 2 ulps of the exact result.
     * Results must be semi-monotonic.
     *
     * @param   y   the ordinate coordinate
     * @param   x   the abscissa coordinate
     * @return  the <i>theta</i> component of the point
     *          (<i>r</i>,&nbsp;<i>theta</i>)
     *          in polar coordinates that corresponds to the point
     *          (<i>x</i>,&nbsp;<i>y</i>) in Cartesian coordinates.
     */
    static double atan2(double y, double x) {
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the absolute value of a double value. If the argument is not negative, the argument is returned. If the argument is negative, the negation of the argument is returned. Special cases:
     * <ul><li>If the argument is positive zero or negative zero, the result is positive zero.
     * <li>If the argument is infinite, the result is positive infinity.
     * <li>If the argument is NaN, the result is NaN.</ul>
     *
     * @param a the argument whose absolute value is to be determined
     * @return Returns the absolute value of a double value
     */
    static double abs(double a) {
        return Math.abs(a);
    }
    
    /**
     * Returns the greater of two double values. That is, the result is the argument closer to positive infinity. If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one argument is positive zero and the other negative zero, the result is positive zero.
     * @param a an argument
     * @param b an other argument
     * @return Returns the minium of the given two values
     */
    static double min(double a, double b) {
        return Math.min(a, b);
    }

    /**
     * Returns the smaller of two double values. That is, the result is the value closer to negative infinity. If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one argument is positive zero and the other is negative zero, the result is negative zero.
     *
     * @param a an argument
     * @param b an other argument
     * @return Returns the maximum of the given two values
     */
    static double max(double a, double b) {
        return Math.max(a, b);
    }

    /**
     * Returns the signum function of the argument; zero if the argument is zero, 1.0 if the argument is greater than zero, -1.0 if the argument is less than zero.
     * Special Cases:
     * <ul>
     * <li>If the argument is NaN, then the result is NaN.</li>
     * <li>If the argument is positive zero or negative zero, then the result is the same as the argument.</li>
     * </ul>
     * 
     * @param d the double value whose signum is to be returned
     * @return the signum-function of the argument
     */
    static double signum(double d) {
        return Math.signum(d);
    }
}

/**
 * An immutable vector in two dimensional space over doubles.
 *
 * The vector contains an x- and y-coordinate and provides various methods to operate on them.
 * The output of the methods is chosen so that the results are easy to use in Robocode. Specifically this means that
 * angles are measured from the ordinate axis in a clockwise manner and that angles returned may be negative. All angles
 * are in degrees.
 * 
 * @author Tobias Zimmermann (original)
 * @author Billy Joe Franks (adaptation)
 * @author Julian "Jules" Stieß (adaptation)
 */
class Point {

    /**
     * The x coordinate of the vector. Guaranteed to be finite.
     */
    private double x;

    /**
     * The y coordinate of the vector. Guaranteed to be finite.
     */
    private double y;

    /**
     * Creates a new vector from given finite x and y coordinates.
     *
     * @param x The x coordinate of the newly constructed vector.
     * @param y The y coordinate of the newly constructed vector.
     */
    public Point(double x, double y) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("The x-coordinate of vector must be a finite number.");
        }

        if (!Double.isFinite(y)) {
            throw new IllegalArgumentException("The y-coordinate of vector must be a finite number.");
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new vector from its orientation and length.
     *
     * @param phi    The orientation of the new vector, measured from the y-axis, in degrees.
     * @param length The length of the new vector.
     * @return       The position vector for the given polar coordinates.
     */
    public static Point fromPolarCoordinates(double phi, double length) {
        return new Point(Math.sin(Math.toRadians(phi)) * length, Math.cos(Math.toRadians(phi)) * length);
    }

    /**
     * Returns the x coordinate of the vector. Guaranteed to be finite.
     *
     * @return The x coordinate of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the vector. Guaranteed to be finite.
     *
     * @return The y coordinate of the vector.
     */
    public double getY() {
        return y;
    }
    /**
     * Returns a new vector with the given finite x coordinate
     *
     * @param newX The x coordinate of the new vector.
     * @return The new vector
     */
    public Point withX(double newX) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("The x-coordinate of vector must be a finite number.");
        }
        return new Point(newX,y);
    }

    /**
     * Returns a new vector with the given finite y coordinate.
     *
     * @param newY The y coordinate of the new vector.
     * @return The new vector
     */
    public Point withY(double newY) {
        if (!Double.isFinite(y)) {
            throw new IllegalArgumentException("The x-coordinate of vector must be a finite number.");
        }

        return new Point(x,newY);
    }

    /**
     * Adds the given finite x and y values to this vector and stores the result in a new vector.
     *
     * @param x The finite value to add to the x-coordinate.
     * @param y The finite value to add to the y-coordinate,
     * @return  The sum vector.
     */
    public Point add(double x, double y) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("The x-coordinate of vector must be a finite number.");
        }

        if (!Double.isFinite(y)) {
            throw new IllegalArgumentException("The y-coordinate of vector must be a finite number.");
        }

        return new Point(this.x + x, this.y + y);
    }

    /**
     * Adds the given vector to this vector by adding each coordinate and stores the result in a new vector.
     *
     * @param other The vector to add to this one.
     * @return      The sum vector.
     */
    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts the given finite x and y values to this vector and stores the result in a new vector.
     *
     * @param x The finite value to subtract from the x-coordinate
     * @param y The finite value to subtract from the y-coordinate
     * @return  The difference vector.
     */
    public Point subtract(double x, double y) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("The x-coordinate of vector must be a finite number.");
        }

        if (!Double.isFinite(y)) {
            throw new IllegalArgumentException("The y-coordinate of vector must be a finite number.");
        }

        return new Point(this.x - x, this.y - y);
    }

    /**
     * Subtracts the given vector from this one by subtracting each coordinate and stores the result in a new vector.
     *
     * @param other The vector to subtract from this one.
     * @return      The difference vector.
     */
    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    /**
     * Multiplies the vector by a given finite scalar and stores the result in a new vector.
     *
     * @param scalar The finite scalar by which to multiply.
     * @return       The result vector.
     */
    public Point multiply(double scalar) {
        return new Point(x * scalar, y * scalar);
    }

    /**
     * Returns the length of the vector.
     *
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the square of the length of the vector.
     *
     * This method is provided for performance and precision reasons. The length is calculated with the Pythagorean
     * theorem, which means a square root is used, which costs both CPU time and potentially precision, if the result
     * cannot be represented exactly.
     *
     * @return The square of the length of the vector.
     */
    public double lengthSquared() {
        return x * x + y * y;
    }

    /**
     * Normalizes the vector to length one and returns the result as a new vector.
     *
     * @return The normalized vector.
     */
    public Point normalize() {
        double len = length();
        if (len == 0) {
            throw new IllegalStateException("Cannot normalize a null vector.");
        }

        return multiply(1 / len);
    }

    /**
     * Returns a new vector with the same orientation as this one and the specified length.
     *
     * @param desiredLength The length the vector should have.
     * @return The vector with the desired length.
     */
    public Point withLength(double desiredLength) {
        double len = length();
        if (len == 0) {
            if (desiredLength == 0) {
                return this;
            }

            throw new IllegalStateException("Cannot create a null vector of non-zero length.");
        }

        return multiply(desiredLength / len);
    }

    /**
     * Returns the angle between the y-axis and this vector in degrees.
     *
     * The angle is measured in the range from -Pi to Pi.
     *
     * @return The angle between the y-axis and this vector in degrees.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public double angle() {
        return Math.toDegrees(Math.atan2(x, y));
    }

    /**
     * Returns the angle between this vector and the one specified by the given coordinates in degrees.
     *
     * The angle is measured in the range from -Pi to Pi.
     *
     * @param x The x-coordinate of the vector to which to measure the angle to.
     * @param y The y-coordinate of the vector to which to measure the angle to.
     * @return  The angle between the vectors in degrees.
     */
    public double angleFrom(double x, double y) {
        return Math.toDegrees(Math.atan2(this.x - x, this.y - y));
    }

    /**
     * Returns the ange between this vector and the one given as a parameter measured in degrees.
     *
     * The angle is measured in the range from -Pi to Pi. Returns {@code NaN} if the given vector is {@code null}.
     *
     * @param other The vector to which to measure the angle to.
     * @return      The angle between the vectors in degrees.
     */
    public double angleFrom(Point other) {
        if (other == null) {
            return Double.NaN;
        }

        return Math.toDegrees(Math.atan2(x - other.x, y - other.y));
    }

    /**
     * Returns the dot product between this vector and the one given as a parameter.
     *
     * @param other The other vector in the dot product.
     * @return      The dot product in double precision.
     */
    public double dotProduct(Point other) {
        return x * other.x + y * other.y;
    }

    /**
     * Checks if this vector and the given vector point in either the same or exact opposite directions.
     *
     * @param other The vector to check this vector against.
     * @return      {@code true} if this vector and the given vector point in the same or opposite directions.
     */
    public boolean isSameDirectionAs(Point other) {
        if (other == null) {
            return false;
        }

        if (other.x == 0) {
            return x == 0 && (y == 0 && other.y == 0 || y != 0 && other.y != 0);
        } else if (x == 0) {
            return false;
        } else {
            double xFactor = x / other.x;
            return Double.compare(y, other.y * xFactor) == 0;
        }
    }

    /**
     * Checks if this vector and the given vector point in the exact same direction and have the same heading.
     *
     * @param other The vector to check this vector against.
     * @return      {@code true} if this vector and the given vector have the same direction and heading.
     */
    public boolean isSameDirectionAndHeadingAs(Point other) {
        if (other == null) {
            return false;
        }

        if (other.x == 0) {
            return x == 0 && Math.signum(y) == Math.signum(other.y);
        } else if (x == 0) {
            return false;
        } else {
            double xFactor = x / other.x;
            return Double.compare(y, other.y * xFactor) == 0 && Math.signum(x) == Math.signum(other.x);
        }
    }

    /**
     * Returns the distance between this vector and the given one.
     *
     * @param other the vector to calculate the distance to
     * @return the distance between this vector and the given one
     */
    public double distance(Point other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * Returns the square of the distance between this vector and the given one.
     *
     * This method is provided for performance and precision reasons. The length is calculated with the Pythagorean
     * theorem, which means a square root is used, which costs both CPU time and potentially precision, if the result
     * cannot be represented exactly.
     *
     * @param other the vector to calculate the distance to
     * @return the square of the distance between this vector and the given one
     */
    public double distanceSq(Point other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        return xDiff * xDiff + yDiff * yDiff;
    }

    /**
     * Tests this vector of equality to the given vector.
     *
     * Vectors are equal if and only if both the x- and the y-coordinate are equal. To account for double imprecision,
     * the numbers are compared with {@link Double#compare(double, double)} which allows slight differences.
     * Returns {@code false} if the other vector is {@code null}.
     * If the other object is not a vector, returns {@code false}.
     *
     * @param other The vector to compare this vector to.
     * @return      {@code true} if this vector and the other are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Point)) {
            return false;
        }

        Point vec = (Point)other;
        return Double.compare(x, vec.x) == 0 && Double.compare(y, vec.y) == 0;
    }

    /**
     * Returns a string representation of this vector.
     *
     * @return A string representation of this vector.
     */
    @Override
    public String toString() {
        return "<" + x + ", " + y + '>';
    }
}

/**
 * This event is returned by {@link SimpleRobotBehavior#getBulletHitEvents()}
 * when one of your bullets has hit another robot.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
final class BulletHitEvent {
    private robocode.BulletHitEvent ev;

    /**
     * Called by the game to create a new {@link BulletHitEvent} object.
     *
     * @param ev the internal event
     */
    BulletHitEvent(robocode.BulletHitEvent ev) {
        this.ev = ev;
    }

    /**
     * Returns the bullet of yours that hit the robot.
     *
     * @return the bullet that hit the robot
     */
    public Bullet getBullet() {
        return new Bullet(ev.getBullet());
    }

    /**
     * Returns the remaining energy of the robot your bullet has hit (after the
     * damage done by your bullet).
     *
     * @return energy the remaining energy of the robot that your bullet has hit
     */
    public double getEnergy() {
        return ev.getEnergy();
    }

    /**
     * Returns the name of the robot your bullet hit.
     *
     * @return the name of the robot your bullet hit.
     */
    public String getName() {
        return ev.getName();
    }
}

/**
 * This event is sent to {@link SimpleRobotBehavior#getBulletHitBulletEvents()}.
 * when one of your bullets has hit another bullet.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
final class BulletHitBulletEvent {
    private robocode.BulletHitBulletEvent ev;

    /**
     * Called by the game to create a new {@link BulletHitBulletEvent} object.
     *
     * @param ev the internal event
     */
    BulletHitBulletEvent(robocode.BulletHitBulletEvent ev) {
        this.ev = ev;
    }

    /**
     * Returns your bullet that hit another bullet.
     *
     * @return your bullet
     */
    public Bullet getBullet() {
        return new Bullet(ev.getBullet());
    }

    /**
     * Returns the bullet that was hit by your bullet.
     *
     * @return the bullet that was hit
     */
    public Bullet getHitBullet() {
        return new Bullet(ev.getHitBullet());
    }
}

/**
 * This event is sent to {@link SimpleRobotBehavior#getBulletMissedEvents()}
 * when one of your bullets has missed, i.e. when the bullet has reached the
 * border of the battlefield.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
final class BulletMissedEvent {
    private robocode.BulletMissedEvent ev;

    /**
     * Called by the game to create a new {@link BulletMissedEvent} object.
     *
     * @param ev the internal Robocode event
     */
    BulletMissedEvent(robocode.BulletMissedEvent ev) {
        this.ev = ev;
    }

    /**
     * Returns the bullet that missed.
     *
     * @return the bullet that missed
     */
    public Bullet getBullet() {
        return new Bullet(ev.getBullet());
    }
}

/**
 * A HitByBulletEvent is sent to
 * {@link SimpleRobotBehavior#getHitByBulletEvents()} when your robot has been
 * hit by a bullet. You can use the information contained in this event to
 * determine what to do.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
final class HitByBulletEvent {
    private robocode.HitByBulletEvent ev;

    /**
     * Called by the game to create a new {@link HitByBulletEvent} object.
     *
     * @param ev the internal Robocode event
     */
    HitByBulletEvent(robocode.HitByBulletEvent ev) {
        this.ev = ev;
    }

    /**
     * Returns the bearing to the bullet, relative to your robot's heading,
     * in degrees (-180 &lt; getBearing() &lt;= 180).
     * <p>
     * If you were to {@code turn(event.getBearing())}, you would be facing the
     * direction the bullet came from. The calculation used here is:
     * (bullet's heading in degrees + 180) - (your heading in degrees)
     *
     * @return the bearing to the bullet, in degrees
     */
    public double getBearing() {
        return ev.getBearing();
    }

    /**
     * Returns the bullet that hit your robot.
     *
     * @return the bullet that hit your robot
     */
    public Bullet getBullet() {
        return new Bullet(ev.getBullet());
    }

    /**
     * Returns the heading of the bullet when it hit you, in degrees
     * (0 &lt;= getHeading() &lt; 360).
     * <p>
     * Note: This is not relative to the direction you are facing. The robot
     * that fired the bullet was in the opposite direction of getHeading() when
     * it fired the bullet.
     *
     * @return the heading of the bullet, in degrees
     */
    public double getHeading() {
        return ev.getHeading();
    }

    /**
     * Returns the name of the robot that fired the bullet.
     *
     * @return the name of the robot that fired the bullet
     */
    public String getName() {
        return ev.getName();
    }

    /**
     * Returns the power of this bullet. The damage you take (in fact, already
     * took) is 4 * power, plus 2 * (power-1) if power &gt; 1. The robot that fired
     * the bullet receives 3 * power back.
     *
     * @return the power of the bullet
     */
    public double getPower() {
        return ev.getPower();
    }

    /**
     * Returns the velocity of this bullet.
     *
     * @return the velocity of the bullet
     */
    public double getVelocity() {
        return ev.getVelocity();
    }
}

/**
 * A HitRobotEvent is sent to {@link SimpleRobotBehavior#getHitRobotEvents()}
 * when your robot collides with another robot.
 * You can use the information contained in this event to determine what to do.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
final class HitRobotEvent {
    private robocode.HitRobotEvent ev;

    /**
     * Called by the game to create a new {@link HitRobotEvent} object.
     *
     * @param ev the internal Robocode event
     */
    HitRobotEvent(robocode.HitRobotEvent ev) {
        this.ev = ev;
    }

    /**
     * Returns the bearing to the robot you hit, relative to your robot's
     * heading, in degrees (-180 &lt;= getBearing() &lt; 180)
     *
     * @return the bearing to the robot you hit, in degrees
     */
    public double getBearing() {
        return ev.getBearing();
    }

    /**
     * Returns the amount of energy of the robot you hit.
     *
     * @return the amount of energy of the robot you hit
     */
    public double getEnergy() {
        return ev.getEnergy();
    }

    /**
     * Returns the name of the robot you hit.
     *
     * @return the name of the robot you hit
     */
    public String getName() {
        return ev.getName();
    }

    /**
     * Checks if your robot was moving towards the robot that was hit.
     * <p>
     * If {@link #isMyFault()} returns {@code true} then your robot's movement
     * (including turning) will have stopped and been marked complete.
     * <p>
     * Note: If two robots are moving toward each other and collide, they will
     * each receive two HitRobotEvents. The first will be the one if
     * {@link #isMyFault()} returns {@code true}.
     *
     * @return {@code true} if your robot was moving towards the robot that was
     *         hit; {@code false} otherwise.
     */
    public boolean isMyFault() {
        return ev.isMyFault();
    }
}

/**
 * A HitWallEvent is sent to {@link SimpleRobotBehavior#getHitWallEvents()}
 * when you collide a wall.
 * You can use the information contained in this event to determine what to do.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
final class HitWallEvent {
    private robocode.HitWallEvent ev;

    /**
     * Called by the game to create a new {@link HitWallEvent} object.
     *
     * @param ev the internal Robocode event.
     */
    HitWallEvent(robocode.HitWallEvent ev) {
        this.ev = ev;
    }

    /**
     * Returns the bearing to the wall you hit, relative to your robot's
     * heading, in degrees (-180 &lt;= getBearing() &lt; 180)
     *
     * @return the bearing to the wall you hit, in degrees
     */
    public double getBearing() {
        return ev.getBearing();
    }
}

/**
 * A ScannedRobotEvent is sent to
 * {@link SimpleRobotBehavior#getScannedRobotEvents()} when you scan a robot.
 * You can use the information contained in this event to determine what to do.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
final class ScannedRobotEvent {
    private robocode.ScannedRobotEvent ev;

    /**
     * Called by the game to create a new {@link ScannedRobotEvent} object.
     *
     * @param ev the internal Robocode event
     */
    ScannedRobotEvent(robocode.ScannedRobotEvent ev) {
        this.ev = ev;
    }

    /**
     * Returns the bearing to the robot you scanned, relative to your robot's
     * heading, in degrees (-180 &lt;= getBearing() &lt; 180)
     *
     * @return the bearing to the robot you scanned, in degrees
     */
    public double getBearing() {
        return ev.getBearing();
    }

    /**
     * Returns the distance to the robot (your center to his center).
     *
     * @return the distance to the robot.
     */
    public double getDistance() {
        return ev.getDistance();
    }

    /**
     * Returns the energy of the robot.
     *
     * @return the energy of the robot
     */
    public double getEnergy() {
        return ev.getEnergy();
    }

    /**
     * Returns the heading of the robot, in degrees (0 &lt;= getHeading() &lt; 360)
     *
     * @return the heading of the robot, in degrees
     */
    public double getHeading() {
        return ev.getHeading();
    }

    /**
     * Returns the name of the robot.
     *
     * @return the name of the robot
     */
    public String getName() {
        return ev.getName();
    }

    /**
     * Returns the velocity of the robot.
     *
     * @return the velocity of the robot
     */
    public double getVelocity() {
        return ev.getVelocity();
    }
}

/**
 * Represents a bullet. This is returned from
 * {@link SimpleRobotBehavior#fireBullet(double)} and all the bullet-related
 * events.
 *
 * @see SimpleRobotBehavior#fireBullet(double)
 * @see BulletHitEvent
 * @see BulletMissedEvent
 * @see BulletHitBulletEvent
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
class Bullet {
    private robocode.Bullet bullet;

    /*
     * Called by the game to create a new {@code Bullet} object.
     *
     * @param bullet The internal Bullet
     */
    public Bullet(robocode.Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return bullet.equals(((Bullet) obj).bullet);
    }

    @Override
    public int hashCode() {
        return bullet.hashCode();
    }

    /**
     * Returns the direction the bullet is/was heading, in degrees
     * (0 &lt;= getHeading() &lt; 360). This is not relative to the direction you are
     * facing.
     *
     * @return the direction the bullet is/was heading, in degrees
     */
    public double getHeading() {
        return bullet.getHeading();
    }

    /**
     * Returns the name of the robot that fired this bullet.
     *
     * @return the name of the robot that fired this bullet
     */
    public String getName() {
        return bullet.getName();
    }

    /**
     * Returns the power of this bullet.
     * <p>
     * The bullet will do (4 * power) damage if it hits another robot.
     * If power is greater than 1, it will do an additional 2 * (power - 1)
     * damage. You will get (3 * power) back if you hit the other robot.
     *
     * @return the power of the bullet
     */
    public double getPower() {
        return bullet.getPower();
    }

    /**
     * Returns the velocity of this bullet. The velocity of the bullet is
     * constant once it has been fired.
     *
     * @return the velocity of the bullet
     */
    public double getVelocity() {
        return bullet.getVelocity();
    }

    /**
     * Returns the name of the robot that this bullet hit, or {@code null} if
     * the bullet has not hit a robot.
     *
     * @return the name of the robot that this bullet hit, or {@code null} if
     *         the bullet has not hit a robot.
     */
    public String getVictim() {
        return bullet.getVictim();
    }

    /**
     * Returns the X position of the bullet.
     *
     * @return the X position of the bullet
     */
    public double getX() {
        return bullet.getX();
    }

    /**
     * Returns the Y position of the bullet.
     *
     * @return the Y position of the bullet
     */
    public double getY() {
        return bullet.getY();
    }

    /**
     * Checks if this bullet is still active on the battlefield.
     *
     * @return {@code true} if the bullet is still active on the battlefield;
     *         {@code false} otherwise
     */
    public boolean isActive() {
        return bullet.isActive();
    }
}

/**
 * This class provides methods to control the actions of a robot. To implement
 * your own behavior, override the {@link #start()} and {@link #execute()}
 * methods.
 * <p>
 * The {@link #start()} method is called once at the start of each round. The
 * {@link #execute()} method is called once each tick and should be used to
 * implement an actual behavior. All actions will be queued up and executed when
 * the method returns.
 */
abstract class SimpleRobotBehavior {
    private SimpleRobot robot;

    SimpleRobotBehavior(SimpleRobot robot) {
        this.robot = robot;
    }

    /**
     * This method is called once at the beginning of a round and can be used to
     * perform any kind of setup. All actions started in this method will be
     * executed after it returns.
     */
    abstract void start();

    /**
     * This method is called once during each game tick and should be used to
     * implement the behavior of the robot.
     * All actions queued up will be executed when this method returns.
     */
    abstract void execute();

    /**
     * Returns the distance remaining in the robot's current move measured in
     * pixels.
     * <p>
     * This call returns both positive and negative values. Positive values
     * means that the robot is currently moving forwards. Negative values means
     * that the robot is currently moving backwards. If the returned value is 0,
     * the robot currently stands still.
     *
     * @return the distance remaining in the robot's current move measured in
     *         pixels.
     * @see #getTurnRemaining() getTurnRemaining()
     * @see #getGunTurnRemaining() getGunTurnRemaining()
     * @see #getRadarTurnRemaining() getRadarTurnRemaining()
     */
    final double getDistanceRemaining() {
        return robot.getDistanceRemaining();
    }

    /**
     * Returns the direction that the robot's gun is facing, in degrees.
     * The value returned will be between 0 and 360 (is excluded).
     * <p>
     * Note that the heading in Robocode is like a compass, where 0 means North,
     * 90 means East, 180 means South, and 270 means West.
     *
     * @return the direction that the robot's gun is facing, in degrees.
     * @see #getHeading()
     * @see #getRadarHeading()
     */
    final double getGunHeading() {
        return robot.getGunHeading();
    }

    /**
     * Returns the angle remaining in the gun's turn, in degrees.
     * <p>
     * This call returns both positive and negative values. Positive values
     * means that the gun is currently turning to the right. Negative values
     * means that the gun is currently turning to the left. If the returned
     * value is 0, the gun is currently not turning.
     *
     * @return the angle remaining in the gun's turn, in degrees
     * @see #getDistanceRemaining() getDistanceRemaining()
     * @see #getTurnRemaining() getTurnRemaining()
     * @see #getRadarTurnRemaining() getRadarTurnRemaining()
     */
    final double getGunTurnRemaining() {
        return robot.getGunTurnRemaining();
    }

    /**
     * Returns the direction that the robot's body is facing, in degrees.
     * The value returned will be between 0 and 360 (is excluded).
     * <p>
     * Note that the heading in Robocode is like a compass, where 0 means North,
     * 90 means East, 180 means South, and 270 means West.
     *
     * @return the direction that the robot's body is facing, in degrees.
     * @see #getGunHeading()
     * @see #getRadarHeading()
     */
    final double getHeading() {
        return robot.getHeading();
    }

    /**
     * Returns the direction that the robot's radar is facing, in degrees.
     * The value returned will be between 0 and 360 (is excluded).
     * <p>
     * Note that the heading in Robocode is like a compass, where 0 means North,
     * 90 means East, 180 means South, and 270 means West.
     *
     * @return the direction that the robot's radar is facing, in degrees.
     * @see #getHeading()
     * @see #getGunHeading()
     */
    final double getRadarHeading() {
        return robot.getRadarHeading();
    }

    /**
     * Returns the angle remaining in the radar's turn, in degrees.
     * <p>
     * This call returns both positive and negative values. Positive values
     * means that the radar is currently turning to the right. Negative values
     * means that the radar is currently turning to the left. If the returned
     * value is 0, the radar is currently not turning.
     *
     * @return the angle remaining in the radar's turn, in degrees
     * @see #getDistanceRemaining() getDistanceRemaining()
     * @see #getGunTurnRemaining() getGunTurnRemaining()
     * @see #getRadarTurnRemaining() getRadarTurnRemaining()
     */
    final double getRadarTurnRemaining() {
        return robot.getRadarTurnRemaining();
    }

    /**
     * Returns the angle remaining in the robots's turn, in degrees.
     * <p>
     * This call returns both positive and negative values. Positive values
     * means that the robot is currently turning to the right. Negative values
     * means that the robot is currently turning to the left. If the returned
     * value is 0, the robot is currently not turning.
     *
     * @return the angle remaining in the robots's turn, in degrees
     * @see #getDistanceRemaining() getDistanceRemaining()
     * @see #getGunTurnRemaining() getGunTurnRemaining()
     * @see #getRadarTurnRemaining() getRadarTurnRemaining()
     */
    final double getTurnRemaining() {
        return robot.getTurnRemaining();
    }

    /**
     * Sets the robot to move ahead (forward) by distance measured in pixels
     * when the call returns from {@link #execute()}.
     * <p>
     * This call returns immediately, and will not execute until you return from
     * {@link #execute()}.
     * <p>
     * Note that both positive and negative values can be given as input, where
     * positive values means that the robot is set to move ahead, and negative
     * values means that the robot is set to move back. If 0 is given as input,
     * the robot will stop its movement, but will have to decelerate
     * till it stands still, and will thus not be able to stop its movement
     * immediately, but eventually.
     *
     * @param distance the distance to move measured in pixels.
     *                 If {@code distance} &gt; 0 the robot is set to move ahead.
     *                 If {@code distance} &lt; 0 the robot is set to move back.
     *                 If {@code distance} = 0 the robot is set to stop its movement.
     */
    final void ahead(double distance) {
        robot.setAhead(distance);
    }

    /**
     * Sets the gun to fire a bullet when the call returns from
     * {@link #execute()}. The bullet will travel in the direction the gun is
     * pointing.
     * <p>
     * This call returns immediately, and will not execute until you return from
     * {@link #execute()}.
     * <p>
     * The specified bullet power is an amount of energy that will be taken from
     * the robot's energy. Hence, the more power you want to spend on the
     * bullet, the more energy is taken from your robot.
     * <p>
     * The bullet will do (4 * power) damage if it hits another robot. If power
     * is greater than 1, it will do an additional 2 * (power - 1) damage.
     * You will get (3 * power) back if you hit the other robot.
     * <p>
     * The specified bullet power should be between
     * 0.1 and 3.
     * <p>
     * Note that the gun cannot fire if the gun is overheated, meaning that
     * {@link #getGunHeat()} returns a value &gt; 0.
     * <p>
     * A event is generated when the bullet hits a robot
     * ({@link BulletHitEvent}), wall ({@link BulletMissedEvent}), or another
     * bullet ({@link BulletHitBulletEvent}).
     * <p>
     * Example:
     * <pre>
     *   Bullet bullet = null;
     * 
     *   // Fire a bullet with maximum power if the gun is ready
     *   if (getGunHeat() == 0) {
     *       bullet = fireBullet(3);
     *   }
     *   ...
     *   // Get the velocity of the bullet
     *   if (bullet != null) {
     *       double bulletVelocity = bullet.getVelocity();
     *   }
     * </pre>
     *
     * @param power the amount of energy given to the bullet, and subtracted
     *              from the robot's energy.
     * @return a {@link Bullet} that contains information about the bullet if it
     *         was actually fired, which can be used for tracking the bullet
     *         after it has been fired. If the bullet was not fired,
     *         {@code null} is returned.
     * @see Bullet
     * @see #getGunHeat() getGunHeat()
     * @see #getGunCoolingRate() getGunCoolingRate()
     */
    final Bullet fireBullet(double power) {
        robocode.Bullet bullet = robot.setFireBullet(power);
        if (bullet == null) return null;
        return new Bullet(bullet);
    }

    /**
     * Sets the robot's gun to turn right by degrees when the call returns from
     * {@link #execute()}.
     * takes place.
     * <p>
     * This call returns immediately, and will not execute until you return from
     * {@link #execute()}.
     * <p>
     * Note that both positive and negative values can be given as input,
     * where negative values means that the robot's gun is set to turn left
     * instead of right.
     * <p>
     *
     * @param degrees the amount of degrees to turn the robot's gun to the right.
     *                If {@code degrees} &gt; 0 the robot's gun is set to turn right.
     *                If {@code degrees} &lt; 0 the robot's gun is set to turn left.
     *                If {@code degrees} = 0 the robot's gun is set to stop turning.
     */
    final void turnGun(double degrees) {
        robot.setTurnGunRight(degrees);
    }

    /**
     * Sets the robot's body to turn right by degrees when the call returns from
     * {@link #execute()}.
     * <p>
     * This call returns immediately, and will not execute until you return from
     * {@link #execute()}.
     * <p>
     * Note that both positive and negative values can be given as input,
     * where negative values means that the robot's body is set to turn left
     * instead of right.
     *
     * @param degrees the amount of degrees to turn the robot's body to the right.
     *                If {@code degrees} &gt; 0 the robot is set to turn right.
     *                If {@code degrees} &lt; 0 the robot is set to turn left.
     *                If {@code degrees} = 0 the robot is set to stop turning.
     */
    final void turn(double degrees) {
        robot.setTurnRight(degrees);
    }

    /**
     * Sets the robot's radar to turn right by degrees when the call returns
     * from {@link #execute()}.
     * <p>
     * This call returns immediately, and will not execute until you return from
     * {@link #execute()}.
     * <p>
     * Note that both positive and negative values can be given as input,
     * where negative values means that the robot's radar is set to turn left
     * instead of right.
     *
     * @param degrees the amount of degrees to turn the robot's radar to the right.
     *                If {@code degrees} &gt; 0 the robot's radar is set to turn right.
     *                If {@code degrees} &lt; 0 the robot's radar is set to turn left.
     *                If {@code degrees} = 0 the robot's radar is set to stop turning.
     */
    final void turnRadar(double degrees) {
        robot.setTurnRadarRight(degrees);
    }

    /**
     * Returns the height of the current battlefield measured in pixels.
     *
     * @return the height of the current battlefield measured in pixels.
     */
    final double getBattleFieldHeight() {
        return robot.getBattleFieldHeight();
    }

    /**
     * Returns the width of the current battlefield measured in pixels.
     *
     * @return the width of the current battlefield measured in pixels.
     */
    final double getBattleFieldWidth() {
        return robot.getBattleFieldWidth();
    }

    /**
     * Returns the robot's current energy.
     *
     * @return the robot's current energy.
     */
    final double getEnergy() {
        return robot.getEnergy();
    }

    /**
     * Returns the rate at which the gun will cool down, i.e. the amount of heat
     * the gun heat will drop per turn.
     * <p>
     * The gun cooling rate is default 0.1 / turn, but can be changed by the
     * battle setup. So don't count on the cooling rate being 0.1!
     *
     * @return the gun cooling rate
     * @see #getGunHeat()
     * @see #fireBullet(double)
     */
    final double getGunCoolingRate() {
        return robot.getGunCoolingRate();
    }

    /**
     * Returns the current heat of the gun. The gun cannot fire unless this is
     * 0. (Calls to fire will succeed, but will not actually fire unless
     * getGunHeat() == 0).
     * <p>
     * The amount of gun heat generated when the gun is fired is
     * 1 + (firePower / 5). Each turn the gun heat drops by the amount returned
     * by {@link #getGunCoolingRate()}, which is a battle setup.
     * <p>
     * Note that all guns are "hot" at the start of each round, where the gun
     * heat is 3.
     *
     * @return the current gun heat
     * @see #getGunCoolingRate()
     * @see #fireBullet(double)
     */
    final double getGunHeat() {
        return robot.getGunHeat();
    }

    /**
     * Returns the game time of the current round, where the time is equal to
     * the current turn in the round.
     * <p>
     * A battle consists of multiple rounds.
     * <p>
     * Time is reset to 0 at the beginning of every round.
     *
     * @return the game time/turn of the current round.
     */
    final long getTime() {
        return robot.getTime();
    }

    /**
     * Returns the velocity of the robot measured in pixels/turn.
     * <p>
     * The maximum velocity of a robot is 8 pixels / turn.
     *
     * @return the velocity of the robot measured in pixels/turn.
     */
    final double getVelocity() {
        return robot.getVelocity();
    }

     /**
     * Sets the maximum velocity of the robot measured in pixels/turn if the robot should move slower than 8 pixels/turn.
     * when the call returns from {@link #execute()}.
     * 
     * @param newMaxVelocity - the new maximum turn rate of the robot measured in pixels/turn. Valid values are 0 - 8
     */
    final void setMaxVelocity(double newMaxVelocity){
        robot.setMaxVelocity(newMaxVelocity);
    }
    
    /**
     * Returns the X position of the robot. (0,0) is at the bottom left of the
     * battlefield.
     *
     * @return the X position of the robot.
     * @see #getY()
     */
    final double getX() {
        return robot.getX();
    }

    /**
     * Returns the Y position of the robot. (0,0) is at the bottom left of the
     * battlefield.
     *
     * @return the Y position of the robot.
     * @see #getX()
     */
    final double getY() {
        return robot.getY();
    }
    
    /**
     * Returns the position of the robot as a Point. (0,0) is at the bottom left of the
     * battlefield.
     *
     * @return the position of the robot as a Point.
     * @see #getX()
     * @see #getY()
     */
    final Point getPoint() {
        return new Point(this.getX(),this.getY());
    }

    /**
     * Sets the color of the robot's body, gun, radar, bullet, and scan arc in
     * the same time.
     * <p>
     * You may only call this method one time per battle. A {@code null}
     * indicates the default (blue) color for the body, gun, radar, and scan
     * arc, but white for the bullet color.
     * <p>
     * Example:
     * <pre>
     *   // Don't forget to import java.awt.Color at the top...
     *   import java.awt.Color;
     *   ...
     *
     *   public void start() {
     *       setColors(null, Color.RED, Color.GREEN, null, new Color(150, 0, 150));
     *       ...
     *   }
     * </pre>
     *
     * @param bodyColor	the new body color
     * @param gunColor	 the new gun color
     * @param radarColor   the new radar color
     * @param bulletColor  the new bullet color
     * @param scanArcColor the new scan arc color
     * @see Color
     */
    final void setColors(Color bodyColor, Color gunColor, Color radarColor, Color bulletColor, Color scanArcColor) {
        robot.setColors(bodyColor, gunColor, radarColor, bulletColor, scanArcColor);
    }

    /**
     * Returns a vector containing all {@link BulletHitBulletEvent}s currently
     * in the robot's queue.
     * <p>
     * Example:
     * <pre>
     *   for (BulletHitBulletEvent event : getBulletHitBulletEvents()) {
     *       <i>// do something with the event</i>
     *   }
     * </pre>
     *
     * @return a vector containing all BulletHitBulletEvents currently in the
     *         robot's queue
     * @see BulletHitBulletEvent
     */
    final Vector<BulletHitBulletEvent> getBulletHitBulletEvents() {
        Vector<BulletHitBulletEvent> res =  Stream.concat(
                robot
                    .getBulletHitBulletEvents()
                    .stream()
                    .map(BulletHitBulletEvent::new),
                robot.bulletHitBulletEventQueue.stream())
                .collect(Collectors.toCollection(Vector::new));
        robot.bulletHitBulletEventQueue = new LinkedList<>();
        return res;
    }

    /**
     * Returns a vector containing all {@link BulletHitEvent}s in the
     * robot's queue.
     * <p>
     * Example:
     * <pre>
     *   for (BulletHitEvent event: getBulletHitEvents()) {
     *       <i>// do something with the event</i>
     *   }
     * </pre>
     *
     * @return a vector containing all BulletHitEvents currently in the robot's
     *         queue
     * @see BulletHitEvent
     */
    final Vector<BulletHitEvent> getBulletHitEvents() {
        Vector<BulletHitEvent> res = Stream.concat(
                robot
                    .getBulletHitEvents()
                    .stream()
                    .map(BulletHitEvent::new),
                robot.bulletHitEventQueue.stream())
                .collect(Collectors.toCollection(Vector::new));
        robot.bulletHitEventQueue = new LinkedList<>();
        return res;
    }

    /**
     * Returns a vector containing all {@link BulletMissedEvent}s currently in
     * the robot's queue.
     * <p>
     * Example:
     * <pre>
     *   for (BulletMissedEvent event : getBulletMissedEvents()) {
     *       <i>// do something with the event</i>
     *   }
     * </pre>
     *
     * @return a vector containing all BulletMissedEvents currently in the
     *         robot's queue
     * @see BulletMissedEvent
     */
    final Vector<BulletMissedEvent> getBulletMissedEvents() {
        Vector<BulletMissedEvent> res = Stream.concat(
                robot
                    .getBulletMissedEvents()
                    .stream()
                    .map(BulletMissedEvent::new),
                robot.bulletMissedEventQueue.stream())
                .collect(Collectors.toCollection(Vector::new));
        robot.bulletMissedEventQueue = new LinkedList<>();
        return res;
    }

    /**
     * Returns a vector containing all {@link HitByBulletEvent}s currently in
     * the robot's queue.
     * <p>
     * Example:
     * <pre>
     *   for (HitByBulletEvent event : getHitByBulletEvents()) {
     *       <i>// do something with the event</i>
     *   }
     * </pre>
     *
     * @return a vector containing all HitByBulletEvents currently in the
     *         robot's queue
     * @see HitByBulletEvent
     */
    final Vector<HitByBulletEvent> getHitByBulletEvents() {
        Vector<HitByBulletEvent> res = Stream.concat(
                robot
                    .getHitByBulletEvents()
                    .stream()
                    .map(HitByBulletEvent::new),
                robot.hitByBulletEventQueue.stream())
                .collect(Collectors.toCollection(Vector::new));
        robot.hitByBulletEventQueue = new LinkedList<>();
        return res;
    }

    /**
     * Returns a vector containing all {@link HitRobotEvent}s currently in the
     * robot's queue.
     * <p>
     * Example:
     * <pre>
     *   for (HitRobotEvent event : getHitRobotEvents()) {
     *       <i>// do something with the event</i>
     *   }
     * </pre>
     *
     * @return a vector containing all HitRobotEvents currently in the robot's
     *         queue
     * @see HitRobotEvent
     */
    final Vector<HitRobotEvent> getHitRobotEvents() {
        Vector<HitRobotEvent> res = Stream.concat(
                robot
                    .getHitRobotEvents()
                    .stream()
                    .map(HitRobotEvent::new),
                robot.hitRobotEventQueue.stream())
                .collect(Collectors.toCollection(Vector::new));
        robot.hitRobotEventQueue = new LinkedList<>();
        return res;
    }

    /**
     * Returns a vector containing all {@link HitWallEvent}s currently in the
     * robot's queue.
     * <p>
     * Example:
     * <pre>
     *   for (HitWallEvent event : getHitWallEvents()) {
     *       <i>// do something with the event</i>
     *   }
     * </pre>
     *
     * @return a vector containing all HitWallEvents currently in the robot's
     *         queue
     * @see HitWallEvent
     */
    final Vector<HitWallEvent> getHitWallEvents() {
        Vector<HitWallEvent> res = Stream.concat(
                robot
                    .getHitWallEvents()
                    .stream()
                    .map(HitWallEvent::new),
                robot.hitWallEventQueue.stream())
                .collect(Collectors.toCollection(Vector::new));
        robot.hitWallEventQueue = new LinkedList<>();
        return res;
    }

    /**
     * Returns a vector containing all {@link ScannedRobotEvent}s currently in
     * the robot's queue.
     * <p>
     * Example:
     * <pre>
     *   for (ScannedRobotEvent event : getScannedRobotEvents()) {
     *       <i>// do something with the event</i>
     *   }
     * </pre>
     *
     * @return a vector containing all ScannedRobotEvents currently in the
     *         robot's queue
     * @see ScannedRobotEvent
     */
    final Vector<ScannedRobotEvent> getScannedRobotEvents() {
        Vector<ScannedRobotEvent> res = Stream.concat(
                robot
                    .getScannedRobotEvents()
                    .stream()
                    .map(ScannedRobotEvent::new),
                robot.scannedRobotEventQueue.stream())
                .collect(Collectors.toCollection(Vector::new));
        robot.scannedRobotEventQueue = new LinkedList<>();
        return res;
    }

    /**
     * Returns a graphics context used for painting graphical items for the
     * robot.
     * <p>
     * This method is very useful for debugging your robot.
     * <p>
     * Note that the robot will only be painted if the "Paint" is enabled on the
     * robot's console window; otherwise the robot will never get painted (the
     * reason being that all robots might have graphical items that must be
     * painted, and then you might not be able to tell what graphical items that
     * have been painted for your robot).
     * <p>
     * Also note that the coordinate system for the graphical context where you
     * paint items fits for the Robocode coordinate system where (0, 0) is at
     * the bottom left corner of the battlefield, where X is towards right and Y
     * is upwards.
     *
     * @return a graphics context used for painting graphical items for the
     *         robot.
     */
    public Graphics2D getGraphics() {
        return robot.getGraphics();
    }
}

/**
 * This class represents a robot that can fight in a Robocode match. Its actual
 * behavior is defined by a {@link SimpleRobotBehavior}. Inherit from this class
 * and set the member {@link #behavior} to use it.
 */
abstract class SimpleRobot extends AdvancedRobot {
    /**
     * The behavior for this robot. All actions performed by this robot come
     * from here.
     */
    protected SimpleRobotBehavior behavior = null;

    Queue<BulletHitBulletEvent> bulletHitBulletEventQueue = new LinkedList<>();
    Queue<BulletHitEvent> bulletHitEventQueue = new LinkedList<>();
    Queue<BulletMissedEvent> bulletMissedEventQueue = new LinkedList<>();
    Queue<HitByBulletEvent> hitByBulletEventQueue = new LinkedList<>();
    Queue<HitRobotEvent> hitRobotEventQueue = new LinkedList<>();
    Queue<HitWallEvent> hitWallEventQueue = new LinkedList<>();
    Queue<ScannedRobotEvent> scannedRobotEventQueue = new LinkedList<>();

    @Override
    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        behavior.start();
        execute();

        while (true) {
            behavior.execute();
            execute();
        }
    }

    @Override
    public void onBulletHitBullet(robocode.BulletHitBulletEvent ex) {
        bulletHitBulletEventQueue.add(new BulletHitBulletEvent(ex));
    }

    @Override
    public void onBulletHit(robocode.BulletHitEvent ex) {
        bulletHitEventQueue.add(new BulletHitEvent(ex));
    }

    @Override
    public void onBulletMissed(robocode.BulletMissedEvent ex) {
        bulletMissedEventQueue.add(new BulletMissedEvent(ex));
    }

    @Override
    public void onHitByBullet(robocode.HitByBulletEvent ex) {
        hitByBulletEventQueue.add(new HitByBulletEvent(ex));
    }

    @Override
    public void onHitRobot(robocode.HitRobotEvent ex) {
        hitRobotEventQueue.add(new HitRobotEvent(ex));
    }

    @Override
    public void onHitWall(robocode.HitWallEvent ex) {
        hitWallEventQueue.add(new HitWallEvent(ex));
    }

    @Override
    public void onScannedRobot(robocode.ScannedRobotEvent ex) {
        scannedRobotEventQueue.add(new ScannedRobotEvent(ex));
    }
}
