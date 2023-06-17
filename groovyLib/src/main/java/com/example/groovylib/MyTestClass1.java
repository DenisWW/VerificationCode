package com.example.groovylib;

import org.apache.tools.ant.taskdefs.Manifest;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

public class MyTestClass1 implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getChildProjects();
//        Manifest.getDefaultManifest()
//        project.getTasks().findByPath().doFirst()
//        project.getTasks().findByPath().dependsOn()
//        project.getTasks().findByPath().doLast()
//        project.getTasks().findByPath().()

        String s = "sada";
        char[] chars = s.toCharArray();
        char first = chars[0];
        int length = 1;
        int start = 0;
        StringBuilder stringBuilder = new StringBuilder(first);
        int maxLength = 0;
        HashMap<StringBuilder, Integer> hashMap = new HashMap<>();
        for (int i = 1; i < chars.length; i++) {
            if (first == chars[i]) {
                first = chars[i];
                length = i - start;
                start = i;
                if (maxLength == 0) maxLength = stringBuilder.length();
                else {
                    if (stringBuilder.length() > maxLength)
                        maxLength = stringBuilder.length();
                }
            } else {
                stringBuilder.append(chars[i]);
            }
        }
    }

}
