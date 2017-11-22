package com.pure.share;


import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    private final PsiClass mClass;
    private final List<PsiField> mFields;

    public CodeGenerator(PsiClass psiClass, List<PsiField> fields) {
        mClass = psiClass;
        mFields = fields;
    }

    public void generate() {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(mClass.getProject());
        List<PsiMethod> methods = new ArrayList<>();
        PsiMethod[] psiMethods = mClass.getAllMethods();
        boolean isAddSetParameter = true;
        boolean isAddGetParameter = true;
        for (PsiField field : mFields) {
            boolean isAddGet = true;
            boolean isAddSet = true;
            for(PsiMethod method : psiMethods){
                if(method.getName().equals(generateGetterMethodName(field.getName()))){
                    isAddGet = false;
                }
                if(method.getName().equals(generateSetterMethodName(field.getName()))){
                    isAddSet = false;
                }
                if(method.getName().equals("setParameter")){
                    isAddSetParameter = false;
                }
                if(method.getName().equals("getParameter")){
                    isAddGetParameter = false;
                }
            }
            if(isAddGet) {
                PsiMethod getter =
                        elementFactory.createMethodFromText(generateGetterMethod(field), mClass);
                methods.add(getter);
            }
            if(isAddSet) {
                PsiMethod setter =
                        elementFactory.createMethodFromText(generateSetterMethod(field), mClass);
                methods.add(setter);
            }
        }
        if(isAddGetParameter){
            PsiMethod getter =
                    elementFactory.createMethodFromText("public <T> T getParameter(String key, Object defValue,Class<T> classOfT){ return null;}", mClass);
            methods.add(getter);
        }
        if(isAddSetParameter){
            PsiMethod setter =
                    elementFactory.createMethodFromText("public void setParameter(String key, Object value){ }", mClass);
            methods.add(setter);
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mClass.getProject());
        for (PsiMethod method : methods) {
            styleManager.shortenClassReferences(mClass.add(method));
        }
    }

    private String generateGetterMethod(PsiField field) {
        StringBuilder sb = new StringBuilder("public ");
        String present = field.getType().getPresentableText();
        sb.append(present);
        sb.append(" ").append(generateGetterMethodName(field.getName()))
                .append("() { return ")
                .append("getParameter(\"").append(field.getName())
                .append("\",").append(field.getName()).append(",")
                .append(ClassUtil.paresFiledClass(present)).append("); }");
        return sb.toString();
    }

    private String generateGetterMethodName(String name){
        StringBuilder sb = new StringBuilder(name);
        // verify that the first character is an 'm' or an 's' and the second is uppercase
        if ((sb.charAt(0) == 'm' || sb.charAt(0) == 's') && sb.charAt(1) < 97) {
            sb.deleteCharAt(0);
        }
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "get");
        return sb.toString();
    }

    private String generateSetterMethod(PsiField field) {
        StringBuilder sb = new StringBuilder("public void ");
        String methodName = generateSetterMethodName(field.getName());
        StringBuilder param = new StringBuilder(methodName.substring(3));
        param.setCharAt(0, Character.toLowerCase(param.charAt(0)));
        sb.append(methodName).append("(").append(field.getType().getPresentableText()).append(" ")
                .append(param).append(") { ");
        sb.append("setParameter(\"").append(field.getName()).append("\", ").append(param).append("); }");
        return sb.toString();
    }
    private String generateSetterMethodName(String name){
        StringBuilder sb = new StringBuilder(name);
        // verify that the first character is an 'm' or an 's' and the second is uppercase
        if ((sb.charAt(0) == 'm' || sb.charAt(0) == 's') && sb.charAt(1) < 97) {
            sb.deleteCharAt(0);
        }
        sb.insert(0, "set");
        sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
        return sb.toString();
    }
}