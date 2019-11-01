package com.duojia.messager.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duojia.messager.R;


//单例 对话款
public class CommonProgressDialog extends Dialog {

    protected Context mContext = null;
    private LinearLayout mRootView = null;
    private ImageView mImageView = null;
    private TextView contentTextView = null;
    private final int DISMISS_FLAG = 0x10001;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DISMISS_FLAG:
                    dismiss();
                    break;

                default:
                    break;
            }
        }
    };

    public CommonProgressDialog(Context context, int theme) {
        super(context, theme);
        initContentView(context);
    }

    public CommonProgressDialog(Context context) {
        super(context, R.style.loading_dialog);
        initContentView(context);
    }

    private void initContentView(Context context) {
        this.mContext = context;
        mRootView = new LinearLayout(mContext);
        mRootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.setMinimumWidth(mContext.getResources().getDimensionPixelSize(R.dimen.common_progress_mini_width));
        mRootView.setMinimumHeight(mContext.getResources().getDimensionPixelSize(R.dimen.common_progress_mini_height));
        mRootView.setGravity(Gravity.CENTER);
        mRootView.setPadding(0, mContext.getResources().getDimensionPixelSize(R.dimen.common_progress_padding_top), 0, 0);

        mImageView = new ImageView(mContext);
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.refresh));

        contentTextView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(mContext.getResources().getDimensionPixelSize(R.dimen.common_progress_margin_left), 0, 0, 0);
        contentTextView.setLayoutParams(layoutParams);
        contentTextView.setTextSize(30);
        contentTextView.setText(mContext.getResources().getString(R.string.common_loading_msg));
        contentTextView.setBackground(null);
        mRootView.addView(mImageView);
        mRootView.addView(contentTextView);

        setContentView(mRootView);
        mRootView.setBackgroundResource(R.drawable.shape_base_dialog_bg);
        contentTextView.setTextColor(mContext.getResources().getColor(R.color.progress_text_color));

        // 居中显示
        getWindow().getAttributes().gravity = Gravity.CENTER_HORIZONTAL;
        setCanceledOnTouchOutside(false); // 设置点击屏幕Dialog不消失
        setCancelable(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Animation rotate_anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
        mImageView.setAnimation(rotate_anim);
    }

    // 更新显示文字
    public void setMessage(String strMessage) {
        if (contentTextView != null) {
            contentTextView.setText(strMessage);
        }
    }

    @Override
    public void show() {
        super.show();
        mHandler.sendEmptyMessageDelayed(DISMISS_FLAG, 15 * 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mHandler.removeMessages(DISMISS_FLAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }
}
