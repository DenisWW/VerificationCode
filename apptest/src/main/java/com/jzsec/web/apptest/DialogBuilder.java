package com.jzsec.web.apptest;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.annotation.StyleRes;

public class DialogBuilder {


    public static class DialogStyleBuilder {

        @StyleRes
        private Integer style;
        @DrawableRes
        @ColorRes
        @RawRes
        private Integer dialogBackgroundRes;
        @ColorRes
        private Integer leftTestColor;
        @ColorRes
        private Integer contentTestColor;
        @ColorRes
        private Integer titleTestColor;
        @ColorRes
        private Integer rightTestColor;
        private float leftTestSize;
        private float rightTestSize;
        private float titleTestSize;
        private float contentTestSize;
        private String title;
        private String content;
        private String leftTest;
        private String rightTest;
        private Context context;


        public DialogStyleBuilder(Context context) {
            this.context = context;
        }


    }

}
