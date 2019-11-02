package io.github.zhtmf.converters;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.zhtmf.annotations.enums.NumericEnum;
import io.github.zhtmf.annotations.enums.StringEnum;
import io.github.zhtmf.converters.auxiliary.DataType;

/**
 * Dedicated subclass used to eliminate the branches in {@link #get(Object)} and
 * {@link #set(Object, Object)}
 */
class EnumFieldInfo extends FieldInfo {
    
    private final Map<Object,Object> mapValueByEnumMember;
    private final Map<Object,Object> mapEnumMemberByValue;

    EnumFieldInfo(Field field, DataType type, ClassInfo base) {
        super(field, type, base);
        Class<?> fieldClass = field.getType();
        
        Map<Object,Object> mapEnumMemberByValue = new HashMap<>();
        Map<Object,Object> mapValueByEnumMember = new HashMap<>();
        Object[] constants = fieldClass.getEnumConstants();
        switch(type) {
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
            if(StringEnum.class.isAssignableFrom(fieldClass)) {
                throw forContext(base.entityClass, name, "numeric enum dataType should implement NumericEnum, not StringEnum")
                    .withSiteAndOrdinal(EnumFieldInfo.class, 1);
            }
            for(Object constant:constants) {
                long val = 0;
                if(NumericEnum.class.isAssignableFrom(fieldClass)) {
                    val = ((NumericEnum)constant).getValue();
                }else {
                    val = Long.parseLong(constant.toString());
                }
                String error;
                if((error = DataTypeOperations.of(type).checkRange(val, true))!=null) {
                    throw forContext(base.entityClass, name, error)
                        .withSiteAndOrdinal(EnumFieldInfo.class, 7);
                }
                Long key = Long.valueOf(val);
                if(mapEnumMemberByValue.containsKey(key)) {
                    throw forContext(base.entityClass, name, "multiple enum members should have distinct values")
                    .withSiteAndOrdinal(EnumFieldInfo.class, 2);
                }
                mapEnumMemberByValue.put(key, constant);
                mapValueByEnumMember.put(constant, key);
            }
            break;
        case CHAR:
            if(NumericEnum.class.isAssignableFrom(fieldClass)) {
                throw forContext(base.entityClass, name, "CHAR should implement StringEnum, not NumericEnum")
                    .withSiteAndOrdinal(EnumFieldInfo.class, 3);
            }
            for(Object constant:constants) {
                String key = null;
                if(StringEnum.class.isAssignableFrom(fieldClass)) {
                    key = ((StringEnum)constant).getValue();
                }else {
                    key = constant.toString();
                }
                if(key==null) {
                    throw forContext(base.entityClass, name,
                            "members of an enum mapped to string values should return"
                            + " non-null string as values")
                    .withSiteAndOrdinal(EnumFieldInfo.class, 4);
                }
                if(mapEnumMemberByValue.containsKey(key)) {
                    throw forContext(base.entityClass, name, "multiple enum members should have distinct values")
                    .withSiteAndOrdinal(EnumFieldInfo.class, 5);
                }
                mapEnumMemberByValue.put(key, constant);
                mapValueByEnumMember.put(constant, key);
            }
            break;
        default:
            throw new Error("should not reach here");
        }
        this.mapValueByEnumMember = Collections.unmodifiableMap(mapValueByEnumMember);
        this.mapEnumMemberByValue = Collections.unmodifiableMap(mapEnumMemberByValue);
    }
    
    @Override
    public Class<?> getFieldType() {
        //this method will be called prior to constructor
        return DataTypeOperations.of(super.dataType).mappedEnumFieldClass();
    }

    @Override
    public Object get(Object self) {
        return mapValueByEnumMember.get(super.get(self));
    }
    
    @Override
    public void set(Object self, Object val) {
        val = mapEnumMemberByValue.get(val);
        if(val==null) {
            throw forContext(base.entityClass, name, "unmapped enum value:"+val)
                .withSiteAndOrdinal(EnumFieldInfo.class, 6);
        }
        super.set(self, val);
    }
}