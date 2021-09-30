package com.nineone.verificationcode.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.nineone.verificationcode.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreeActivity extends Activity {

    private RecyclerView recycler;
    private LinearLayout root_View;
    private FrameLayout frame;
    private Context context;
    private TextView expand_bt;
    LinkedList<SimpleBean> list = new LinkedList<>();
    private @IdRes
    static Integer[] idss = new Integer[]{
            R.mipmap.one,
            R.mipmap.two,
            R.mipmap.three,
            R.mipmap.four,
            R.mipmap.five,
            R.mipmap.one,
            R.mipmap.two,
            R.mipmap.three,
            R.mipmap.four,
            R.mipmap.five,
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        context = this;
        initView();
//        System.loadLibrary("ijkffmpeg");
    }


    private int height, viewFirstHeight;
    private boolean isExpand = false;
    private ImageView one_iv, two_iv;
    private VegaLayoutManager vegaLayoutManager;
    private SimpleAdapter adapter;

    private void initView() {
        recycler = findViewById(R.id.recycler);
        root_View = findViewById(R.id.root_View);
        one_iv = findViewById(R.id.one_iv);
        two_iv = findViewById(R.id.two_iv);
        frame = findViewById(R.id.frame);
        for (Integer i : idss) {
            list.add(new SimpleBean(i));
            outPos = i;
        }

        RelativeLayout root = findViewById(R.id.root);


        vegaLayoutManager = new VegaLayoutManager();
        recycler.setLayoutManager(vegaLayoutManager);
        LinearLayout.LayoutParams rl = (LinearLayout.LayoutParams) recycler.getLayoutParams();

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            height = rl.height = context.getResources().getDisplayMetrics().heightPixels - context.getResources().getDimensionPixelSize(resourceId);
//        } else {
//            height = rl.height = context.getResources().getDisplayMetrics().heightPixels;
//        }
        height = rl.height = context.getResources().getDisplayMetrics().heightPixels;

        FrameLayout.LayoutParams firstFL = (FrameLayout.LayoutParams) root_View.getLayoutParams();
        firstFL.height = height / 5 + 100;
        root_View.setLayoutParams(firstFL);

        FrameLayout.LayoutParams oneFL = (FrameLayout.LayoutParams) one_iv.getLayoutParams();
        oneFL.height = height / 5 + 100;
        oneFL.topMargin = 100;
        oneFL.bottomMargin = -100;
        one_iv.setLayoutParams(oneFL);


        FrameLayout.LayoutParams twoFL = (FrameLayout.LayoutParams) two_iv.getLayoutParams();
        twoFL.height = height / 5 + 100;
        two_iv.setLayoutParams(twoFL);
        two_iv.setOnClickListener(v -> {

        });
        one_iv.setOnClickListener(v -> {
            Intent intent = new Intent(context, SixeActivity.class);
            startActivity(intent);
        });
        recycler.setLayoutParams(rl);
        vegaLayoutManager.listener = new VegaLayoutManager.OnHeightListener() {
            @Override
            public void onListener(int height) {
                Log.e("onListener", "===" + height);
                if (viewFirstHeight == 0) viewFirstHeight = root_View.getHeight();
                if (height > viewFirstHeight) {
                    FrameLayout.LayoutParams rl1 = (FrameLayout.LayoutParams) root_View.getLayoutParams();
                    rl1.height = height;
                    root_View.setLayoutParams(rl1);
                    if (height - ThreeActivity.this.height >= 0) {
                        FrameLayout.LayoutParams fr = (FrameLayout.LayoutParams) expand_bt.getLayoutParams();
                        fr.bottomMargin = ThreeActivity.this.height - height;
                        expand_bt.setLayoutParams(fr);
                    }
                }
            }

            @Override
            public void onOutListener(int height, int pos) {
                if (viewFirstHeight == 0) viewFirstHeight = root_View.getHeight();
                if (height > viewFirstHeight) {
                    FrameLayout.LayoutParams rl1 = (FrameLayout.LayoutParams) root_View.getLayoutParams();
                    rl1.height = height;
                    root_View.setLayoutParams(rl1);
                    if (height - ThreeActivity.this.height >= 0) {
                        FrameLayout.LayoutParams fr = (FrameLayout.LayoutParams) expand_bt.getLayoutParams();
                        fr.bottomMargin = ThreeActivity.this.height - height;
                        expand_bt.setLayoutParams(fr);
                    }
                }
                if (outPos != pos) outPos = pos;

            }
        };

        recycler.setAdapter(adapter = new SimpleAdapter());
//        recycler.scrollToPosition(15);
        recycler.scrollToPosition(list.size() - 5);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                View view = ((LinearLayoutManager) recyclerView.getLayoutManager()).findViewByPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
//                view.setVisibility(View.INVISIBLE);
//                float scal = view.getTop() / view.getHeight();
            }
        });

        imageViews[0] = one_iv;
        imageViews[1] = two_iv;
        Glide.with(context).load(list.getLast().resId).transform(new CenterCrop(), new RoundedCorners(25)).into(imageViews[0]);
        Glide.with(context).load(list.get(list.size() - 2).resId).transform(new CenterCrop(), new RoundedCorners(25)).into(imageViews[1]);

        expand_bt = findViewById(R.id.expand_bt);
        expand_bt.setOnClickListener(v -> {
            if (isExpand) {
                isExpand = false;
//                vegaLayoutManager.stopAnimator(isExpand, one_iv.getTop(), animatorListenerAdapter);

                vegaLayoutManager.stopAnimator(isExpand, 100, animatorListenerAdapter);
            } else {
                if (objectAnimator != null && !objectAnimator.isRunning()) {
                    expand();
                } else {
                    isNeedExpand = true;
                }

            }
//            startAnimal();
        });
//        mhandler.sendEmptyMessageDelayed(22, 1500);

    }

    private void expand() {
        mhandler.removeMessages(22);
        isExpand = true;
        one_iv.setVisibility(View.GONE);
        two_iv.setVisibility(View.GONE);
        recycler.scrollToPosition(list.size() - 5);
        recycler.setVisibility(View.VISIBLE);
//                vegaLayoutManager.startAnimator(isExpand, one_iv.getTop());
        vegaLayoutManager.startAnimator(isExpand, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeMessages(22);
    }

    private boolean isNeedExpand;

    private int outPos = -1;

    private AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            recycler.setVisibility(View.INVISIBLE);

            if (outPos >= 0 && outPos < list.size() - 1) {
//                List<SimpleBean> simpleBeans = list.subList(outPos + 1, list.size());
//                list.removeAll(simpleBeans);
//                list.addAll(0, simpleBeans);
//
                for (int i = outPos + 1; i < list.size(); i++) {
                    SimpleBean bean = list.getLast();
                    list.removeLast();
                    list.addFirst(bean);
                }
                outPos = -1;
            }

            Glide.with(context).load(list.getLast().resId).transform(new CenterCrop(), new RoundedCorners(25)).into(imageViews[0]);
            Glide.with(context).load(list.get(list.size() - 2).resId).transform(new CenterCrop(), new RoundedCorners(25)).into(imageViews[1]);
            one_iv.setVisibility(View.VISIBLE);
            two_iv.setVisibility(View.VISIBLE);
            mhandler.removeMessages(22);
            mhandler.sendEmptyMessageDelayed(22, 1500);
            recycler.scrollToPosition(list.size() - 5);

        }
    };
    private LinkedList<SimpleBean> simpleBeans;
    private Handler mhandler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 22) {
                SimpleBean bean = list.getLast();
                list.removeLast();
                list.addFirst(bean);
                adapter.notifyDataSetChanged();
//                Glide.with(context).load(list.getLast().resId).transform(new CenterCrop(), new RoundedCorners(25)).into(imageViews[0]);
//                Glide.with(context).load(list.get(list.size() - 2).resId).transform(new CenterCrop(), new RoundedCorners(25)).into(imageViews[1]);
                startAnimal();
                mhandler.sendEmptyMessageDelayed(22, 1500);
            }
        }
    };
    private ImageView[] imageViews = new ImageView[2];
    private ValueAnimator objectAnimator;

    private void startAnimal() {
        objectAnimator = ObjectAnimator.ofInt(0, height);
        objectAnimator.setDuration(200);
        objectAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ImageView one = imageViews[0];
            ImageView two = imageViews[1];
            one.setTranslationY(value);
            two.setScaleX((0.2f * animation.getAnimatedFraction()) + 0.8f);
            FrameLayout.LayoutParams oneFL = (FrameLayout.LayoutParams) two.getLayoutParams();
            oneFL.height = height / 5 + 100;
            oneFL.topMargin = (int) (100 * animation.getAnimatedFraction());
            oneFL.bottomMargin = (int) (-100 * animation.getAnimatedFraction());
            two.setLayoutParams(oneFL);

        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ImageView one = imageViews[0];
                ImageView two = imageViews[1];
                frame.removeView(one);
                one.setScaleX(0.8f);
                frame.addView(one, 1, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height / 5 + 100));
                ValueAnimator objectAnimator = ObjectAnimator.ofFloat(one, "translationY", 100, 0);

                objectAnimator.setDuration(100);
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (isNeedExpand) {
                            isNeedExpand = false;
                            expand();
                        }
                    }
                });
                imageViews = new ImageView[2];
                imageViews[0] = two;
                imageViews[1] = one;
                Glide.with(context).load(list.get(list.size() - 2).resId).transform(new CenterCrop(), new RoundedCorners(25)).into(imageViews[1]);
                objectAnimator.start();

            }
        });
        objectAnimator.start();


    }

    private class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView textView = new ImageView(context);
            return new SimpleViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
//            holder.imageView.setImageResource(ids.get(position));
            Glide.with(context).load(list.get(position).resId).transform(new CenterCrop(), new RoundedCorners(25)).into(holder.imageView);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height / 5) + 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public class Test extends LinearLayoutManager {

        public Test(Context context) {
            super(context);
        }

        public Test(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

//        @Override
//        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//            super.onLayoutChildren(recycler, state);
//
//
//        }

    }

    public static class StartSnapHelper extends LinearSnapHelper {

        @SuppressLint("NewApi")
        @Override
        public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
            int[] out = new int[2];
            out[0] = 0;
//            out[1] = ((VegaLayoutManager) layoutManager).getSnapHeight();
            out[1] = 0;
            return out;
        }

        @SuppressLint("NewApi")
        @Override
        public View findSnapView(RecyclerView.LayoutManager layoutManager) {
            if (layoutManager instanceof VegaLayoutManager) {
                VegaLayoutManager custLayoutManager = (VegaLayoutManager) layoutManager;
                return custLayoutManager.findSnapView();
            }

            Log.e("findSnapView", "==" + layoutManager);
            LinearLayoutManager custLayoutManager = (LinearLayoutManager) layoutManager;
            return custLayoutManager.findViewByPosition(custLayoutManager.findFirstVisibleItemPosition());
        }
    }

    //    private class MyLayout extends RecyclerView.LayoutManager {
//        public OrientationHelper mVerticalHelper;
//        private SparseArray<Rect> locationRects = new SparseArray<>();
//        private SparseBooleanArray attachedItems = new SparseBooleanArray();
//
//        public MyLayout(Context context) {
//            if (mVerticalHelper == null)
//                mVerticalHelper = OrientationHelper.createHorizontalHelper(this);
//
//        }
//
//
//        @Override
//        public void offsetChildrenVertical(int dy) {
//            super.offsetChildrenVertical(dy);
//        }
//
//        @Override
//        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
//            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//
//        @Override
//        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
////            super.onLayoutChildren(recycler, state);
//            detachAndScrapAttachedViews(recycler);
//            Log.e("recycler", "====" + getItemCount() + "   " + getWidth() + "     " + getHeight());
//            int allHeight = 0;
//            for (int i = 0; i < getItemCount(); i++) {
//                View view = recycler.getViewForPosition(i);
//                addView(view);
//                measureChildWithMargins(view, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                int height = getDecoratedMeasuredHeight(view);
//                Log.e("recycler", "====" + i + "   " + view.getWidth() + "     " + height);
//                Rect rect = new Rect();
//                rect.top = allHeight;
//                allHeight += height;
//                rect.left = 0;
//                rect.right = getWidth();
//                rect.bottom = allHeight;
//                locationRects.put(i, rect);
////                layoutItem(view, rect);
//                layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
//            }
//        }
//
//        @Override
//        public boolean canScrollVertically() {
//            return true;
//        }
//
//        private void layoutItemsOnCreate(RecyclerView.Recycler recycler) {
//            int itemCount = getItemCount();
//            Rect displayRect = new Rect(0, 0, getWidth(), getHeight());
//            for (int i = 0; i < itemCount; i++) {
//                Rect thisRect = locationRects.get(i);
//                if (Rect.intersects(displayRect, thisRect)) {
//                    View childView = recycler.getViewForPosition(i);
//                    addView(childView);
//                    measureChildWithMargins(childView, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                    layoutItem(childView, locationRects.get(i));
//                    attachedItems.put(i, true);
//                    childView.setPivotY(0);
//                    childView.setPivotX(childView.getMeasuredWidth() / 2);
//                }
//            }
//        }
//
//
//        private void layoutItem(View child, Rect rect) {
//            int topDistance = rect.top;
//            int layoutTop, layoutBottom;
//            int itemHeight = rect.bottom - rect.top;
//            if (topDistance < itemHeight && topDistance > 0) {
//                float rate1 = (float) topDistance / itemHeight;
//                float rate2 = 1 - rate1 * rate1 / 3;
//                float rate3 = 1 - rate1 * rate1;
//                child.setScaleX(rate2);
//                child.setScaleY(rate2);
//                child.setAlpha(rate3);
//                layoutTop = 0;
//                layoutBottom = itemHeight;
//            } else {
//                child.setScaleX(1);
//                child.setScaleY(1);
//                child.setAlpha(1);
//
//                layoutTop = rect.top;
//                layoutBottom = rect.bottom;
//            }
//            layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom);
//        }
//
//
//        @Override
//        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//            View view = getChildAt(0);
//            if (view != null) {
//                view.setScaleX(1f - Math.abs(view.getTop()) * 1.0f / view.getHeight());
//                view.setScaleY(1f - Math.abs(view.getTop()) * 1.0f / view.getHeight());
//            }
//            Log.e("scrollVerticallyBy", "==" + dy);
//            return dy;
////            return dy;
//        }
//
//        @Override
//        public void onAttachedToWindow(RecyclerView view) {
//            super.onAttachedToWindow(view);
//            new StartSnapHelper().attachToRecyclerView(view);
//        }
//    }
    private class SimpleBean {
        @RawRes
        @DrawableRes
        Integer resId;

        String name;

        public SimpleBean(int resId) {
            this.resId = resId;
        }
    }
}