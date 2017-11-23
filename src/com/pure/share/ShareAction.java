package com.pure.share;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShareAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        PsiHolder holder = getPsiClassFromEvent(anActionEvent);
        List<PsiField> fields = Arrays.asList(holder.psiClass.getFields());
        generateAccessors(holder, filterField(fields));
    }
    private void generateAccessors(final PsiHolder holder, final List<PsiField> fields) {
        new WriteCommandAction.Simple(holder.psiClass.getProject(),holder.psiClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
                new CodeGenerator(holder, fields).generate();
            }
        }.execute();
    }
    /**
     * 过滤非基本类型
     * @param fields
     * @return
     */
    private List<PsiField> filterField(List<PsiField> fields){
        List<PsiField> fieldList = new ArrayList<>();
        for(int i=0;i<fields.size();i++){
            String presentable = fields.get(i).getType().getPresentableText();
            if(presentable.equals("int")||
                    presentable.equals("long")||
                    presentable.equals("String")||
                    presentable.equals("boolean")||
                    presentable.equals("float")){
                fieldList.add(fields.get(i));
            }
        }
        return fieldList;
    }

    private PsiHolder getPsiClassFromEvent(AnActionEvent event) {
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        if (psiFile == null || editor == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiHolder holder = new PsiHolder();
        holder.element = psiFile.findElementAt(offset);
        holder.psiClass =  PsiTreeUtil.getParentOfType(holder.element, PsiClass.class);
        return holder;
    }
}
