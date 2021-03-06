package examples.javaclass.entities.attributeinfo.info;

import io.github.zhtmf.DataPacket;
import io.github.zhtmf.annotations.modifiers.Order;
import io.github.zhtmf.annotations.modifiers.Unsigned;
import io.github.zhtmf.annotations.types.SHORT;

@Unsigned
public class LocalVariable extends DataPacket{
    @Order(0)
    @SHORT
    public int startPc;
    @Order(1)
    @SHORT
    public int length;
    @Order(2)
    @SHORT
    public int nameIndex;
    @Order(3)
    @SHORT
    public int descriptorIndex;
    @Order(4)
    @SHORT
    public int index;
}
