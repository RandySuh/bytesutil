package test.general;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.dzh.bytesutil.DataPacket;
import org.dzh.bytesutil.annotations.enums.NumericEnum;
import org.dzh.bytesutil.annotations.modifiers.BigEndian;
import org.dzh.bytesutil.annotations.modifiers.CHARSET;
import org.dzh.bytesutil.annotations.modifiers.DatePattern;
import org.dzh.bytesutil.annotations.modifiers.Length;
import org.dzh.bytesutil.annotations.modifiers.ListLength;
import org.dzh.bytesutil.annotations.modifiers.LittleEndian;
import org.dzh.bytesutil.annotations.modifiers.Order;
import org.dzh.bytesutil.annotations.modifiers.Signed;
import org.dzh.bytesutil.annotations.modifiers.Unsigned;
import org.dzh.bytesutil.annotations.types.BCD;
import org.dzh.bytesutil.annotations.types.BYTE;
import org.dzh.bytesutil.annotations.types.CHAR;
import org.dzh.bytesutil.annotations.types.INT;
import org.dzh.bytesutil.annotations.types.LONG;
import org.dzh.bytesutil.annotations.types.RAW;
import org.dzh.bytesutil.annotations.types.SHORT;
import org.junit.Assert;
import org.junit.Test;

import test.TestUtils;

public class TestCase91{
	
	private enum NEnum1 implements NumericEnum{
		FLAG1 {
			@Override
			public long getValue() {
				return 1;
			}
		},
		FLAG2 {
			@Override
			public long getValue() {
				return 2;
			}
		};
		@Override
		public abstract long getValue();
	}
	
	@Signed
	@BigEndian
	@CHARSET("UTF-8")
	public static final class Entity extends DataPacket{
		@Order(-2)
		@INT
		public int i = 5;
		@Order(-1)
		@CHAR
		@Length
		public String str = "ddd";
		@Order(1)
		@CHAR(9)
		public String str2 = "呵呵呵";
		@Order(2)
		@CHAR
		@Length
		@ListLength(3)
		public List<String> strList1 = Arrays.asList("ttt","qwert","哈哈哈");
		@Order(3)
		@RAW
		@Length
		@ListLength(2)
		public List<byte[]> byteArrayList = Arrays.asList(new byte[] {1,2,3},new byte[] {0,1,2});;
		@Order(6)
		@RAW
		@Length
		public byte[] tail2 = new byte[] {1,2,3};
		@Order(7)
		@CHAR
		@Length
		@ListLength(2)
		public List<Byte> bytes = Arrays.asList(Byte.valueOf((byte)13),Byte.valueOf((byte)14));
		@Order(8)
		@BCD(2)
		public byte bcd = 100;
		@Order(9)
		@BYTE
		@Signed
		public byte byteSigned = 120;
		@Order(10)
		@BYTE
		@Unsigned
		public byte byteUnsigned = 119;
		@Order(11)
		@CHAR
		@Length
		public short short1;
		@Order(12)
		@CHAR
		@Length
		public Short short2 = 32344;
		@Order(13)
		@BCD(2)
		public short short3 = 1919;
		@Order(14)
		@BYTE
		@Signed
		public short short4;
		@Order(15)
		@BYTE
		@Unsigned
		public short short5;
		@Order(16)
		@BYTE
		@Signed
		public long long1 = 120L;
		@Order(17)
		@BYTE
		@Unsigned
		public Long long2 = 240L;
		@Order(18)
		@SHORT
		@Signed
		public long long3 = Short.MAX_VALUE;
		@Order(19)
		@SHORT
		@Unsigned
		public Long long4 = (long) (((int)Short.MAX_VALUE)*2);
		@Order(20)
		@INT
		@Signed
		public long long5 = Integer.MAX_VALUE;
		@Order(21)
		@INT
		@Unsigned
		public Long long6 = ((long)Integer.MAX_VALUE)*2;
		@Order(22)
		@RAW(2)
		@Signed
		public byte[] byteArray1 = new byte[] {-3,120};
		@Order(23)
		@RAW
		@Length
		@Unsigned
		public byte[] byteArray2 = new byte[] {(byte) 250,(byte) 230};
		@Order(24)
		@RAW(2)
		@Signed
		public int[] intArray1 = new int[] {-3,127};
		@Order(25)
		@RAW
		@Length
		@Unsigned
		public int[] intArray2 = new int[] {255,255};
		@Order(26)
		@CHAR
		@Length
		@DatePattern("yyyyMMdd")
		public Date date = new GregorianCalendar(1997, 11, 17).getTime();
		
		@Order(27)
		@CHAR
		@Length
		@CHARSET("GBK")
		public String strGBK = "哦哦哦";
		
		@Order(28)
		@CHAR
		@Length
		@CHARSET("SHIFT-JIS")
		public String strSHIFTJIS = "マイン";
		
		@Order(29)
		@CHAR(2)
		@CHARSET("GBK")
		public Character ch1 = '我';
		
		@Order(30)
		@CHAR
		@Length
		@CHARSET("SHIFT-JIS")
		public Character ch2 = 'マ';
		
		@Order(31)
		@BYTE
		@Unsigned
		public byte bbb = -100;
		
		@Order(32)
		@BYTE
		@Unsigned
		public Short s1 = 250;
		
		@Order(33)
		@BYTE
		@Signed
		public NEnum1 n1 = NEnum1.FLAG1;
		
		@Order(34)
		@SHORT
		@Unsigned
		public short s2 = (short) 65535;
		
		@Order(35)
		@SHORT
		@Signed
		public Short s3 = Short.MAX_VALUE;
		
		@Order(36)
		@SHORT
		@Unsigned
		public Long l1 = 65535L;
		
		@Order(37)
		@SHORT
		@Signed
		public NEnum1 n2 = NEnum1.FLAG1;
		
		@Order(38)
		@BCD(1)
		@Signed
		public Byte bcd1 = 30;
		
		@Order(39)
		@BCD(2)
		@Signed
		public Short bcd2 = 3011;
		
		@Order(40)
		@BCD(3)
		@Signed
		public Long bcd3 = 123456L;
		
		@Order(41)
		@CHAR
		@Length
		public Long ch3 = 123456L;
		
		@Order(42)
		@CHAR
		@Length
		public Integer ch4 = 123456;
		
		@Order(31)
		@INT
		public final int ignored1 = 1;
		@Order(32)
		@INT
		public static int ignored2;
		
		@Order(43)
		@Signed
		@LittleEndian
		@LONG
		public long longa = Long.MIN_VALUE;
		
		@Order(44)
		@Signed
		@BigEndian
		@LONG
		public long longb = Long.MAX_VALUE;
		
		@Order(45)
		@Unsigned
		@BigEndian
		@LONG
		public long longa1 = 0;
		
		@Order(46)
		@Unsigned
		@BigEndian
		@LONG
		public long longa2 = Long.MIN_VALUE;
		
		@Order(47)
		@Unsigned
		@BigEndian
		@LONG
		public BigInteger longc = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
		
		@Order(48)
		@Signed
		@BigEndian
		@LONG
		public BigInteger longd1 = BigInteger.valueOf(Long.MIN_VALUE);
		
		@Order(49)
		@Signed
		@BigEndian
		@LONG
		public BigInteger longd2 = BigInteger.valueOf(Long.MAX_VALUE);
		
		@Order(50)
		@Unsigned
		@BigEndian
		@LONG
		public BigInteger longc1 = BigInteger.valueOf(0);
		
		@Order(51)
		@LittleEndian
		@INT
		public Date datea = new Date(System.currentTimeMillis()/1000*1000);
		
		@Order(52)
		@BigEndian
		@LONG
		public Date dateb = new Date(1293894059333L);
		
		@Order(53)
		@CHAR(27)
		public BigInteger bic1 = new BigInteger("111222333444555666777888999");
		
		@Order(54)
		@CHAR(9)
		public BigInteger bic2 = new BigInteger("111222333");
		
		@Order(55)
		@LONG
		@Signed
		public NEnum1 n3 = NEnum1.FLAG1;
		
		@Order(56)
		@Unsigned
		@BigEndian
		@LONG
		public Long longa3 = Long.MIN_VALUE;
		
		@Order(57)
		@BYTE
		public boolean bool1 = false;
		@Order(58)
		@BYTE
		public boolean bool2 = true;
		@Order(59)
		@BYTE
		public Boolean bool3 = Boolean.TRUE;
		@Order(60)
		@BYTE
		public Boolean bool4 = Boolean.FALSE;
	}
	
	@Test
	public void test() throws Exception {
		final Entity entity = new Entity();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		entity.serialize(baos);
		Assert.assertEquals(baos.size(), entity.length());
		Entity restored = new Entity();
		final byte[] arr1 = baos.toByteArray();
		restored.deserialize(new ByteArrayInputStream(arr1));
		Assert.assertTrue(TestUtils.equalsOrderFields(entity, restored));
		baos.reset();
		restored.serialize(baos);
		Assert.assertArrayEquals(arr1, baos.toByteArray());
		
		TestUtils.serializeMultipleTimesAndRestore(entity);
	}
	
	@Test
	public void testConcurrency() throws Exception {
		TestUtils.serializeMultipleTimesAndRestoreConcurrently(new Entity(), 5000);
	}
}
