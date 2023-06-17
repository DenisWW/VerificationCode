package com.nineone.verificationcode.uikit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class SimpleSelectHelper {
    public static CustomVerticallyLayoutManager createVerticallyLayoutManager(Context context, float normalTextSize, float maxTextSize) {
        return new CustomVerticallyLayoutManager(context, normalTextSize, maxTextSize);
    }

    public static SnapVerticalCenterHelper createVerticallySnapVerticalHelper(float normalTextSize, float maxTextSize) {
        return new SnapVerticalCenterHelper(normalTextSize, maxTextSize);

    }

    public static SelectTextViewAdapter createVerticallyAdapter(float normalTextSize, float maxTextSize, int topAndBottomPadding, int itemCount, int gravity) {
        return new SelectTextViewAdapter(normalTextSize, maxTextSize, topAndBottomPadding, itemCount, gravity);
    }

    public static SelectSimpleTextViewAdapter createVerticallySimpleAdapter(float normalTextSize, float maxTextSize, int topAndBottomPadding, int itemCount, int gravity) {
        return new SelectSimpleTextViewAdapter(normalTextSize, maxTextSize, topAndBottomPadding, itemCount, gravity);
    }


    private static void setCurrentTvStyle(TextView textView) {
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private static void setNormalTvStyle(TextView textView) {
        textView.setTextColor(Color.parseColor("#999999"));
        textView.setTypeface(Typeface.DEFAULT);
    }

    static class CustomVerticallyLayoutManager extends LinearLayoutManager {

        private final float normalTextSize, maxTextSize;
        private int currentCenterItem, center;
//        private SimpleSelectListLayout.SimpleSelectListener listener;

        public CustomVerticallyLayoutManager(Context context, float normalTextSize, float maxTextSize) {
            super(context);
            this.normalTextSize = normalTextSize;
            this.maxTextSize = maxTextSize;
        }

//        public void setListener(SimpleSelectListener listener) {
//            this.listener = listener;
//        }

        @Override
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            View child;
            if (center == 0) center = getHeight() / 2;
            ViewGroup group;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                if (child == null) continue;
                int viewCenter = getCenterDistance(child);
                int distanceCenter = Math.abs(viewCenter - center);
                if (child instanceof ViewGroup) {
                    group = (ViewGroup) child;
                    upItemTextView(group, distanceCenter, normalTextSize, maxTextSize);
//                    if (listener != null) {
//                        View invisibleView = group.getChildAt(1);
//                        if (distanceCenter < invisibleView.getMeasuredHeight() / 2
//                                && currentCenterItem != getPosition(child)) {
//                            currentCenterItem = getPosition(child);
//                            listener.onSelectItem(currentCenterItem);
//                        }
//                    }

                }

            }
            return super.scrollVerticallyBy(dy, recycler, state);
        }

        public int findCenterPosition() {
            View child;
            if (center == 0) center = getHeight() / 2;
            ViewGroup group;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                if (child == null) continue;
                int viewCenter = getCenterDistance(child);
                int distanceCenter = Math.abs(viewCenter - center);
                if (child instanceof ViewGroup) {
                    group = (ViewGroup) child;
                    View invisibleView = group.getChildAt(1);
                    if (distanceCenter < invisibleView.getMeasuredHeight() / 2) {
                        return getPosition(child);
                    }
                }

            }
            return -1;
        }

        private int getCenterDistance(View child) {
            return (child.getHeight() - child.getPaddingTop() - child.getPaddingBottom()) / 2 + child.getTop() + child.getPaddingTop();
        }

    }

    private static void upItemTextView(ViewGroup child, int distanceCenter, float normalTextSize, float maxTextSize) {
        TextView tv = ((TextView) child.getChildAt(0));
        View invisibleView = child.getChildAt(1);
        float scaleValue = Math.min((distanceCenter * 1F / invisibleView.getMeasuredHeight()), 1F);
        if (scaleValue > 0.5f) {
            setNormalTvStyle(tv);
        } else {
            setCurrentTvStyle(tv);
        }
        if (maxTextSize != normalTextSize) {
            float textSize = maxTextSize * tv.getResources().getDisplayMetrics().density - (maxTextSize - normalTextSize) * tv.getResources().getDisplayMetrics().density * scaleValue;
            if (tv.getTextSize() != textSize) tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    static class SnapVerticalCenterHelper extends LinearSnapHelper {
        @Nullable
        private OrientationHelper mVerticalHelper;
        private final float normalTextSize, maxTextSize;

        public SnapVerticalCenterHelper(float normalTextSize, float maxTextSize) {
            this.normalTextSize = normalTextSize;
            this.maxTextSize = maxTextSize;
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
            for (int i = 0; i < childCount; i++) {
                final View child = layoutManager.getChildAt(i);
                if (child == null) continue;
                int childCenter = helper.getDecoratedStart(child) + child.getPaddingTop() + ((helper.getDecoratedMeasurement(child) - child.getPaddingTop() - child.getPaddingBottom()) / 2);
                int absDistance = Math.abs(childCenter - center);
                if (absDistance < absClosest) {
                    absClosest = absDistance;
                    closestChild = child;
                }
                if (child instanceof ViewGroup)
                    upItemTextView((ViewGroup) child, absDistance, normalTextSize, maxTextSize);
            }

            return closestChild;
        }

        @NonNull
        private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
            if (mVerticalHelper == null || mVerticalHelper.getLayoutManager() != layoutManager) {
                mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
            }
            return mVerticalHelper;
        }
    }

    static class SelectTextViewAdapter extends RecyclerView.Adapter<SelectTextViewViewHolder> {

        private List<String> data = new ArrayList<>();
        private final int topAndBottomPadding, itemCount;
        private final float normalTextSize, maxTextSize;
        private final int gravity;
        private int currentPosition = 0;

        public SelectTextViewAdapter(float normalTextSize, float maxTextSize, int topAndBottomPadding, int itemCount, int gravity) {
            this.normalTextSize = normalTextSize;
            this.maxTextSize = maxTextSize;
            this.gravity = gravity;
            this.topAndBottomPadding = topAndBottomPadding;
            this.itemCount = itemCount;
        }

        public void setData(List<String> data, int currentPosition) {
            this.data = data;
            this.currentPosition = currentPosition;
            notifyDataSetChanged();
        }

        public void setData(List<String> data) {
            setData(data, 0);
        }

        @NonNull
        @Override
        public SelectTextViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new SelectTextViewViewHolder(linearLayout, normalTextSize, maxTextSize, topAndBottomPadding, gravity);
        }


        @Override
        public void onBindViewHolder(@NonNull SelectTextViewViewHolder holder, int position) {
            if (position < itemCount || position > data.size() + itemCount - 1) {
                holder.textView.setText("");
            } else {
                holder.textView.setText(data.get(position - itemCount));
            }
            if (currentPosition != -1 && position == currentPosition + itemCount) {
                currentPosition = -1;
                setCurrentTvStyle(holder.textView);
                holder.textView.setTextSize(maxTextSize);
            } else {
                setNormalTvStyle(holder.textView);
                holder.textView.setTextSize(normalTextSize);
            }

        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() > 0 ? data.size() + itemCount * 2 : 0 : 0;
        }
    }

    static class SelectSimpleTextViewAdapter extends RecyclerView.Adapter<SelectTextViewViewHolder> {

        private List<String> data = new ArrayList<>();
        private final int topAndBottomPadding, itemHeight;
        private final float normalTextSize, maxTextSize;
        private final int gravity;
        private int currentPosition = 0;

        public SelectSimpleTextViewAdapter(float normalTextSize, float maxTextSize, int topAndBottomPadding, int itemHeight, int gravity) {
            this.normalTextSize = normalTextSize;
            this.maxTextSize = maxTextSize;
            this.gravity = gravity;
            this.itemHeight = itemHeight;
            this.topAndBottomPadding = topAndBottomPadding;
        }

        public void setData(List<String> data, int currentPosition) {
            this.data = data;
            this.currentPosition = currentPosition;
            notifyDataSetChanged();
        }

        public void setData(List<String> data) {
            setData(data, 0);
        }

        @NonNull
        @Override
        public SelectTextViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new SelectTextViewViewHolder(linearLayout, normalTextSize, maxTextSize, topAndBottomPadding, gravity);
        }


        @Override
        public void onBindViewHolder(@NonNull SelectTextViewViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
            if (currentPosition != -1 && position == currentPosition) {
                currentPosition = -1;
                setCurrentTvStyle(holder.textView);
                holder.textView.setTextSize(maxTextSize);
            } else {
                setNormalTvStyle(holder.textView);
                holder.textView.setTextSize(normalTextSize);
            }
            if ((position == 0)) {
                if (holder.itemView.getPaddingTop() == 0)
                    holder.itemView.setPadding(0, itemHeight, 0, 0);
            } else if (position == data.size() - 1) {
                if (holder.itemView.getPaddingBottom() == 0)
                    holder.itemView.setPadding(0, 0, 0, itemHeight);
            } else {
                holder.itemView.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }
    }

    static class SelectTextViewViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public SelectTextViewViewHolder(@NonNull View itemView, float normalTextSize, float maxTextSize, int topAndBottomPadding, int gravity) {
            super(itemView);
            textView = new TextView(itemView.getContext());
            LinearLayout linearLayout = (LinearLayout) itemView;
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) textView.getLayoutParams();
            ll.weight = 1;
            ll.width = ViewGroup.LayoutParams.MATCH_PARENT;
            ll.height = ViewGroup.LayoutParams.MATCH_PARENT;
            ll.gravity = Gravity.CENTER | Gravity.START;
            textView.setLayoutParams(ll);

            textView.setGravity(gravity);

            textView.setMaxLines(1);
            textView.setTextSize(normalTextSize);

            TextView invisibleTv = new TextView(linearLayout.getContext());
            linearLayout.addView(invisibleTv, new ViewGroup.LayoutParams(1, LinearLayout.LayoutParams.WRAP_CONTENT));
            invisibleTv.setTextSize(maxTextSize);
            invisibleTv.setVisibility(View.INVISIBLE);
            invisibleTv.setPadding(0, (int) linearLayout.getResources().getDisplayMetrics().density * topAndBottomPadding, 0, (int) itemView.getResources().getDisplayMetrics().density * topAndBottomPadding);

        }
    }
}
