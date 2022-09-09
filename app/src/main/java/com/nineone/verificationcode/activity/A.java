package com.nineone.verificationcode.activity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class A  extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable @org.jetbrains.annotations.Nullable String[] projection, @Nullable @org.jetbrains.annotations.Nullable String selection, @Nullable @org.jetbrains.annotations.Nullable String[] selectionArgs, @Nullable @org.jetbrains.annotations.Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable @org.jetbrains.annotations.Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable @org.jetbrains.annotations.Nullable String selection, @Nullable @org.jetbrains.annotations.Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable @org.jetbrains.annotations.Nullable ContentValues values, @Nullable @org.jetbrains.annotations.Nullable String selection, @Nullable @org.jetbrains.annotations.Nullable String[] selectionArgs) {
        return 0;
    }
}
