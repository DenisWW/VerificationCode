package com.jzsec.web.apptest.utils;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.jzsec.web.apptest.listener.SimpleDialogListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class DownManager {

    public static void start(String url, String targetPath, SimpleDialogListener simpleDialogListener) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Call call = client.newCall(new Request.Builder().url(url).build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                long totalLength = response.body().contentLength();
                InputStream inputStream = response.body().byteStream();
                Reader charStream = response.body().charStream();
                BufferedSource buffer = response.body().source();
                Log.e("onResponse", "====" + response.body().contentType()
                        + "    totalLength=" + totalLength
                        + "    getName=" + Thread.currentThread().toString()
                        + "    source==" + buffer
                        + "    charStream==" + charStream
                        + "    inputStream==" + inputStream
                );
                int length = 0;
                File file = new File(targetPath + ".temp");
                File fileFinish = new File(targetPath);

//                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
//                char[] chars = new char[512];
//                while ((length = charStream.read(chars)) != -1) {
//                    Log.e("length", "===" + length+"   =="+(new String(chars,0,length)));
//                    bufferedWriter.write(chars, 0, length);
//                }
//                bufferedWriter.flush();
//                bufferedWriter.close();

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                long sum = 0;
                byte[] bytes = new byte[1024];
                while ((length = buffer.inputStream().read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);
                    sum += length;
                    Log.e("downing", "=== sum=" + sum + "    totalLength=" + totalLength);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                file.renameTo(fileFinish);
                if (simpleDialogListener != null) simpleDialogListener.onDownFinish();
            }
        });
    }
}
