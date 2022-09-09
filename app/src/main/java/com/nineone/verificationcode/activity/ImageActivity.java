package com.nineone.verificationcode.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jzsec.imageloader.ImageLoader;
import com.jzsec.imageloader.listener.ImageLoadListener;
import com.jzsec.imageloader.option.ImageCacheType;
import com.jzsec.imageloader.option.ImageLoadType;
import com.jzsec.imageloader.option.ImageScaleType;
import com.nineone.verificationcode.BuildConfig;
import com.nineone.verificationcode.R;
import com.nineone.verificationcode.bean.UserBean;
import com.nineone.verificationcode.utils.TestFlow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kotlinx.coroutines.flow.Flow;

public class ImageActivity extends Activity {
    private ImageView image;
    private TextView tv_test;
    private ObservableEmitter<String> observableEmitter;
    private ObservableEmitter<Integer> observableEmitterAge;
    private ObservableEmitter<Integer> observableEmitterInteger;
    ;
    Observable<String> observable;
    Observable<Integer> observableAge;
    Observable<Integer> observableInteger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        image = findViewById(R.id.image);
        tv_test = findViewById(R.id.tv_test);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
        FutureTarget<File> fileFutureTarget = Glide.with(this).downloadOnly().load(
                "https://upload-images.jianshu.io/upload_images/4474991-d6c489f54b70e1f3.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200"
        ).skipMemoryCache(true).listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {


                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                saveLocalPath(resource);
                return false;
            }
        }).submit();
        Context context = this;

//        ImageLoader.downLoad(context, "url")//需要下载的图片url
//                .progressListener(new ProgressListener() {
//                    @Override
//                    public void onProgress(int i) {
//
//                    }
//                })//图片加载进度监听
//                .listener(new ImageDownListener() {
//                    @Override
//                    public void onDownStart(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void onDownFailed(String s, String s1, String s2) {
//
//                    }
//
//                    @Override
//                    public void onDownSuccess(String s, String s1) {
//
//                    }
//                })//图片加载生命周期监听
//                .localPath("")//传入保存的本地路劲和名称  xxx.jpg 非必须参数  默认下载为时间戳命名  download文件夹
//                .down();//开启下载并且返回 Rxjava 的Observable<File> 对象


        ImageLoader.with(context,
                "https://upload-images.jianshu.io/upload_images/4474991-d6c489f54b70e1f3.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200")
                .as(ImageLoadType.BITMAP_TYPE)//加载解码方式  gif 或者bitmap drawable
                .round(20)//圆角dp
                .placeholder(R.mipmap.placeholder)
                .listener(new ImageLoadListener() {
                    @Override
                    public void onLoadFailed(Object o) {
                        System.err.println("请求失败");//注意：如果添加占位图 加载失败此方法不会回调。
                    }

                    @Override
                    public void onLoadSuccess(Object o, Object o1) {
                        System.err.println("请求成功");
                    }
                })
                .widthAndHeight(100, 100)//设置宽高
                .diskCacheStrategy(ImageCacheType.AUTOMATIC)//缓存模式
                .scaleType(ImageScaleType.CENTER_CROP)//等于centerCrop()
                .scaleType(ImageScaleType.FIT_CENTER)//等于fitCenter()
                .scaleType(ImageScaleType.CENTER_INSIDE)//等于centerInside()
                .scaleType(ImageScaleType.CIRCLE_CROP)//等于circleCrop()
                .error(R.mipmap.error)//加载出错占位图
                .load(image);//加载到ImageView .preload() 预加载  .loadBitmap() 获取到一个Rxjava 的Observable<Bitmap>

        tv_test.setOnClickListener(v -> {
            if (observableEmitter != null) observableEmitter.onNext("就是这个感觉！");
            if (observableEmitterAge != null) observableEmitterAge.onNext(1000);
//            initJoin();
            initCombineLatest();
        });
        observable = Observable.create(emitter -> {
            //在订阅的时候回调
            observableEmitter = emitter;
        });
        observableAge = Observable.create(emitter -> {
            observableEmitterAge = emitter;
        });
        observableInteger = Observable.just(1, 3, 4);
        observableAge.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
        observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
        Flow<String> stringFlow = new TestFlow().getflow();
//       stringFlow.

//        initZip();
//        initMap();
//        initMerge();
//        initConcat();

//        Observable.concatEager()//前一个完成后一个排干。
//        Observable.amb()//amb操作符，谁先调用onNext谁就是胜利者，
//        Observable.switchOnNext()//和flatMap很像，将Observable发射的数据变换为Observables集合，当原始Observable发射一个新的数据（Observable）时，它将取消订阅前一个Observable。

//        Observable.sequenceEqual()// 用于判断两个Observable发射的数据是否相同（数据，发射顺序，终止状态）。
//        Observable.interval()//创建一个按照给定的时间间隔发射从0开始的整数序列的Observable<Long>，内部通过OnSubscribeTimerPeriodically工作。


//        new ViewHolderBinderPool().addViewHolder(R.layout.activity_image,String.class);
    }

    private void initCombineLatest() {
//        当两个Observables中的任何一个发射了一个数据时，通过一个指定的函数组合每个Observable发射的最新数据（一共两个数据），然后发射这个函数的结果。类似于zip，但是，不同的是zip只有在每个Observable都发射了数据才工作，而combineLatest任何一个发射了数据都可以工作，每次与另一个Observable最近的数据压合。具体请看下面流程图。
//zip工作流程  做Android表单的校验,
        Observable.combineLatest(Observable.fromArray(28), Observable.fromArray("wang"), new BiFunction<Integer, String, UserBean>() {
            @NonNull
            @Override
            public UserBean apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                UserBean userBean = new UserBean();
                userBean.age = integer;
                userBean.name = s;
                return userBean;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Observer<UserBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull UserBean userBean) {
                Log.e("onNext", "====" + userBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
       }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveLocalPath(File file) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator;
        Log.e("path", "====" + path + "      " + path.endsWith(File.separator));
        File saveFileParent = new File(path);
        path += (File.separator + "wwww111.png");
        File saveFile = new File(path);
        if (!saveFileParent.exists()) {
            saveFileParent.mkdirs();
        }

        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        Uri uri = null;
        String substring = path.substring(path.lastIndexOf(File.separator) + 1);
        ContentValues values = new ContentValues();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, substring);
//            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        }
//        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            if (uri != null) {
                outputStream = getContentResolver().openOutputStream(uri);
            }
            if (outputStream == null) {
                if (!saveFile.exists()) saveFile.createNewFile();
                outputStream = new FileOutputStream(saveFile);
            }
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private void initConcat() {
//        .firstElement()   按顺序连接多个Observables。需要注意的是Observable.concat(a,b)等价于a.concatWith(b)。

        Observable.concat(Observable.just(100, 300), Observable.just(200, 400)).subscribeOn(Schedulers.io()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.e("onNext", "====" + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initJoin() {
        //切记发送必须指定发射线程
        Observable.fromArray("wang").subscribeOn(Schedulers.io()).join(Observable.fromArray(28).subscribeOn(Schedulers.io()), new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String integer) throws Exception {
                ObservableSource<String> observable = Observable.fromArray(integer);
                Log.e("Join", "====" + integer + "    " + observable);
                return observable;
            }
        }, new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {
                ObservableSource<Integer> observable = Observable.fromArray(integer);
                Log.e("Join", "====" + integer + "    " + observable);
                return observable;
            }
        }, new BiFunction<String, Integer, UserBean>() {
            @NonNull
            @Override
            public UserBean apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                Log.e("Join___apply", "====" + s + "   " + integer);
                UserBean userBean = new UserBean();
                userBean.age = integer;
                userBean.name = s;
                return userBean;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UserBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("onSubscribe", "====");
            }

            @Override
            public void onNext(@NonNull UserBean userBean) {
                Log.e("Join__UserBean", "====");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("onError", "====");
            }

            @Override
            public void onComplete() {
                Log.e("onComplete", "====");
            }
        });
    }

    private void initMerge() {
        //合并请求   将多个Observable合并为一个。不同于concat，merge不是按照添加顺序连接，而是按照时间线来连接。其中mergeDelayError将异常延迟到其它没有错误的Observable发送完毕后才发射。而merge则是一遇到异常将停止发射数据，发送onError通知。
        Observable.merge(observableInteger, observableAge).subscribeOn(Schedulers.io()).subscribe(new Observer<Integer>() {//Object
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.e("merge_onNext", "====" + integer);
            }


            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void initMap() {
//        Observable.merge()
//        Observable.fromArray(1, 2, 3).mergeWith().delay()
//将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.
//map变换后可以返回任意值，而flatMap则只能返回ObservableSource类型
        Observable.just(1, 2, 3).flatMap(new Function<Integer, ObservableSource<UserBean>>() {
            @Override
            public ObservableSource<UserBean> apply(@NonNull Integer integer) throws Exception {
                UserBean userBean = new UserBean();
                userBean.age = integer;
                Log.e("apply", "====" + integer);
                return Observable.fromArray(userBean);
            }
        })
//                .map(new Function<Integer, UserBean>() {
//
//                    @Override
//                    public UserBean apply(@NonNull Integer integer) throws Exception {
//                        UserBean userBean = new UserBean();
//                        userBean.age = integer;
//                        Log.e("apply", "====" + integer);
//                        return userBean;
//                    }
//                })
                .subscribeOn(Schedulers.io()).subscribe(new Observer<UserBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("map", "====onSubscribe");
            }

            @Override
            public void onNext(@NonNull UserBean userBean) {
                Log.e("map", "====" + userBean.age + Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initZip() {
// 使用一个函数组合多个Observable发射的数据集合，然后再发射这个结果。如果多个Observable发射的数据量不一样，则以最少的Observable为标准进行压合。内部通过OperatorZip进行压合。
        PublishSubject<Object> publishSubject = PublishSubject.create();
        //publishSubject.toFlowable().compose().observeOn().subscribe();

//        Observable.zip(observable, observableInteger, observableAge, new Function3<String, Integer, Integer, Object>() {
//            @NonNull
//            @Override
//            public Object apply(@NonNull String s, @NonNull Integer integer, @NonNull Integer integer2) throws Exception {
//                return null;
//            }
//        });
        Observable.zip(observable, observableAge, new BiFunction<String, Integer, UserBean>() {
            @NonNull
            @Override
            public UserBean apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                UserBean userBean = new UserBean();
                userBean.age = integer;
                userBean.name = s;
                return userBean;
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<UserBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull UserBean userBean) {
                Log.e("onNext", "====" + userBean.name + "   " + userBean.age);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private ImageView imageView;

    public void setImageBitmap(ImageView imageView, String url) {
        //开启线程获取网络图片，设置到显示ImageView上
        new Thread() {
            @Override
            public void run() {
                super.run();
                //用本地资源来模拟加载网络图片
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.placeholder);
                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);
//                imageView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //.....操作UI
            imageView.setImageBitmap((Bitmap) msg.obj);
        }
    };


    public void setImageBitmapByRxJava(ImageView imageView, String url) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                //.......从数据库中取出id资源数组操作
                //用本地资源来模拟加载网络图片
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.placeholder);
                emitter.onNext(bitmap);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())//在IO线程执行数据库处理操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程显示图片
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("----", "onSubscribe");
                    }

                    @Override
                    public void onNext(Bitmap integer) {
                        imageView.setImageBitmap(integer);//拿到id,加载图片
                        Log.d("----", integer + "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("----", e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("----", "onComplete");
                    }
                });
    }

}