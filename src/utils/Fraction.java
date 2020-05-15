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
    public Fraction(int numerator, int denominator) {
        if (denominator == 0)
            throw new WrongFractionException("Denominator is 0! To infinity and beyond!");
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        int GCD = this.getGCD(numerator, denominator);
        this.numerator = numerator / GCD;
        this.denominator = denominator / GCD;
    }

    /**
     * Получить новый объект класса Fraction
     *
     * @param numerator   числитель
     * @param denominator знаменатель
     * @return новый объект класса
     * @throws WrongFractionException если числитель меньше 0 или знаменатель меньше или равен 0
     */
    public static Fraction valueOf(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    /**
     * Получить наибольший общий делитель,
     * алгоритм Евклида
     *
     * @return НОД
     */
    private int getGCD(int a, int b) {
        int temp;
        do {
            temp = a % b;
            a = b;
            b = temp;
        } while (temp != 0);

        return Math.abs(a);
    }

    public Fraction copy() {
        return valueOf(numerator, denominator);
    }

    /**
     * Представление дроби в виде строки со сокращением.
     * Если числитель равен 0, то 0.
     * Если знаменатель равен 1, то убираем его.
     * Если модуль числителя больше знаменателя, то возвращаем целую часть + остаток со знаменателем,
     * иначе просто возвращаем дробь.
     *
     * @return дробь
     */
    @Override
    public String toString() {
        if (numerator == 0) return "0";
        if (numerator == denominator) return "1";
        if (denominator == 1) return String.valueOf(numerator);
        if (Math.abs(numerator) > denominator) {
            return numerator > 0 ? numerator / denominator + "+" + numerator % denominator + "/" + denominator :
                    "-(" + Math.abs(numerator) / denominator + "+" + Math.abs(numerator % denominator) + "/" + denominator + ")";
        }
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o && this.hashCode() == o.hashCode()) return true;
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
        return (this.numerator * o.denominator) - (o.numerator * this.denominator);
    }
}