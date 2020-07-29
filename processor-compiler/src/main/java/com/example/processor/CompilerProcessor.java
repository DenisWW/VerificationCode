package com.example.processor;

import com.example.annotation.ActAnnotation;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class CompilerProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private HashMap<String, List<Element>> hashSet;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        hashSet = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            for (Element element : roundEnv.getElementsAnnotatedWith(ActAnnotation.class)) {
                System.out.println("Processor======" + mElementUtils.getPackageOf(element).getKind()
                        + "\n" + mElementUtils.getPackageOf(element).getQualifiedName()
                        + "\n" + mElementUtils.getPackageOf(element).getSimpleName()
                );
                String name = mElementUtils.getPackageOf(element).getQualifiedName().toString();
                List<Element> elementList = hashSet.get(name);

                if (elementList == null) {
                    elementList = new ArrayList<>();
                    hashSet.put(name, elementList);
                }
                elementList.add(element);
            }
            for (String key : hashSet.keySet()) {
                List<Element> list = hashSet.get(key);
                generateCode(key + ".ActivityAddUtils", getCodeString(key, list));
            }


            return true;
        }

        return false;
    }

    private String getCodeString(String key, List<Element> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(key).append(";\n\n");
        stringBuilder.append("import com.example.annotation.IAddActivity;\n");
        stringBuilder.append("import java.util.Map;\n");
        stringBuilder.append("public class ").append("ActivityAddUtils").append(" implements IAddActivity{\n")
                .append("@Override\n")
                .append("public void addActivity(Map<String,Class<?>> classMap){\n");
        for (Element element : list) {
//            System.err.println("  element==== " + element.getAnnotation(ActAnnotation.class).name()
//                    + "\n" + element.toString()
//            );
            stringBuilder.append("classMap.put(\"").append(element.toString()).append("\",").append(element.toString()).append(".class);");

        }
        stringBuilder.append("\n}\n")
                .append("}");
        return stringBuilder.toString();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(ActAnnotation.class.getCanonicalName());
        return annotations;
    }

    private void generateCode(String className, String code) {
        try {
            JavaFileObject file = mFiler.createSourceFile(className);
            Writer writer = file.openWriter();
            writer.write(code);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}