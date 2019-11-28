package com.ring.myguide.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ring.myguide.R;

/**
 * Created by ring on 2019/8/14.
 */
public class InputDialog extends Dialog {

    private TextView mTitleText;
    private Button mSureBtn;
    private Button mCancelBtn;
    private EditText mInputEdit;

    private String mTitle;
    private String mContent;
    private myOnclickListener mMyOnclickListener;


    private InputDialog(Builder builder) {
        super(builder.mContext);
        mTitle = builder.mTitle;
        mContent = builder.mContent;
        mMyOnclickListener = builder.mMyOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
        mTitleText = findViewById(R.id.tv_title);
        mSureBtn = findViewById(R.id.btn_sure);
        mCancelBtn = findViewById(R.id.btn_cancel);
        mInputEdit = findViewById(R.id.et_input);

        if (!TextUtils.isEmpty(mTitle)) {
            mTitleText.setText(mTitle);
        }

        if (!TextUtils.isEmpty(mContent)) {
            mInputEdit.setHint(mContent);
        }

        mSureBtn.setOnClickListener(view -> {
            if (mMyOnclickListener != null) {
                String string = mInputEdit.getText().toString().trim();
                mMyOnclickListener.onSure(InputDialog.this, string);
            }
        });

        mCancelBtn.setOnClickListener(view -> {
            if (mMyOnclickListener != null) {
                mMyOnclickListener.onCancel(InputDialog.this);
            }
        });
    }

    public interface myOnclickListener {
        void onSure(InputDialog dialog, String content);

        void onCancel(InputDialog dialog);
    }

    public static class Builder {
        private String mTitle;
        private String mContent;
        private myOnclickListener mMyOnclickListener;
        private Context mContext;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setListener(myOnclickListener myOnclickListener) {
            this.mMyOnclickListener = myOnclickListener;
            return this;
        }

        public InputDialog build() {
            return new InputDialog(this);
        }

    }
}
