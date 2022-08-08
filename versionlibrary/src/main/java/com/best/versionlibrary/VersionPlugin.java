package com.best.versionlibrary;



import org.gradle.api.Plugin;
import org.gradle.api.Project;


public class VersionPlugin implements Plugin<Project> {
    public static final String material = "androidx.appcompat:appcompat:1.1.0";

    @Override
    public void apply(Project target) {
        System.out.println(target.toString() + " ======" + target.getDepth());
//        target.container()
//        target.getExtensions().add("",);
        for (String name : target.getDefaultTasks()) {
            System.out.println(" name=======" + name);
        }

    }

}
