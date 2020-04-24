package utils;

import java.util.Objects;

/**
 * Класс, представляющий собой рациональое число
 *
 * @author Aleksei Vokhmin
 */
public class Fraction implements Comparable<Fraction> {
    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) throws WrongFractionException {
        if (numerator < 0)
            throw new WrongFractionException("Numerator must be greater than 0!");
        if (denominator <= 0)
            throw new WrongFractionException("Denominator is 0 or less! To infinity and beyond!");
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction valueOf(int numerator, int denominator) throws WrongFractionException {
        return new Fraction(numerator, denominator);
    }

    public int getIntValue() {
        return numerator / denominator;
    }

    @Override
    public String toString() {
        if (numerator == 0) return "0";
        if (numerator == denominator) return "1";
        if (numerator > denominator) {
            if (numerator % denominator == 0)
                return String.valueOf(getIntValue());
            return numerator / denominator + " " + numerator % denominator + "/" + denominator;
        }
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fraction)) return false;
        Fraction fraction = (Fraction) o;
        return numerator == fraction.numerator &&
                denominator == fraction.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public int compareTo(Fraction o) {
        Double d1 = (double) numerator / denominator;
        Double d2 = (double) o.numerator / o.denominator;
        return d1.compareTo(d2);
    }
}