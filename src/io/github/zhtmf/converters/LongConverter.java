package io.github.zhtmf.converters;

import java.io.IOException;
import java.io.OutputStream;

import io.github.zhtmf.ConversionException;
import io.github.zhtmf.annotations.types.BCD;
import io.github.zhtmf.converters.auxiliary.DataType;

import static io.github.zhtmf.converters.StreamUtils.*;

class LongConverter implements Converter<Long> {

    @Override
    public void serialize(Long value, OutputStream dest, FieldInfo ctx, Object self)
            throws IOException, ConversionException {
        long val = value;
        switch(ctx.dataType) {
        case BYTE:{
            checkRangeInContext(DataType.BYTE, val, ctx);
            writeBYTE(dest, (byte)val);
            return;
        }
        case SHORT:{
            checkRangeInContext(DataType.SHORT, val, ctx);
            writeSHORT(dest, (short) val, ctx.bigEndian);
            return;
        }
        case INT:{
            checkRangeInContext(DataType.INT, val, ctx);
            writeInt(dest, (int) val, ctx.bigEndian);
            return;
        }
        case INT3:{
            checkRangeInContext(DataType.INT3, val, ctx);
            writeInt3(dest, (int)val, ctx.bigEndian);
            return;
        }
        case INT5:{
            checkRangeInContext(DataType.INT5, val, ctx);
            writeInt5(dest, val, ctx.bigEndian);
            return;
        }
        case INT6:{
            checkRangeInContext(DataType.INT6, val, ctx);
            writeInt6(dest, val, ctx.bigEndian);
            return;
        }
        case INT7:{
            checkRangeInContext(DataType.INT7, val, ctx);
            writeInt7(dest, val, ctx.bigEndian);
            return;
        }
        case LONG:{
            writeLong(dest, val, ctx.bigEndian);
            return;
        }
        case CHAR:
            serializeAsCHAR(val, dest, ctx, self);
            return;
        case BCD:
            writeBCD(
                    dest, checkAndConvertToBCD(val, ctx.localAnnotation(BCD.class).value()));
            return;
        default:throw new Error("cannot happen");
        }
    }

    @Override
    public Long deserialize(java.io.InputStream is, FieldInfo ctx, Object self)
            throws IOException, ConversionException {
        switch(ctx.dataType) {
        case BYTE:{
            return (long)readByte(is, ctx.signed);
        }
        case SHORT:{
            return (long)readShort(is, ctx.signed, ctx.bigEndian);
        }
        case INT:{
            return readInt(is, ctx.signed, ctx.bigEndian);
        }
        case INT3:{
            return (long)readInt3(is, ctx.signed, ctx.bigEndian);
        }
        case INT5:{
            return readInt5(is, ctx.signed, ctx.bigEndian);
        }
        case INT6:{
            return readInt6(is, ctx.signed, ctx.bigEndian);
        }
        case INT7:{
            return readInt7(is, ctx.signed, ctx.bigEndian);
        }
        case LONG:{
            return readLong(is, ctx.bigEndian);
        }
        case CHAR:{
            return deserializeAsCHAR(is, ctx, self, null);
        }
        case BCD:{
            return readIntegerBCD(is, ctx.localAnnotation(BCD.class).value());
        }
        default:throw new Error("cannot happen");
        }
    }
}
