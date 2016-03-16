package org.schedoscope.export.kafka.avro;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hive.hcatalog.data.schema.HCatFieldSchema;
import org.apache.hive.hcatalog.data.schema.HCatSchema;
import org.junit.Before;
import org.junit.Test;

public class HCatToAvroSchemaConverterTest { //extends HiveUnitBaseTest {

    PrimitiveTypeInfo stringType = new PrimitiveTypeInfo();
    PrimitiveTypeInfo longType = new PrimitiveTypeInfo();
    PrimitiveTypeInfo intType = new PrimitiveTypeInfo();

    HCatSchema subSchemaMulti;
    HCatSchema subSchemaSingle;

    @Before
    public void setUp() throws Exception {

        stringType.setTypeName("string");
        longType.setTypeName("bigint");
        intType.setTypeName("int");

        // create nested sub schema
        List<HCatFieldSchema> fields = new ArrayList<HCatFieldSchema>();
        HCatFieldSchema field1 = new HCatFieldSchema("nested_field1", longType, "comment");
        HCatFieldSchema field2 = new HCatFieldSchema("nested_field2", stringType, "comment");
        HCatFieldSchema field3 = new HCatFieldSchema("nested_field3", intType, "comment");
        fields.add(field1);
        subSchemaSingle = new HCatSchema(fields);
        fields.add(field2);
        fields.add(field3);
        subSchemaMulti = new HCatSchema(fields);
    }

    @Test
    public void testStructSchemaConversion() throws IOException {

        List<HCatFieldSchema> fields = new ArrayList<HCatFieldSchema>();
        HCatFieldSchema field1 = new HCatFieldSchema("field1", longType, "comment");
        HCatFieldSchema field2 = new HCatFieldSchema("field2", stringType, "comment");
        HCatFieldSchema field3 = new HCatFieldSchema("field3", intType, "comment");
        HCatFieldSchema field4 = new HCatFieldSchema("field4", HCatFieldSchema.Type.STRUCT, subSchemaMulti, "comment");
        fields.add(field1);
        fields.add(field2);
        fields.add(field3);
        fields.add(field4);

        HCatSchema schemaComplete = new HCatSchema(fields);
        Schema avroSchema = HCatToAvroSchemaConverter.convert(schemaComplete, "my_table");

        assertEquals(4, avroSchema.getFields().size());
        assertEquals(schemaComplete.getSchemaAsTypeString(), avroSchema.getDoc());

        System.err.println(schemaComplete.getSchemaAsTypeString());
        System.err.println(avroSchema);
    }

    @Test
    public void testArraySchemaConversion() throws IOException {

        List<HCatFieldSchema> fields = new ArrayList<HCatFieldSchema>();
        HCatFieldSchema field1 = new HCatFieldSchema("field1", longType, "comment");
        HCatFieldSchema field2 = new HCatFieldSchema("field2", stringType, "comment");
        HCatFieldSchema field3 = new HCatFieldSchema("field3", intType, "comment");
        HCatFieldSchema field4 = new HCatFieldSchema("field4", HCatFieldSchema.Type.ARRAY, subSchemaSingle, "comment");
        fields.add(field1);
        fields.add(field2);
        fields.add(field3);
        fields.add(field4);

        HCatSchema schemaComplete = new HCatSchema(fields);
        Schema avroSchema = HCatToAvroSchemaConverter.convert(schemaComplete, "my_table");

        assertEquals(4, avroSchema.getFields().size());
        assertEquals(schemaComplete.getSchemaAsTypeString(), avroSchema.getDoc());

        System.err.println(schemaComplete.getSchemaAsTypeString());
        System.err.println(avroSchema);
    }

    @Test
    public void testMapSchemaConversion() throws IOException {

        List<HCatFieldSchema> fields = new ArrayList<HCatFieldSchema>();
        HCatFieldSchema field1 = new HCatFieldSchema("field1", longType, "comment");
        HCatFieldSchema field2 = new HCatFieldSchema("field2", stringType, "comment");
        HCatFieldSchema field3 = new HCatFieldSchema("field3", intType, "comment");
        HCatFieldSchema field4 = HCatFieldSchema.createMapTypeFieldSchema("field4", stringType, subSchemaSingle, "comment");
        fields.add(field1);
        fields.add(field2);
        fields.add(field3);
        fields.add(field4);

        HCatSchema schemaComplete = new HCatSchema(fields);
        Schema avroSchema = HCatToAvroSchemaConverter.convert(schemaComplete, "my_table");

        assertEquals(4, avroSchema.getFields().size());
        assertEquals(schemaComplete.getSchemaAsTypeString(), avroSchema.getDoc());
    }

    @Test
    public void testStuctStructSchemaConversion() throws IOException {


        List<HCatFieldSchema> fields = new ArrayList<HCatFieldSchema>();
        HCatFieldSchema field1 = new HCatFieldSchema("field1", longType, "comment");
        HCatFieldSchema field2 = new HCatFieldSchema("field2", stringType, "comment");
        HCatFieldSchema field3 = new HCatFieldSchema("field3", intType, "comment");
        HCatFieldSchema field4 = new HCatFieldSchema("field4", HCatFieldSchema.Type.STRUCT, subSchemaMulti, "comment");
        fields.add(field1);
        fields.add(field2);
        fields.add(field3);
        fields.add(field4);

        HCatSchema subStructSchema = new HCatSchema(fields);

        HCatFieldSchema field5 = new HCatFieldSchema("field5", HCatFieldSchema.Type.STRUCT, subStructSchema, "comment");
        fields.add(field5);

        HCatSchema schemaComplete = new HCatSchema(fields);

        Schema avroSchema = HCatToAvroSchemaConverter.convert(schemaComplete, "my_table");

        assertEquals(5, avroSchema.getFields().size());
        assertEquals(schemaComplete.getSchemaAsTypeString(), avroSchema.getDoc());
    }
}
