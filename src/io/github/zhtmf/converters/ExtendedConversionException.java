package io.github.zhtmf.converters;

import io.github.zhtmf.ConversionException;

/**
 * Sub class for code coverage purposes.
 * @author dzh
 */
class ExtendedConversionException extends ConversionException implements ExactException{
    private static final long serialVersionUID = 1L;
    private Class<?> site;
    private int ordinal;
    public ExtendedConversionException(Class<?> enclosingEntityClass, String fieldName, String msg, Throwable cause) {
        super(enclosingEntityClass, fieldName, msg, cause);
    }
    public ExtendedConversionException(Class<?> enclosingEntityClass, String fieldName, String msg) {
        super(enclosingEntityClass, fieldName, msg);
    }
    public ExtendedConversionException(Class<?> enclosingEntityClass, String fieldName, Throwable cause) {
        super(enclosingEntityClass, fieldName, cause);
    }
    public ConversionException withSiteAndOrdinal(Class<?> site, int ordinal) {
        this.site = site;
        this.ordinal = ordinal;
        return this;
    }
    @Override
    public Class<?> getSite() {
        return site;
    }
    @Override
    public int getOrdinal() {
        return ordinal;
    }
}
