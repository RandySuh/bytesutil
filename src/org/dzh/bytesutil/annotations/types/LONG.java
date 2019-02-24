package org.dzh.bytesutil.annotations.types;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.dzh.bytesutil.annotations.modifiers.BigEndian;
import org.dzh.bytesutil.annotations.modifiers.LittleEndian;
import org.dzh.bytesutil.annotations.modifiers.Signed;
import org.dzh.bytesutil.annotations.modifiers.Unsigned;

/**
 * 8-byte integral dataType.
 * <p>
 * Signed/Unsigned is specified with {@link Signed} / {@link Unsigned}
 * annotation. Endianness is specified with {@link BigEndian} /
 * {@link LittleEndian} annotation.
 * <p>
 * Convertible with <code>byte</code>,
 * <code>short</code>,<code>int</code>,<code>long</code> and their wrapper
 * classes, <code>java.math.BigInteger</code> and {@link java.util.Date}.
 * <p>
 * When interpreted as a date, it is assumed that this integer stores
 * milliseconds since epoch.
 * <p>
 * Conversion with <code>java.math.BigInteger</code> exists to support
 * processing unsigned 8-byte integral values.
 * <p>
 * It is not an error to store an {@link Unsigned} value in such a field,
 * however incorrect values may be observed in Java code due to overflow.
 * 
 * @author dzh
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface LONG {

}
