package com.example.groovylib;

import org.apache.tools.ant.taskdefs.Manifest;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyTestClass1 implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getChildProjects();
//        Manifest.getDefaultManifest()
//        project.getTasks().findByPath().doFirst()
//        project.getTasks().findByPath().dependsOn()
//        project.getTasks().findByPath().doLast()
//        project.getTasks().findByPath().()
    }

}
