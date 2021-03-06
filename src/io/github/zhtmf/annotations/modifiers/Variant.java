package io.github.zhtmf.annotations.modifiers;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.github.zhtmf.converters.auxiliary.EntityHandler;

/**
 * Used to indicate instantiation of the class of a field (or its components if
 * it is declared as a list of entities) needs custom logic rather than plain
 * reflection.
 * <p>
 * Typical uses of this annotation are when the object being constructed need to
 * carry over some properties from its "parent" or when the declaring type of a
 * field is an interface/abstract class and should be instantiated as one
 * concrete implementation.
 * <p>
 * Without this annotation, this library will try to instantiate classes with
 * their no-arg constructors.
 * <p>
 * 
 * @author dzh
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Variant {
    /**
     * Class of the {@link EntityHandler} whose instance be created and called
     * during deserialization to initiate this field.
     * 
     * @return class that extends {@link EntityHandler}
     */
    Class<? extends EntityHandler> value();
}