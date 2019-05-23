package io.github.zhtmf.converters;

import java.io.IOException;
import java.io.OutputStream;

import io.github.zhtmf.ConversionException;
import io.github.zhtmf.annotations.types.BCD;
import io.github.zhtmf.converters.auxiliary.DataType;
import io.github.zhtmf.converters.auxiliary.FieldInfo;
import io.github.zhtmf.converters.auxiliary.MarkableInputStream;
import io.github.zhtmf.converters.auxiliary.StreamUtils;
import io.github.zhtmf.converters.auxiliary.Utils;

public class LongConverter implements Converter<Long> {

    @Override
    public void serialize(Long value, OutputStream dest, FieldInfo ctx, Object self)
            throws IOException, ConversionException {
        long val = value;
        switch(ctx.dataType) {
        case BYTE:{
            Utils.checkRangeInContext(DataType.BYTE, val, ctx);
            StreamUtils.writeBYTE(dest, (byte)val);
            return;
        }
        case SHORT:{
            Utils.checkRangeInContext(DataType.SHORT, val, ctx);
            StreamUtils.writeSHORT(dest, (short) val, ctx.bigEndian);
            return;
        }
        case INT:{
            Utils.checkRangeInContext(DataType.INT, val, ctx);
            StreamUtils.writeInt(dest, (int) val, ctx.bigEndian);
            return;
        }
        case LONG:{
            StreamUtils.writeLong(dest, val, ctx.bigEndian);
            return;
        }
        case CHAR:
            Utils.serializeAsCHAR(val, dest, ctx, self);
            return;
        case BCD:
            StreamUtils.writeBCD(
                    dest, Utils.checkAndConvertToBCD(val, ctx.localAnnotation(BCD.class).value()));
            return;
        default:throw new Error("cannot happen");
        }
    }

    @Override
    public Long deserialize(MarkableInputStream is, FieldInfo ctx, Object self)
            throws IOException, ConversionException {
        switch(ctx.dataType) {
        case BYTE:{
            return (long)StreamUtils.readByte(is, ctx.signed);
        }
        case SHORT:{
            return (long)StreamUtils.readShort(is, ctx.signed, ctx.bigEndian);
        }
        case INT:{
            return StreamUtils.readInt(is, ctx.signed, ctx.bigEndian);
        }
        case LONG:{
            return StreamUtils.readLong(is, ctx.bigEndian);
        }
        case CHAR:{
            return Utils.deserializeAsCHAR(is, ctx, self, null);
        }
        case BCD:{
            return StreamUtils.readIntegerBCD(is, ctx.localAnnotation(BCD.class).value());
        }
        default:throw new Error("cannot happen");
        }
    }
}