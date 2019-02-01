package test.general.entities;

import org.dzh.bytesutil.DataPacket;
import org.dzh.bytesutil.annotations.modifiers.CHARSET;
import org.dzh.bytesutil.annotations.modifiers.Order;
import org.dzh.bytesutil.annotations.modifiers.Signed;
import org.dzh.bytesutil.annotations.types.CHAR;
import org.dzh.bytesutil.annotations.types.INT;

public class SubEntity extends DataPacket{
	
	public byte carryOver;
	
	@Order(0)
	@INT
	@Signed
	public int integerA;
	
	@Order(1)
	@CHAR(15)
	@CHARSET("GBK")
	public String strB;
	
	public SubEntity(int i, float j) {
		//no-op
	}

	public SubEntity(int integerA, String strB) {
		this.integerA = integerA;
		this.strB = strB;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + integerA;
		result = prime * result + ((strB == null) ? 0 : strB.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubEntity other = (SubEntity) obj;
		if (integerA != other.integerA)
			return false;
		if (strB == null) {
			if (other.strB != null)
				return false;
		} else if (!strB.equals(other.strB))
			return false;
		return true;
	}
}