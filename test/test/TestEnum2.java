package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.dzh.bytesutil.ConversionException;
import org.dzh.bytesutil.DataPacket;
import org.dzh.bytesutil.annotations.enums.NumericEnum;
import org.dzh.bytesutil.annotations.enums.StringEnum;
import org.dzh.bytesutil.annotations.modifiers.Order;
import org.dzh.bytesutil.annotations.types.CHAR;
import org.dzh.bytesutil.annotations.types.INT;
import org.junit.Assert;
import org.junit.Test;

public class TestEnum2 {

	private enum ErrorEnum1{
		FLAG1 {
			@Override
			public String toString() {
				return "1";
			}
		},
		FLAG2 {
			@Override
			public String toString() {
				return "1";
			}
		};
	}
	
	private enum ErrorEnum2 implements NumericEnum{
		FLAG1 {
			@Override
			public long getValue() {
				return 233;
			}
		},
		FLAG2 {
			@Override
			public long getValue() {
				return 233;
			}
		};
		@Override
		public abstract long getValue();
	}
	
	private enum ErrorEnum3 implements StringEnum{
		FLAG1 {
			@Override
			public String getValue() {
				return "";
			}
		},
		FLAG2 {
			@Override
			public String getValue() {
				return "";
			}
		};
	}
	
	public static class Test1 extends DataPacket{
		@INT
		@Order(0)
		public ErrorEnum1 error1;
	}
	
	public static class Test2 extends DataPacket{
		@INT
		@Order(0)
		public ErrorEnum2 error2;
	}
	
	public static class Test3 extends DataPacket{
		@CHAR(3)
		@Order(0)
		public ErrorEnum1 error1;
	}
	
	public static class Test4 extends DataPacket{
		@CHAR(3)
		@Order(0)
		public ErrorEnum3 error3;
	}
	
	@Test
	public void test() throws ConversionException, IOException {
		
		{
			Test1 inst = new Test1();
			try {
				inst.serialize(new ByteArrayOutputStream());
				Assert.fail();
			} catch (IllegalArgumentException e) {
				System.out.println("successfully produced error:"+e.getMessage());
			}
		}
		{
			Test2 inst = new Test2();
			try {
				inst.serialize(new ByteArrayOutputStream());
				Assert.fail();
			} catch (IllegalArgumentException e) {
				System.out.println("successfully produced error:"+e.getMessage());
			}
		}
		{
			Test3 inst = new Test3();
			try {
				inst.serialize(new ByteArrayOutputStream());
				Assert.fail();
			} catch (IllegalArgumentException e) {
				System.out.println("successfully produced error:"+e.getMessage());
			}
		}
		{
			Test4 inst = new Test4();
			try {
				inst.serialize(new ByteArrayOutputStream());
				Assert.fail();
			} catch (IllegalArgumentException e) {
				System.out.println("successfully produced error:"+e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new TestEnum2().test();
	}
}
