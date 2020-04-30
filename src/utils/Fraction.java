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

    /**
     * Конструктор рациональной броби
     *
     * @param numerator   числитель
     * @param denominator знаменатель
     * @throws WrongFractionException если числитель меньше 0 или знаменатель меньше или равен 0
     */
    public Fraction(int numerator, int denominator) throws WrongFractionException {
        if (numerator < 0)
            throw new WrongFractionException("Numerator must be greater than 0!");
        if (denominator <= 0)
            throw new WrongFractionException("Denominator is 0 or less! To infinity and beyond!");
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Получить новый объект класса Fraction
     *
     * @param numerator   числитель
     * @param denominator знаменатель
     * @return новый объект класса
     * @throws WrongFractionException если числитель меньше 0 или знаменатель меньше или равен 0
     */
    public static Fraction valueOf(int numerator, int denominator) throws WrongFractionException {
        return new Fraction(numerator, denominator);
    }

    /**
     * Представление дроби в виде строки со сокращением.
     * Если знаменатель равен 1, то убираем его.
     * Если числитель больше знаменателя и если удаётся поделить бробь без остатка,
     * то возвращаем сокращённое целочисленное число или возвращаем целую часть + остаток со знаменателем,
     * иначе просто возвращаем дробь.
     *
     * @return дробь
     */
    @Override
    public String toString() {
        if (numerator == 0) return "0";
        // Сокращаем дробь, если числитель равен знаменателю
        if (numerator == denominator) return "1";
        // Если знаменатель равен 1, то убираем его
        if (denominator == 1) return String.valueOf(numerator);
        // Если числитель больше знаменателя...
        if (numerator > denominator) {
            // ...и если удаётся поделить бробь без остатка...
            if (numerator % denominator == 0)
                // ...то возвращаем сокращённое целочисленное число...
                return String.valueOf(numerator / denominator);
            // ...или возвращаем целую часть + остаток со знаменателем.
            return numerator / denominator + "+" + numerator % denominator + "/" + denominator;
        }
        // ...иначе просто возвращаем дробь
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fraction)) return false;
        Fraction fraction = (Fraction) o;
        return this.compareTo(fraction) == 0;
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