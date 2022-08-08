package com.nineone.verificationcode.view;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DisplayShapeRenderer implements GLSurfaceView.Renderer {


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
    }

    public class Triangle {

        private FloatBuffer vertexBuffer;

        // number of coordinates per vertex in this array
        final int COORDS_PER_VERTEX = 3;
        float triangleCoords[] = {   // in counterclockwise order:
                0.0f, 0.5f, 0.0f, // top
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f  // bottom right
        };

        // Set color with red, green, blue and alpha (opacity) values
        float color[] = {255, 0, 0, 1.0f};

        public Triangle() {
            // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
            ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
            // 数组排列用nativeOrder
            bb.order(ByteOrder.nativeOrder());
            // 从ByteBuffer创建一个浮点缓冲区
            vertexBuffer = bb.asFloatBuffer();
            // 将坐标添加到FloatBuffer
            vertexBuffer.put(triangleCoords);
            // 设置缓冲区来读取第一个坐标
            vertexBuffer.position(0);
        }
    }
}
