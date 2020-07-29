package com.example.groovylib;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyTestClass1 implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getChildProjects();
    }

}
