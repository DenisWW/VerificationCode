package com.example.groovylib;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import groovy.util.Node;
import groovy.util.XmlParser;

public class MyClass extends DefaultTask {
    @Override
    public void onlyIf(Spec<? super Task> spec) {
        super.onlyIf(spec);
        try {
            XmlParser xmlParser=new XmlParser();
            Node node= xmlParser.parse("");
//            node.attribute()
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
