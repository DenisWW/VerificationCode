package com.nineone.verificationcode.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.net.VpnManager;
import android.net.VpnService;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.utils.CenterAlignImageSpan;
import com.nineone.verificationcode.utils.ToastUtils;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class OpenGlActivity extends Activity {

    private RecyclerView recycler_view;
    private RecyclerView recycler_view1;
    private RecyclerView recycler_view2;
    private FrameLayout layout;
    private TextView center_view;
    private static int itemHeight;
    private static float density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view1 = findViewById(R.id.recycler_view1);
        recycler_view2 = findViewById(R.id.recycler_view2);
        center_view = findViewById(R.id.center_view);
        center_view.setTextColor(ContextCompat.getColorStateList(this, R.color.text_selector));
        center_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "点击一下", Toast.LENGTH_SHORT).show();
            }
        });
        layout = findViewById(R.id.layout);
        LinearLayoutManager rightManager;
        recycler_view.setLayoutManager(new CustomLayoutManager(this));
        recycler_view1.setLayoutManager(rightManager = new CustomLayoutManager(this));

        TextView textView = findViewById(R.id.text_test_view);
        String s = " 1234567890";
//        String s = " 沪ag萨达啥今天亏打发了是啊哈\n哈哈哈";
//        s = " 沪a萨达啥今天亏打发了是啊哈哈哈哈哈哈哈哈哈哈\n*";
        s = " 国家萨达啥今天亏打发了是啊哈哈哈哈哈哈哈哈哈哈\n*";
        SpannableStringBuilder spannable = new SpannableStringBuilder(s);
//        spannable.setSpan(new DrawableMarginSpan( getResources().getDrawable(R.drawable.stroke_eb4a38_1)), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CenterAlignImageSpan(this, R.mipmap.label_ke), s.length() - 1, s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CenterAlignImageSpan(this, R.mipmap.label_ke), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);



        SnapHelperCenter pagerSnapHelper = new SnapHelperCenter();
        pagerSnapHelper.attachToRecyclerView(recycler_view);

        SnapHelperCenter pagerSnapHelper1 = new SnapHelperCenter();
        pagerSnapHelper1.attachToRecyclerView(recycler_view1);

        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) layout.getLayoutParams();

        int intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        center_view.measure(intw, inth);
        textView.measure(intw, inth);
        layout.setLayoutParams(ll);
        if (itemHeight == 0) {
            itemHeight = center_view.getMeasuredHeight();
        }
        ll.height = center_view.getMeasuredHeight() * 7;
        density = getResources().getDisplayMetrics().density;
        recycler_view.setAdapter(new TextViewAdapter());
        recycler_view1.setAdapter(new TextViewAdapter1());

        rightManager.scrollToPositionWithOffset(0, -itemHeight);
//        rightManager.scrollToPositionWithOffset(0, 0);
        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Log.e("Thread222222", "======" + Thread.currentThread().getName());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                            Log.e("Thread222233", "======" + Thread.currentThread().getName());
                        }
                    };
                    thread.start();
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("Thread1111", "======" + Thread.currentThread().getName());
            }
        }.start();
//        Log.e("=======", "======" + "".charAt(0));
        //        BigDecimal decimal = new BigDecimal("1.402000");
//        BigDecimal bigDecimal = decimal.setScale(2, RoundingMode.FLOOR);
//        DecimalFormat decimalFormat=new DecimalFormat();
//        decimalFormat.format()

//        recycler_view1.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            int currentCenterItem;
//            private LinearLayoutManager layoutManager;
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (layoutManager == null)
//                    layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (layoutManager != null) {
//                    int center = (layoutManager.findFirstVisibleItemPosition()
//                            + (layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition()) / 2);
//                    View view = layoutManager.findViewByPosition(center);
//                    if (view == null) return;
//                    int centerHeight = recyclerView.getHeight() / 2;
//                    int top = (view.getTop() + view.getHeight() / 2);
//                    int distance = Math.abs(top - centerHeight);
//                    if (distance > view.getMeasuredHeight() / 2) {
//                        if (currentCenterItem != center + 1) {
//                            currentCenterItem = center + 1;
//                            Log.e("onScrolled", "====" + currentCenterItem);
//                        }
//                    } else {
//                        if (currentCenterItem != center) {
//                            currentCenterItem = center;
//                            Log.e("onScrolled", "====" + currentCenterItem);
//                        }
//                    }
//                }
//            }
//        });

    }

    public static class SnapHelperCenter extends SnapHelper {
        @Nullable
        private OrientationHelper mVerticalHelper;
        private static final float INVALID_DISTANCE = 1f;

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
            int[] out = new int[2];
            if (layoutManager.canScrollVertically()) {
//                Log.e("calculateDistance", "===" + "   position=" + layoutManager.getPosition(targetView));
                out[1] = distanceToCenter(targetView, getVerticalHelper(layoutManager));
            }
            return out;
        }

        @Nullable
        @Override
        public View findSnapView(RecyclerView.LayoutManager layoutManager) {
            if (layoutManager.canScrollVertically()) {
                return findCenterView(layoutManager, getVerticalHelper(layoutManager));
            }
            return null;
        }

        @Nullable
        private View findCenterView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
            int childCount = layoutManager.getChildCount();
            if (childCount == 0) {
                return null;
            }
            View closestChild = null;
            final int center = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
            int absClosest = Integer.MAX_VALUE;
            float scaleValue;
            for (int i = 0; i < childCount; i++) {
                final View child = layoutManager.getChildAt(i);
                if (child == null) continue;
                int childCenter = helper.getDecoratedStart(child) + child.getPaddingTop() +
                        ((helper.getDecoratedMeasurement(child) - child.getPaddingTop() - child.getPaddingBottom()) / 2);
                int absDistance = Math.abs(childCenter - center);
                /** if child center is closer than previous closest, set it as closest  **/
                if (absDistance < absClosest) {
                    absClosest = absDistance;
                    closestChild = child;
                }
                float viewCenter = child.getTop() + child.getPaddingTop() +
                        (child.getHeight() - child.getPaddingTop() - child.getPaddingBottom()) / 2F;
//                int viewCenter = (child.getHeight() - child.getPaddingTop() - child.getPaddingBottom()) / 2 + child.getTop() + child.getPaddingTop();
                int distanceCenter = (int) Math.abs(viewCenter - center);
                TextView tv = ((TextView) ((ViewGroup) child).getChildAt(0));
                TextView invisibleTv = ((TextView) ((ViewGroup) child).getChildAt(1));
                scaleValue = Math.min((distanceCenter * 1F / invisibleTv.getMeasuredHeight()), 1F);
                float textSize = 18F * density - 4F * density * scaleValue;
                if (scaleValue > 0.5f) {
                    tv.setTextColor(Color.parseColor("#999999"));
                    tv.setTypeface(Typeface.DEFAULT);
                } else {
                    tv.setTextColor(Color.parseColor("#333333"));
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if (tv.getTextSize() != textSize)
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            Log.e("findCenterView", "====" + closestChild);
            return closestChild;
        }


        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
                return RecyclerView.NO_POSITION;
            }

            final int itemCount = layoutManager.getItemCount();
            if (itemCount == 0) {
                return RecyclerView.NO_POSITION;
            }

            final View currentView = findSnapView(layoutManager);
            if (currentView == null) {
                return RecyclerView.NO_POSITION;
            }

            final int currentPosition = layoutManager.getPosition(currentView);
            if (currentPosition == RecyclerView.NO_POSITION) {
                return RecyclerView.NO_POSITION;
            }

            RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider =
                    (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
            // deltaJumps sign comes from the velocity which may not match the order of children in
            // the LayoutManager. To overcome this, we ask for a vector from the LayoutManager to
            // get the direction.
            PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
            if (vectorForEnd == null) {
                // cannot get a vector for the given position.
                return RecyclerView.NO_POSITION;
            }

            int vDeltaJump, hDeltaJump;
            hDeltaJump = 0;
            if (layoutManager.canScrollVertically()) {
                vDeltaJump = estimateNextPositionDiffForFling(layoutManager, getVerticalHelper(layoutManager), 0, velocityY);
                if (vectorForEnd.y < 0) {
                    vDeltaJump = -vDeltaJump;
                }
            } else {
                vDeltaJump = 0;
            }

            int deltaJump = layoutManager.canScrollVertically() ? vDeltaJump : hDeltaJump;
            if (deltaJump == 0) {
                return RecyclerView.NO_POSITION;
            }
            int targetPos = currentPosition + deltaJump;
            if (targetPos < 0) {
                targetPos = 0;
            }
            if (targetPos >= itemCount) {
                targetPos = itemCount - 1;
            }
            return targetPos;
        }

        private int distanceToCenter(@NonNull View targetView, OrientationHelper helper) {
            final int childCenter = helper.getDecoratedStart(targetView) + (helper.getDecoratedMeasurement(targetView) / 2);
            final int containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
            return childCenter - containerCenter;
        }

        private int estimateNextPositionDiffForFling(
                RecyclerView.LayoutManager layoutManager, OrientationHelper helper, int velocityX, int velocityY) {
            int[] distances = calculateScrollDistance(velocityX, velocityY);
            float distancePerChild = computeDistancePerChild(layoutManager, helper);
            if (distancePerChild <= 0) {
                return 0;
            }
            int distance = Math.abs(distances[0]) > Math.abs(distances[1]) ? distances[0] : distances[1];
            return Math.round(distance / distancePerChild);
        }

        private float computeDistancePerChild(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
            View minPosView = null;
            View maxPosView = null;
            int minPos = Integer.MAX_VALUE;
            int maxPos = Integer.MIN_VALUE;
            int childCount = layoutManager.getChildCount();
            if (childCount == 0) {
                return INVALID_DISTANCE;
            }

            for (int i = 0; i < childCount; i++) {
                View child = layoutManager.getChildAt(i);
                final int pos = layoutManager.getPosition(child);
                if (pos == RecyclerView.NO_POSITION) {
                    continue;
                }
                if (pos < minPos) {
                    minPos = pos;
                    minPosView = child;
                }
                if (pos > maxPos) {
                    maxPos = pos;
                    maxPosView = child;
                }
            }
            if (minPosView == null || maxPosView == null) {
                return INVALID_DISTANCE;
            }
            int start = Math.min(helper.getDecoratedStart(minPosView),
                    helper.getDecoratedStart(maxPosView));
            int end = Math.max(helper.getDecoratedEnd(minPosView),
                    helper.getDecoratedEnd(maxPosView));
            int distance = end - start;
            if (distance == 0) {
                return INVALID_DISTANCE;
            }
            return 1f * distance / ((maxPos - minPos) + 1);
        }

        @NonNull
        private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
            if (mVerticalHelper == null || mVerticalHelper.getLayoutManager() != layoutManager) {
                mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
            }
            return mVerticalHelper;
        }
    }

    public static class CustomLayoutManager extends LinearLayoutManager {

        public CustomLayoutManager(Context context) {
            super(context);
        }


        @Override
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            ViewGroup child = null;
            final int center = getHeight() / 2;
            float scaleValue;
            int absClosest = Integer.MAX_VALUE;
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) == null) continue;
                child = (ViewGroup) getChildAt(i);
                if (child == null) continue;
                float viewCenter = child.getTop() + child.getPaddingTop() +
                        (child.getHeight() - child.getPaddingTop() - child.getPaddingBottom()) / 2F;
                int distanceCenter = (int) Math.abs(viewCenter - center);
                TextView tv = ((TextView) child.getChildAt(0));
                TextView invisibleTv = ((TextView) child.getChildAt(1));
                scaleValue = Math.min((distanceCenter * 1F / invisibleTv.getMeasuredHeight()), 1F);
                float textSize = 18F * density - 4F * density * scaleValue;
//                Log.e("distanceCenter", "  ===" + i + "  distanceCenter=" + distanceCenter + "  scaleValue=" + scaleValue);
                if (scaleValue > 0.5f) {
                    tv.setTextColor(Color.parseColor("#999999"));
                    tv.setTypeface(Typeface.DEFAULT);
                } else {
                    tv.setTextColor(Color.parseColor("#333333"));
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                }
//                tv.setPivotX(0);
//                tv.setPivotY(tv.getHeight() / 2F);
//                tv.setScaleX(1F + (1F - scaleValue) * 1 / 6);
//                tv.setScaleY(1F + (1F - scaleValue) * 1 / 6);
                if (tv.getTextSize() != textSize)
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }
            return super.scrollVerticallyBy(dy, recycler, state);
        }
    }

    public static class TextViewAdapter extends RecyclerView.Adapter<TextViewViewHolder> {

        private final List<String> data = new ArrayList<>();

        public TextViewAdapter() {
            for (int i = 0; i < 20; i++) {
                data.add("测试" + i);
            }
        }

        @NonNull
        @Override
        public TextViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new TextViewViewHolder(linearLayout);
        }


        @Override
        public void onBindViewHolder(@NonNull TextViewViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.textView.getLayoutParams();
//            if (position == 0 && layoutParams.topMargin == 0) {
//                layoutParams.topMargin = itemHeight;
//                holder.textView.setLayoutParams(layoutParams);
//            } else if (position != 0 && layoutParams.topMargin > 0) {
//                layoutParams.topMargin = 0;
//                holder.textView.setLayoutParams(layoutParams);
//            }
            if ((position == 0)) {
//                 && holder.itemView.getPaddingTop() == 0
                holder.itemView.setPadding(0, itemHeight * 3, 0, 0);
            } else if (position == data.size() - 1) {
//                && holder.itemView.getPaddingBottom() == 0
                holder.itemView.setPadding(0, 0, 0, itemHeight * 3);
            } else {
                holder.itemView.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public static class TextViewViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textViewInvisible;

        public TextViewViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = new TextView(itemView.getContext());
            ((LinearLayout) itemView).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout) itemView).setGravity(Gravity.CENTER);
            ((ViewGroup) itemView).addView(textView);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) textView.getLayoutParams();
            ll.weight = 1;
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT;
            ll.height = ViewGroup.LayoutParams.MATCH_PARENT;
            ll.gravity = Gravity.CENTER | Gravity.START;
            textView.setLayoutParams(ll);
//            textView.setPadding(0, 0, 0, 0);
            textView.setGravity(Gravity.START | Gravity.CENTER);
            textView.setMaxLines(1);
            textView.setTextSize(14f);

            textViewInvisible = new TextView(itemView.getContext());
            ((ViewGroup) itemView).addView(textViewInvisible, new ViewGroup.LayoutParams(1, LinearLayout.LayoutParams.WRAP_CONTENT));
            textViewInvisible.setTextSize(18F);
            textViewInvisible.setPadding(0, (int) itemView.getResources().getDisplayMetrics().density * 5, 0, (int) itemView.getResources().getDisplayMetrics().density * 5);


        }

    }

    public static class TextViewAdapter1 extends RecyclerView.Adapter<TextViewViewHolder> {

        private final List<String> data = new ArrayList<>();

        public TextViewAdapter1() {
            for (int i = 0; i < 20; i++) {
                data.add("测" + i);
            }
        }

        @NonNull
        @Override
        public TextViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new TextViewViewHolder(linearLayout);
        }


        @Override
        public void onBindViewHolder(@NonNull TextViewViewHolder holder, int position) {
//            Log.e("holder.itemView", "====" + holder.itemView.getLayoutParams());
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (position == 0 || position == data.size() + 1) {
                holder.textView.setText("");
                layoutParams.height = itemHeight * 3;
                holder.itemView.setPadding(0, position == 0 ? itemHeight * 2 : 0, 0, position == data.size() + 1 ? itemHeight * 2 : 0);
            } else {
                holder.textView.setText(data.get(position - 1));
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.itemView.setPadding(0, 0, 0, 0);
            }
            holder.itemView.setLayoutParams(layoutParams);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.textView.getLayoutParams();
//            if (position == 0 && layoutParams.topMargin == 0) {
//                layoutParams.topMargin = itemHeight;
//                holder.textView.setLayoutParams(layoutParams);
//            } else if (position != 0 && layoutParams.topMargin > 0) {
//                layoutParams.topMargin = 0;
//                holder.textView.setLayoutParams(layoutParams);
//            }
        }

        @Override
        public int getItemCount() {
            return data.size() + 2;
        }
    }

    public static class TextViewAdapter2 extends RecyclerView.Adapter<TextViewViewHolder> {

        private final List<String> data = new ArrayList<>();

        public TextViewAdapter2() {
            for (int i = 0; i < 20; i++) {
                data.add("测试" + i);
            }
        }

        @NonNull
        @Override
        public TextViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new TextViewViewHolder(linearLayout);
        }


        @Override
        public void onBindViewHolder(@NonNull TextViewViewHolder holder, int position) {
//            Log.e("holder.itemView", "====" + holder.itemView.getLayoutParams());
//            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (position < 3 || position > data.size() + 2) {
                holder.textView.setText("");
//                layoutParams.height = itemHeight * 3;
//                holder.itemView.setPadding(0, position == 0 ? itemHeight * 2 : 0, 0, position == data.size() + 1 ? itemHeight * 2 : 0);
            } else {
                holder.textView.setText(data.get(position - 3));
//                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                holder.itemView.setPadding(0, 0, 0, 0);
            }
//            holder.itemView.setLayoutParams(layoutParams);

//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.textView.getLayoutParams();
//            if (position == 0 && layoutParams.topMargin == 0) {
//                layoutParams.topMargin = itemHeight;
//                holder.textView.setLayoutParams(layoutParams);
//            } else if (position != 0 && layoutParams.topMargin > 0) {
//                layoutParams.topMargin = 0;
//                holder.textView.setLayoutParams(layoutParams);
//            }
        }

        @Override
        public int getItemCount() {
            return data.size() + 6;
        }
    }

}