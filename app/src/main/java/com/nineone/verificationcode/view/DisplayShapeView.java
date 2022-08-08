package com.nineone.verificationcode.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class DisplayShapeView extends GLSurfaceView {
    public DisplayShapeView(Context context) {
        super(context);
        init();
    }

    public DisplayShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private Renderer renderer;

    private void init() {
        if (renderer == null) {
            setEGLContextClientVersion(3);
            renderer = new DisplayShapeRenderer();
            setRenderer(renderer);
            setRenderMode(RENDERMODE_WHEN_DIRTY);  // 渲染模式设置为 仅在调用 requestRender() 时才渲染，减少不必要的绘制
//            renderer=DisplayShapeView
        }
    }
}
