package test.exceptions;

import org.dzh.bytesutil.ConversionException;
import org.dzh.bytesutil.DataPacket;
import org.dzh.bytesutil.annotations.modifiers.Length;
import org.dzh.bytesutil.annotations.modifiers.Order;
import org.dzh.bytesutil.annotations.types.CHAR;
import org.dzh.bytesutil.converters.auxiliary.Utils;
import org.junit.Assert;
import org.junit.Test;

import test.TestUtils;

public class TestCaseCHARSerialization {
	public static class Entity0 extends DataPacket{
		@Order(0)
		@CHAR
		@Length
		public byte b;
	}
	@Test
	public void test0() throws ConversionException {
		Entity0 entity = new Entity0();
		try {
			entity.b = -3;
			entity.serialize(TestUtils.newByteArrayOutputStream());
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, Utils.class, 0);
			return;
		}
	}
	public static class Entity1 extends DataPacket{
		@Order(0)
		@CHAR(2)
		public byte b;
	}
	@Test
	public void test1() throws ConversionException {
		Entity1 entity = new Entity1();
		try {
			entity.b = 120;
			entity.serialize(TestUtils.newByteArrayOutputStream());
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, Utils.class, 2);
			return;
		}
	}
	public static class Entity2 extends DataPacket{
		@Order(0)
		@CHAR(2)
		public byte b;
	}
	public static class Entity3 extends DataPacket{
		@Order(0)
		@CHAR(20)
		public byte b;
	}
	@Test
	public void test2() throws ConversionException {
		Entity2 entity = new Entity2();
		try {
			entity.deserialize(TestUtils.newInputStream(new byte[] {(byte)'0',(byte)'9'}));
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, Utils.class, 10);
		}
		try {
			entity.deserialize(TestUtils.newInputStream(new byte[] {(byte)'1',(byte)'/'}));
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, Utils.class, 3);
		}
		try {
			entity.deserialize(TestUtils.newInputStream(new byte[] {(byte)'1',(byte)';'}));
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, Utils.class, 3);
		}
		try {
			new Entity3().deserialize(TestUtils.newInputStream(new byte[] {'9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9',
																	'9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9',
																	'9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9','9',}));
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, Utils.class, 3);
		}
	}
}