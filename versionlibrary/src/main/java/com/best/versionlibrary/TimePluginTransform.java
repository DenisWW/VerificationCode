package com.best.versionlibrary;


import org.gradle.api.file.RelativePath;
import org.gradle.internal.Pair;
import org.gradle.internal.classpath.CachedClasspathTransformer;
import org.gradle.internal.classpath.ClasspathEntryVisitor;
import org.gradle.internal.hash.Hasher;
import org.gradle.internal.impldep.org.objectweb.asm.ClassVisitor;

import java.io.IOException;

public class TimePluginTransform implements CachedClasspathTransformer.Transform {
    @Override
    public void applyConfigurationTo(Hasher hasher) {

    }

    @Override
    public Pair<RelativePath, ClassVisitor> apply(ClasspathEntryVisitor.Entry entry, ClassVisitor classVisitor) throws IOException {
        return null;
    }

}
