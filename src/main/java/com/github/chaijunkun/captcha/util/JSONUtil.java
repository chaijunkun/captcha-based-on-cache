package com.github.chaijunkun.captcha.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.IOUtils;

/**
 * JSON序列化与反序列化组件
 * @author chaijunkun
 */
public enum JSONUtil {

    /**
     * 单例实例
     */
    instance;

    private ObjectMapper objectMapper;

    /**
     * 懒惰单例模式得到ObjectMapper实例
     * 此对象为Jackson的核心
     */
    JSONUtil() {
        this.objectMapper = new ObjectMapper();
        //当找不到对应的序列化器时 忽略此字段
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //支持双引号
        this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //禁止一个Map中value为null时,对应key参与序列化
        this.objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //未知字段在反序列化时忽略
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //使Jackson JSON支持Unicode编码非ASCII字符
        this.objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        //设置全局日期格式
        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //设置null值参与序列化(字段显示为空数组或空对象)
        this.objectMapper.setSerializationInclusion(Include.NON_EMPTY);
    }

    /**
     * 获取单例ObjectMapper
     *
     * @return 单例ObjectMapper
     */
    private ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    /**
     * 创建JSON字符串处理器的静态方法
     *
     * @param content JSON字符串
     * @return
     */
    private JsonParser getParser(String content) {
        try {
            return getObjectMapper().getFactory().createParser(content);
        } catch (IOException ioe) {
            return null;
        }
    }

    /**
     * 创建JSON流式处理器的静态方法
     *
     * @param in JSON输入流
     * @return
     */
    private JsonParser getParser(InputStream in) {
        try {
            return getObjectMapper().getFactory().createParser(in);
        } catch (IOException ioe) {
            return null;
        }
    }

    /**
     * 创建JSON生成器的静态方法, 使用标准输出
     *
     * @param sw 用于JSON输出的书写器
     * @return
     */
    private JsonGenerator getGenerator(StringWriter sw) {
        try {
            return getObjectMapper().getFactory().createGenerator(sw);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * JSON对象序列化
     *
     * @param obj 待序列化的对象
     * @return JSON字符串
     * @throws JSONSerException
     */
    public static String toJSON(Object obj) throws JSONSerException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGen = JSONUtil.instance.getGenerator(sw);
        if (jsonGen == null) {
            IOUtils.closeQuietly(sw);
            return null;
        }
        try {
            //由于在getGenerator方法中指定了OutputStream为sw
            //因此调用writeObject会将数据输出到sw
            jsonGen.writeObject(obj);
            //由于采用流式输出 在输出完毕后务必清空缓冲区并关闭输出流
            jsonGen.flush();
            jsonGen.close();
            return sw.toString();
        } catch (JsonGenerationException e) {
            throw new JSONSerException(e);
        } catch (IOException e) {
            throw new JSONSerException(e);
        }
    }

    /**
     * 生成JSONP
     * @param callBack 回调函数名
     * @param obj 要序列化的数据对象
     * @return JSONP
     */
    public static String toJSONP(String callBack, Object obj) throws JSONSerException {
        String json= JSONUtil.toJSON(obj);
        String retVal= String.format("%s(%s)", callBack, json);
        return retVal;
    }

    /**
     * JSON对象反序列化
     *
     * @param json  JSON字符串
     * @param clazz 目标对象类型(仅适用于简单对象,即非泛型对象)
     * @return 目标对象
     * @throws JSONDeserException
     */
    public static <T> T fromJSON(String json, Class<T> clazz) throws JSONDeserException {
        try {
            JsonParser jp = JSONUtil.instance.getParser(json);
            return jp.readValueAs(clazz);
        } catch (JsonParseException e) {
            throw new JSONDeserException(e);
        } catch (JsonMappingException e) {
            throw new JSONDeserException(e);
        } catch (IOException e) {
            throw new JSONDeserException(e);
        }
    }

    /**
     * JSON对象反序列化
     *
     * @param in    JSON输入流
     * @param clazz 目标对象类型(仅适用于简单对象,即非泛型对象)
     * @return 目标对象
     * @throws JSONDeserException
     */
    public static <T> T fromJSON(InputStream in, Class<T> clazz) throws JSONDeserException {
        try {
            JsonParser jp = JSONUtil.instance.getParser(in);
            return jp.readValueAs(clazz);
        } catch (JsonParseException e) {
            throw new JSONDeserException(e);
        } catch (JsonMappingException e) {
            throw new JSONDeserException(e);
        } catch (IOException e) {
            throw new JSONDeserException(e);
        }
    }

    /**
     * JSON对象反序列化
     *
     * @param json         JSON字符串
     * @param valueTypeRef 目标对象类型(适用于泛型对象,例如 new TypeReference<List<User>>(){})
     * @return 目标对象
     * @throws JSONDeserException
     */
    public static <T> T fromJSON(String json, TypeReference<T> valueTypeRef) throws JSONDeserException {
        try {
            JsonParser jp = JSONUtil.instance.getParser(json);
            return jp.readValueAs(valueTypeRef);
        } catch (JsonParseException e) {
            throw new JSONDeserException(e);
        } catch (JsonMappingException e) {
            throw new JSONDeserException(e);
        } catch (IOException e) {
            throw new JSONDeserException(e);
        }
    }

    /**
     * JSON对象反序列化
     *
     * @param in           JSON输入流
     * @param valueTypeRef 目标对象类型(适用于泛型对象,例如 new TypeReference<List<User>>(){})
     * @return 目标对象
     * @throws JSONDeserException
     */
    public static <T> T fromJSON(InputStream in, TypeReference<T> valueTypeRef) throws JSONDeserException {
        try {
            JsonParser jp = JSONUtil.instance.getParser(in);
            return jp.readValueAs(valueTypeRef);
        } catch (JsonParseException e) {
            throw new JSONDeserException(e);
        } catch (JsonMappingException e) {
            throw new JSONDeserException(e);
        } catch (IOException e) {
            throw new JSONDeserException(e);
        }
    }

    /**
     * 将bean对象转化为指定类型对象
     *
     * @param bean 待转化bean
     * @return 指定类型对象
     * @throws IllegalArgumentException 转换失败时抛出的异常
     */
    public static <T> T convertValue(Object bean, Class<T> clazz) throws IllegalArgumentException {
        return JSONUtil.instance.getObjectMapper().convertValue(bean, clazz);
    }

}
