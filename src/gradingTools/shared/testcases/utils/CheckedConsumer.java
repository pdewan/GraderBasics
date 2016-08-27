package gradingTools.shared.testcases.utils;

import java.util.Objects;

/**
 *
 * @author Andrew Vitkus
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    void accept(T t) throws Exception;

    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
