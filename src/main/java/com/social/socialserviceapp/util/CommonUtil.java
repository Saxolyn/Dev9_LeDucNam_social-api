package com.social.socialserviceapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CommonUtil {

    public static String convertDateToString(Date date){
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException{
        if (object == null) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        }
    }

    public static String toString(List<String> lstObject, String separator) throws Exception{
        try {
            if (lstObject != null && !lstObject.isEmpty()) {
                StringBuilder result = new StringBuilder("");
                int size = lstObject.size();
                result.append(lstObject.get(0));
                for (int i = 1; i < size; i++) {
                    result.append(separator);
                    result.append(lstObject.get(i));
                }
                return result.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static List<String> toList(String sourceString, String pattern){
        List<String> results = new LinkedList<>();
        if ("".equals(NVL(sourceString).trim())) {
            return results;
        }
        String[] sources = NVL(sourceString).split(pattern);
        for (String source : sources) {
            results.add(NVL(source).trim());
        }
        return results;
    }

    public static boolean isNullOrEmpty(List data){
        return (data == null || data.isEmpty());
    }

    public static boolean isNullOrEmpty(String str){
        return (str == null || str.trim()
                .isEmpty());
    }

    public static String NVL(String value, String nullValue, String notNullValue){
        return value == null ? nullValue : notNullValue;
    }

    public static String NVL(String value, String defaultValue){
        return NVL(value, defaultValue, value);
    }

    public static String NVL(String value){
        return NVL(value, "");
    }

    public static String generateRandomUuid(){
        return UUID.randomUUID()
                .toString();
    }

    public static Date convertStringToDate(String date) throws Exception{
        if (isNullOrEmpty(date)) {
            return null;
        } else {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setLenient(false);
            return dateFormat.parse(date);
        }
    }

}
