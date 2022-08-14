//package org.example.config.redis;
//
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.BeanDescription;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializationConfig;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
//import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
//import com.fasterxml.jackson.databind.ser.std.DateSerializer;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.TimeZone;
//
///**
// * @ClassName: JacksonDateSerializerModifier
// * @Author: MaCongYi
// * @create: 2022-08-14 11:32
// * @Description:
// * @Version: 1.0
// */
//
//public class JacksonDateSerializerModifier extends BeanSerializerModifier {
//    @Override
//    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
//        for (Object beanProperty : beanProperties) {
//            BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
//            Class<?> clazz = writer.getType().getRawClass();
//            if (clazz.equals(Date.class)) {
//                writer.assignSerializer(new DateSerializer());
//            }
//        }
//        return beanProperties;
//    }
//    public class DateSerializer extends JsonSerializer<Object> {
//        @Override
//        public void serialize(Object date, JsonGenerator jsonGenerator,
//                              SerializerProvider serializerProvider) throws IOException {
//            if (date == null) {
//                jsonGenerator.writeNumber(StringUtils.EMPTY);
//            }
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
//            format.setTimeZone(timeZone);
//            String dateString = "\"" + format.format(date) + "\"";
//            jsonGenerator.writeNumber(dateString);
//        }
//    }
//}
