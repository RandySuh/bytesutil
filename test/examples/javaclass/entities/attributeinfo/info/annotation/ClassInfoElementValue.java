package examples.javaclass.entities.attributeinfo.info.annotation;

import io.github.zhtmf.annotations.modifiers.Order;
import io.github.zhtmf.annotations.modifiers.Unsigned;
import io.github.zhtmf.annotations.types.SHORT;

@Unsigned
public class ClassInfoElementValue extends ElementValue{
    @Order(0)
    @SHORT
    public int classInfoIndex;
}
