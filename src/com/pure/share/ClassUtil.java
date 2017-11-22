package com.pure.share;

public class ClassUtil {

    public static String paresFiledClass(String name){
        switch (name){
            case "int": return "Integer.class";
            case "long" : return "Long.class";
            case "float" : return "Float.class";
            case "boolean" : return "Boolean.class";
            case "String" : return "String.class";
            default: return "Object.class";
        }
    }
}
