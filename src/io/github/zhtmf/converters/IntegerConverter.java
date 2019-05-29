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

public class IntegerConverter implements Converter<Integer> {

    @Override
    public void serialize(Integer value, OutputStream dest, FieldInfo ctx, Object self)
            throws IOException,ConversionException {
        int val = value;
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
            StreamUtils.writeInt(dest, val, ctx.bigEndian);
            return;
        }
        case INT3:{
            Utils.checkRangeInContext(DataType.INT3, val, ctx);
            StreamUtils.writeInt3(dest, val, ctx.bigEndian);
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
    public Integer deserialize(MarkableInputStream is, FieldInfo ctx, Object self)
            throws IOException, ConversionException {
        switch(ctx.dataType) {
        case BYTE:{
            return StreamUtils.readByte(is, ctx.signed);
        }
        case SHORT:{
            return StreamUtils.readShort(is, ctx.signed, ctx.bigEndian);
        }
        case INT:{
            return (int)StreamUtils.readInt(is, ctx.signed, ctx.bigEndian);
        }
        case INT3:{
            return (int)StreamUtils.readInt3(is, ctx.signed, ctx.bigEndian);
        }
        case CHAR:{
            return (int)Utils.deserializeAsCHAR(is, ctx, self, DataType.INT);
        }
        case BCD:{
            long val = StreamUtils.readIntegerBCD(is, ctx.localAnnotation(BCD.class).value());
            Utils.checkRangeInContext(DataType.INT, val, ctx);
            return (int) val;
        }
        default:throw new Error("cannot happen");
        }
    }
}
