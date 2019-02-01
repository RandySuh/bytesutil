package test.exceptions;

import org.dzh.bytesutil.ConversionException;
import org.dzh.bytesutil.DataPacket;
import org.dzh.bytesutil.annotations.modifiers.Length;
import org.dzh.bytesutil.annotations.modifiers.Order;
import org.dzh.bytesutil.annotations.types.CHAR;
import org.dzh.bytesutil.annotations.types.RAW;
import org.dzh.bytesutil.converters.ShortConverter;
import org.junit.Assert;
import org.junit.Test;

import test.TestUtils;

public class TestCaseShortConverter {
	public static class Entity0 extends DataPacket{
		@Order(0)
		@CHAR
		@Length
		public short b;
	}
	@Test
	public void test0() throws ConversionException {
		Entity0 entity = new Entity0();
		try {
			entity.b = -3;
			entity.serialize(TestUtils.newByteArrayOutputStream());
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, ShortConverter.class, 0);
			return;
		}
	}
	public static class Entity1 extends DataPacket{
		@Order(0)
		@CHAR(2)
		public short b;
	}
	@Test
	public void test1() throws ConversionException {
		Entity1 entity = new Entity1();
		try {
			entity.b = 120;
			entity.serialize(TestUtils.newByteArrayOutputStream());
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, ShortConverter.class, 1);
			return;
		}
	}
	public static class Entity2 extends DataPacket{
		@Order(0)
		@CHAR(2)
		public short b;
	}
	@Test
	public void test2() throws ConversionException {
		Entity2 entity = new Entity2();
		try {
			entity.deserialize(TestUtils.newInputStream(new byte[] {(byte)'0',(byte)'9'}));
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, ShortConverter.class, 2);
		}
		try {
			entity.deserialize(TestUtils.newInputStream(new byte[] {(byte)'1',(byte)'/'}));
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, ShortConverter.class, 2);
		}
		try {
			entity.deserialize(TestUtils.newInputStream(new byte[] {(byte)'1',(byte)';'}));
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, ShortConverter.class, 2);
		}
	}
	
	public static class Entity3 extends DataPacket{
		@Order(0)
		@RAW(2)
		public short b;
	}
	@Test
	public void test3() throws ConversionException {
		Entity3 entity = new Entity3();
		try {
			entity.serialize(TestUtils.newByteArrayOutputStream());
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, DataPacket.class, 8);
		}
		try {
			entity.deserialize(TestUtils.newZeroLengthInputStream());
			Assert.fail();
		} catch (Exception e) {
			TestUtils.assertExactException(e, DataPacket.class, 18);
		}
	}
}