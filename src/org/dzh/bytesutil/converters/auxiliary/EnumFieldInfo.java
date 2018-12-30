package org.dzh.bytesutil.converters.auxiliary;

import static org.dzh.bytesutil.converters.auxiliary.Utils.forContext;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dzh.bytesutil.annotations.enums.NumericEnum;
import org.dzh.bytesutil.annotations.enums.StringEnum;

/**
 * Dedicated subclass used to eliminate the branches in {@link #get(Object)} and
 * {@link #set(Object, Object)}
 */
class EnumFieldInfo extends FieldInfo {
	
	private final Map<Object,Object> mapValueByEnumMember;
	private final Map<Object,Object> mapEnumMemberByValue;
	private final Class<?> mappedEnumFieldClass;

	EnumFieldInfo(Field field, DataType type, ClassInfo base) {
		super(field, type, base);
		Class<?> fieldClass = field.getType();
		
		switch(type) {
		case BYTE:
		case SHORT:
		case INT:
			this.mappedEnumFieldClass = Long.class;
			break;
		case CHAR:
			this.mappedEnumFieldClass = String.class;
			break;
		default:
			throw forContext(base.entityClass, name, "enum type fields should be declared as a numeric type or CHAR");
		}
		
		Map<Object,Object> mapEnumMemberByValue = new HashMap<>();
		Map<Object,Object> mapValueByEnumMember = new HashMap<>();
		Object[] constants = fieldClass.getEnumConstants();
		switch(type) {
		case BYTE:
		case SHORT:
		case INT:
			@SuppressWarnings("unchecked")
			Class<? extends Number> numberClass = (Class<? extends Number>) type.correspondingJavaClass();
			if(StringEnum.class.isAssignableFrom(fieldClass)) {
				throw forContext(base.entityClass, name, "numeric enum type should implement NumericEnum, not StringEnum");
			}
			for(Object constant:constants) {
				long val = 0;
				if(NumericEnum.class.isAssignableFrom(fieldClass)) {
					val = ((NumericEnum)constant).getValue();
				}else {
					val = Long.parseLong(constant.toString());
				}
				Utils.checkRange(val, numberClass, unsigned);
				Long key = Long.valueOf(val);
				if(mapEnumMemberByValue.containsKey(key)) {
					throw forContext(base.entityClass, name, "multiple enum members should have distinct values");
				}
				mapEnumMemberByValue.put(key, constant);
				mapValueByEnumMember.put(constant, key);
			}
			break;
		case CHAR:
			if(NumericEnum.class.isAssignableFrom(fieldClass)) {
				throw forContext(base.entityClass, name, "numeric enum type should implements StringEnum, not NumericEnum");
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
							+ " non-null string as values");
				}
				if(mapEnumMemberByValue.containsKey(key)) {
					throw forContext(base.entityClass, name, "multiple enum members should have distinct values");
				}
				mapEnumMemberByValue.put(key, constant);
				mapValueByEnumMember.put(constant, key);
			}
			break;
		default:
			throw forContext(base.entityClass, name, "enum type fields should be declared as a numeric type or CHAR");
		}
		this.mapValueByEnumMember = Collections.unmodifiableMap(mapValueByEnumMember);
		this.mapEnumMemberByValue = Collections.unmodifiableMap(mapEnumMemberByValue);
	}
	
	@Override
	public Class<?> getFieldType() {
		return mappedEnumFieldClass;
	}

	@Override
	public Object get(Object self) {
		return mapValueByEnumMember.get(super.get(self));
	}
	
	@Override
	public void set(Object self, Object val) {
		val = mapEnumMemberByValue.get(val);
		if(val==null) {
			throw forContext(base.entityClass, name, "unmapped enum value:"+val);
		}
		super.set(self, val);
	}
}
