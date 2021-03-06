package io.github.zhtmf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.zhtmf.converters.TestUtils;

public class TestCase1 {
    
    private MyEntity entity = new MyEntity();
    
    @Before
    public void setValues() {
        entity.a = 120;
        entity.b = 110;
        entity.c = -1000;
        entity.d = 255;
        entity.e = 65535;
        entity.f = Integer.MIN_VALUE;
        entity.z = -100;
        entity.str = "abcdef";
        entity.str2 = "啊啊啊";
        entity.bcd = "20180909";
        entity.status = 'Y';
        entity.status2 = 'N';
        entity.sub = new MyEntity.SubEntity(30, "0123456789abcde");
        entity.strList = Arrays.asList("1234","2456","haha");
        entity.list3 = Arrays.asList("1234","abcd","defg","hijk","lmno");
        entity.subEntityList = Arrays.asList(new MyEntity.SubEntity(-3142, "0123456789abcde"),new MyEntity.SubEntity(5000,"0123456789fffff"));
        entity.unusedLength = 0;
        entity.entityList2 = new ArrayList<MyEntity.SubEntity>();
        entity.bytes = new byte[] {0x1,0x2};
        entity.byteList = Arrays.asList(new byte[] {0x1,0x2,0x5},new byte[]{0x3,0x4,0x6});
        entity.bytes2Len = 5;
        entity.anotherBytes = new byte[] {0x1,0x2,0x5,0x1,0x2};
        entity.date = new Date(0);
        entity.date2 = new Date(0); //milliseconds different?
        entity.veryLong = ((long)Integer.MAX_VALUE)*2;
        MyEntity.Sub2 s2 = new MyEntity.Sub2();
        s2.type = 2;
        s2.time = "19990101";
        s2.str1 = "123456";
        s2.str2 = "A";
        s2.str3 = "MF";
        s2.type2 = 1;
        s2.str4 = "hahahahaha";
        entity.variantEntity = s2;
        MyEntity.Sub1 s1 = new MyEntity.Sub1();
        s1.type = 1;
        s1.time = "20000202";
        s1.field1 = -350;
        s1.field2 = 30000;
        entity.anotherEntity = s1;
        
        MyEntity.WeirdEntity we = new MyEntity.WeirdEntity();
        we.char1 = "abcdef";
        we.char2 = "hahahahahaha";
        we.char3 = we.char1;
        we.bytearray1 = new byte[] {1,2,3,4,5};
        we.bytearray2 = new byte[] {1};
        we.bytearray3 = new byte[] {11,22,33,44};
        we.bytearray4 = we.bytearray1;
        entity.we = we;
    }
    
    @Test
    public void testPerformance() throws ConversionException, IOException {
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for(int i=0;i<100000;++i) {
                entity.serialize(baos);
                baos.reset();
            }
        }
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            long st = System.currentTimeMillis();
            for(int i=0;i<100000;++i) {
                entity.serialize(baos);
                baos.reset();
            }
            long elapsed = System.currentTimeMillis() - st;
            System.out.println("time elapsed:"+elapsed);
            Assert.assertTrue("time elapsed:"+elapsed, elapsed<4000);
        }
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            entity.serialize(baos);
            byte[] arr = baos.toByteArray();
            InputStream src = TestUtils.newInputStream(arr);
            src.mark(Integer.MAX_VALUE);
            long st = System.currentTimeMillis();
            for(int i=0;i<100000;++i) {
                new MyEntity().deserialize(src);
                src.reset();
            }
            long elapsed = System.currentTimeMillis() - st;
            System.out.println("time elapsed:"+elapsed);
            Assert.assertTrue("time elapsed:"+elapsed, elapsed<4000);
        }
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for(int i=0;i<31;++i) {
                entity.serialize(baos);
            }
            byte[] bts = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(bts);
            for(int i=0;i<31;++i) {
                MyEntity entity2 = new MyEntity();
                entity2.deserialize(bais);
                Assert.assertTrue(TestUtils.equalsOrderFields(entity, entity2));
            }
        }
    }

    @Test
    public void testEntity1() throws ConversionException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        entity.serialize(baos);
        MyEntity entity2 = new MyEntity();
        //for testing of a rare case
        entity2.sub = new MyEntity.SubEntity(3,4.0f);
        final byte[] bts = baos.toByteArray();
        entity2.deserialize(new ByteArrayInputStream(bts));
        Assert.assertTrue(TestUtils.equalsOrderFields(entity,entity2));
        Assert.assertEquals(entity2.subEntityList.get(0).carryOver, entity2.a);
    }
}
