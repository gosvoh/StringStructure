package utils;

/**
 * Отслеживаемое исключение для класса рациональных чисел
 *
 * @author Aleksei Vokhmin
 * @see Fraction
 */
public class WrongFractionException extends Exception {
    public WrongFractionException() {
    }

    public WrongFractionException(String msg) {
        super(msg);
    }
}
