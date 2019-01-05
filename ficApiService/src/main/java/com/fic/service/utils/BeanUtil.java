package com.fic.service.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public class BeanUtil {

    public static void copy(Object destObj,Object source){

        try{
            BigDecimalConverter bd = new BigDecimalConverter(BigDecimal.ZERO);
            DateConverter dateConverter = new DateConverter();
            dateConverter.setPatterns(new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"});
            ConvertUtils.register(new DateConverter(null),Date.class);
//            ConvertUtils.register(bd, java.math.BigDecimal.class);
            ConvertUtils.register(new BigDecimalConverter(null), java.math.BigDecimal.class);
            ConvertUtils.register(new LongConverter(null), Long.class);
            ConvertUtils.register(new ShortConverter(null), Short.class);
            ConvertUtils.register(new IntegerConverter(null), Integer.class);
            ConvertUtils.register(new DoubleConverter(null), Double.class);
//        ConvertUtils.register(dateConverter, Date.class);
            BeanUtils.copyProperties(destObj,source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
