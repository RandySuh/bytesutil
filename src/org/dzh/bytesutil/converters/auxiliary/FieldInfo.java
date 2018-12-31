package org.dzh.bytesutil.converters.auxiliary;

import static org.dzh.bytesutil.converters.auxiliary.Utils.forContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dzh.bytesutil.DataPacket;
import org.dzh.bytesutil.annotations.modifiers.BigEndian;
import org.dzh.bytesutil.annotations.modifiers.CHARSET;
import org.dzh.bytesutil.annotations.modifiers.DatePattern;
import org.dzh.bytesutil.annotations.modifiers.Length;
import org.dzh.bytesutil.annotations.modifiers.ListLength;
import org.dzh.bytesutil.annotations.modifiers.LittleEndian;
import org.dzh.bytesutil.annotations.modifiers.Signed;
import org.dzh.bytesutil.annotations.modifiers.Unsigned;
import org.dzh.bytesutil.annotations.modifiers.Variant;

/**
 * Internal class that stores compile-time information of a {@link Field}
 * 
 * @author dzh
 */
public class FieldInfo{
	private final Field field;
	protected final ClassInfo base;
	private final Map<Class<? extends Annotation>,Annotation> annotations;
	private final Class<?> fieldClass;
	
	public final String name;
	public final DataType type;
	public final boolean isEntity;
	public final boolean isEntityList;
	public final Class<?> listComponentClass;
	
	public final EntityHandler entityCreator;
	
	/**
	 * Entity class that declares this field
	 */
	public final Class<?> enclosingEntityClass;
	/**
	 * whether this field is defined as little-endian
	 */
	public final boolean littleEndian;
	/**
	 * whether this field is defined as big-endian
	 */
	public final boolean bigEndian;
	/**
	 * whether this field is defined as signed
	 */
	public final boolean signed;
	/**
	 * whether this field is defined as unsigned
	 */
	public final boolean unsigned;
	/**
	 * Value of {@link Length} annotation.
	 * <p>
	 * If the annotation is absent, or it declares a dynamic length, value of this
	 * field will be -1.
	 */
	public final int length;
	public final int listLength;
	/**
	 * Type of the {@link Length} value in the stream. For example some protocol
	 * defines length as unsigned byte, while others defines it as unsigned short
	 * etc.
	 */
	private final DataType lengthType;
	private final DataType listLengthType;
	/**
	 * {@link ModifierHandler} object which is used to obtain the length value
	 * dynamically for this field, null if it is not defined in the {@link Length}
	 * annotation.
	 */
	public final ModifierHandler<Integer> lengthHandler;
	public final ModifierHandler<Integer> listLengthHandler;
	/**
	 * Charset of this field, null if not defined
	 */
	public final Charset charset;
	/**
	 * {@link ModifierHandler} object which is used to obtain Charset object dynamically 
	 * for this field, null if it is not defined in the {@link CHARSET}
	 * annotation.
	 */
	public final ModifierHandler<Charset> charsetHandler;
	/**
	 * Pattern string defined in {@link DatePattern} annotation, null if not present.
	 */
	public final String datePattern;
	/**
	 * Whether Length/ListLength annotation is present, used only by ClassInfo
	 */
	final boolean lengthDefined;
	
	FieldInfo(Field field, DataType type, ClassInfo base) {
		this.base = base;
		this.field = field;
		this.name = field.getName();
		final Class<?> fieldClass = field.getType();
		this.fieldClass = fieldClass;
		this.type = type;
		this.enclosingEntityClass = field.getDeclaringClass();
		
		this.isEntity = DataPacket.class.isAssignableFrom(fieldClass);
		
		if(List.class.isAssignableFrom(fieldClass)) {
			Class<?> componentClass = ClassInfo.firstTypeParameterClass(field);
			if(componentClass==null) {
				throw forContext(base.entityClass, name, "should declare type parameter if it is a List");
			}
			this.listComponentClass = componentClass;
			this.isEntityList = DataPacket.class.isAssignableFrom(listComponentClass);
		}else {
			this.listComponentClass = null;
			this.isEntityList = false;
		}
		
		Map<Class<? extends Annotation>,Annotation> _annotations = new HashMap<>();
		for(Annotation an:field.getAnnotations()) {
			_annotations.put(an.annotationType(), an);
		}
		this.annotations = Collections.unmodifiableMap(_annotations);
		
		if(isEntity) {
			Variant cond = localAnnotation(Variant.class);
			if(cond==null) {
				this.entityCreator = new EntityHandler() {
					
					@Override
					public DataPacket handle0(String fieldName, Object entity, InputStream is) throws IOException {
						try {
							return (DataPacket) fieldClass.newInstance();
						} catch (InstantiationException | IllegalAccessException e) {
							throw new RuntimeException(e);
						}
					}
				};
			}else {
				try {
					this.entityCreator = cond.value().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw forContext(base.entityClass, name, "VariantEntityHandler cannot be initialized by no-arg contructor");
				}
				
			}
		}else{
			this.entityCreator = null;
		}
		
		{
			Annotation ret = mutualExclusive(BigEndian.class,LittleEndian.class);
			this.littleEndian = ret!=null && ret instanceof LittleEndian;
			this.bigEndian = !littleEndian;
		}
		{
			Annotation ret = mutualExclusive(Unsigned.class,Signed.class);
			this.signed = ret!=null && ret instanceof Signed;
			this.unsigned = !signed;
		}
		{
			CHARSET cs = annotation(CHARSET.class);
			if(cs==null) {
				charset = CHARSET.DEFAULT_CHARSET;
				charsetHandler = null;
			}else if( ! PlaceHolderHandler.class.isAssignableFrom(cs.handler())) {
				charset = null;
				try {
					charsetHandler = cs.handler().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw forContext(base.entityClass, name, "Charset ModifierHandler cannot be initialized by no-arg contructor");
				}
			}else {
				try {
					charset = Charset.forName(cs.value());
				} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
					throw forContext(base.entityClass, name, "Illegal charset name");
				}
				charsetHandler = null;
			}
		}
		{
			Length len = localAnnotation(Length.class);
			if(len==null) {
				length = -1;
				lengthHandler = null;
				lengthType = null;
				lengthDefined = false;
			}else {
				this.length = len.value();
				if( ! PlaceHolderHandler.class.isAssignableFrom(len.handler())) {
					try {
						this.lengthHandler = len.handler().newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw forContext(base.entityClass, name, "Length ModifierHandler cannot be initialized by no-arg contructor");
					}
				}else {
					this.lengthHandler = null;
				}
				lengthType = len.type();
				switch(lengthType) {
				case BYTE:
				case SHORT:
				case INT:
					break;
				default:
					throw new IllegalArgumentException("data type "+lengthType+" cannot be set as length type");
				}
				
				lengthDefined = true;
			}
		}
		{
			ListLength len = localAnnotation(ListLength.class);
			if(len==null) {
				listLength = -1;
				listLengthHandler = null;
				listLengthType = null;
			}else {
				this.listLength = len.value();
				if( ! PlaceHolderHandler.class.isAssignableFrom(len.handler())) {
					try {
						this.listLengthHandler = len.handler().newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw forContext(base.entityClass, name, "ListLength ModifierHandler cannot be initialized by no-arg contructor");
					}
				}else {
					this.listLengthHandler = null;
				}
				listLengthType = len.type();
			}
		}
		{
			DatePattern df = localAnnotation(DatePattern.class);
			if(df==null) {
				this.datePattern = null;
			}else {
				String val = df.value();
				for(int i=0;i<val.length();++i) {
					if(val.charAt(i)>127) {
						throw forContext(base.entityClass, name, "Illegal date pattern,"
								+ " only ASCII characters are permitted");
					}
				}
				this.datePattern = val;
			}
		}
	}
	/**
	 * Wrapper of {@link Field#get(Object)}
	 * @param self this object
	 * @return
	 */
	public Object get(Object self) {
		try {
			return field.get(self);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			//for backward-compatibility, do not throw checked exception here 
			throw new Error(
					String.format("cannot obtain value of field [%s] by reflection"
							,field.getName()),e);
		}
	}
	/**
	 * Wrapper of {@link Field#set(Object, Object)}
	 * @param self
	 * @param val
	 */
	public void set(Object self, Object val) {
		try {
			field.set(self, val);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			//for backward-compatibility, do not throw checked exception here 
			throw new Error(
					String.format("cannot set value of field [%s] by reflection"
							,field.getName()),e);
		}
	}
	
	/**
	 * Returns lengthType or listLengthType
	 * @return
	 */
	public DataType lengthType() {
		return lengthType!=null ? lengthType : listLengthType;
	}
	
	/**
	 * Get class of this field,
	 * intended to be overridden by sub-classes
	 * @return
	 */
	public Class<?> getFieldType(){
		return fieldClass;
	}
	
	/**
	 * Annotation of specific class for this field, if the same annotation is
	 * present on both field and class, the one on the field always take precedence.
	 * 
	 * @param annoCls
	 * @return null if it is not present both on the field or on the class
	 */
	public <T extends Annotation> T annotation(Class<T> annoCls) {
		T anno = localAnnotation(annoCls);
		if(anno!=null) {
			return anno;
		}
		return globalAnnotation(annoCls);
	}
	
	/**
	 * Annotation of specific class present on the field.
	 * 
	 * @param annoCls
	 * @return returns null if it is not present on the field, even it is present on
	 *         the class
	 */
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T localAnnotation(Class<T> annoCls) {
		return (T) annotations.get(annoCls);
	}

	/**
	 * Annotation of specific class present on the class definition of this field.
	 * 
	 * @param annoCls
	 * @return	returns null if it is not present
	 */
	public <T extends Annotation> T globalAnnotation(Class<T> annoCls) {
		return base.globalAnnotation(annoCls);
	}
	
	private Annotation mutualExclusive(Class<? extends Annotation> def, Class<? extends Annotation> another) {
		Annotation local1 = localAnnotation(def);
		Annotation local2 = localAnnotation(another);
		if(local1!=null && local2!=null) {
			throw forContext(base.entityClass, name, 
					String.format("[%s] and [%s] should not be both present on the same field declaration",def,another));
		}else if(local1!=null && local2==null) {
			return local1;
		}else if(local1==null && local2!=null) {
			return local2;
		}
		Annotation global1 = globalAnnotation(def);
		Annotation global2 = globalAnnotation(another);
		if(global1!=null && global2!=null) {
			throw forContext(base.entityClass, name, 
					String.format("[%s] and [%s] should not be both present on the same class declaration",def,another));
		}else if(global1!=null && global2==null) {
			return global1;
		}else if(global1==null && global2!=null) {
			return global2;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "FieldInfo:Entity["+enclosingEntityClass+"],Field:["+name+"]";
	}
}