package com.example.groovylib;

import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.util.HashSet;

public class WhyClass implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.err.println("========================");
        System.err.println("hello test plugin" + project.getDisplayName() + "   ===");
        // 获取外部参数
        project.getExtensions().create("MyWHY", PluginExtension.class, project);
        // 创建清理任务
        Task cleanTask = project.getTasks().create(CleanTask.NAME, CleanTask.class);
//        cleanTask.dependsOn()
        System.err.println("========================");
    }

    static  class PluginExtension {
        String lintXmlPath;
        String outputPath;

        public PluginExtension(Project project) {
            // 默认路径
            lintXmlPath = "$project.buildDir/reports/lint-results.xml";
            outputPath = "$project.buildDir/reports/lintCleanerLog.txt";
        }

        public String toString() {
            return "\n\tlintXmlPath:" + lintXmlPath + "\n" +
                    "outputPath:" + outputPath + "\n";
        }
    }

    public static class CleanTask extends DefaultTask {

        static final String NAME = "cleanUnusedRes";
        final String UNUSED_RESOURCES_ID = "UnusedResources";
        final String ISSUE_XML_TAG = "issue";
        HashSet<String> mFilePaths = new HashSet<>();
        StringBuilder mDelLogSb = new StringBuilder();
        StringBuilder mKeepLogSb = new StringBuilder();

        public CleanTask() {
//            group = CleanResPlugin.GROUP;
//            description = "Removes unused resources reported by Android lint task"
        }

        @TaskAction
        public void start() {
//            def ext = project.extensions.findByName(CleanResPlugin.EXTENSION_NAME)  as PluginExtension
//            println ext.toString()
//            def file = new File(ext.lintXmlPath)
//            if (!file.exists()) {
//                println ' '
//                return
//            }
//
//            // 解析xml，添加无用文件的路径到容器中
//            new XmlSlurper().parse(file). '**'.findAll {
//                node ->
//                if (node.name() == ISSUE_XML_TAG && node. @id ==UNUSED_RESOURCES_ID){
//                    mFilePaths.add(node.location. @file)
//                }
//            }
//
//            def num = mFilePaths.size()
//            if (num > 0) {
//                mDelLogSb.append("num:${num}\n")
//                mDelLogSb.append("\n==========\n")
//                mKeepLogSb.append("\n==========\n")
//                for (String path : mFilePaths) {
//                    println path
//                    deleteFileByPath(path)
//                }
//                writeToOutput(ext.outputPath)
//            } else {
//                println ''
//            }
        }

//        def deleteFileByPath(String path) {
//            if (isDelFile(path)) {
//                if (new File(path).delete()) {
//                    mDelLogSb.append('\n\t' + path)
//
//                } else {
//                    mKeepLogSb.append('\n\t' + path)
//
//                }
//            } else {
//                mKeepLogSb.append('\n\t' + path)
//
//            }
//        }
//
//        /**
//         *
//         * @param path
//         */
//        def isDelFile(String path) {
//            String dir = path
//                    (dir.contains('layout') || dir.contains('drawable') || dir.contains('mipmap') || dir.contains('menu')) && (dir.endsWith('.png') || dir.endsWith('.jpg') || dir.endsWith('.jpeg'))
//        }
//
//        def writeToOutput(def path) {
//            def f = new File(path)
//            if (f.exists()) {
//                f.delete()
//            }
//            new File(path).withPrintWriter {
//                pw ->
//                        pw.write(mDelLogSb.toString())
//                pw.write(mKeepLogSb.toString())
//            }
//        }
    }

}
