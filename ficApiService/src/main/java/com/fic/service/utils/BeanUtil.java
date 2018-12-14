package com.fic.service.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public class BeanUtil {

    public static void copy(Object destObj,Object source) throws InvocationTargetException, IllegalAccessException {
        BigDecimalConverter bd = new BigDecimalConverter(BigDecimal.ZERO);
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"});
        ConvertUtils.register(new DateConverter(null),Date.class);
        ConvertUtils.register(bd, java.math.BigDecimal.class);
//        ConvertUtils.register(dateConverter, Date.class);
        BeanUtils.copyProperties(destObj,source);
    }
}
