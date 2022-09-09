package com.example.groovylib

import org.apache.tools.ant.taskdefs.Transform
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyTestClass : Plugin<Project> {
    override fun apply(p0: Project) {
        println("========================")
        println("hello test plugin"+p0.displayName+"   ===")
        println("========================")
//        Transform
    }

}