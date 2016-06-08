package com.mooring.mh.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;

/**
 * 自定义可动态设置显示message和图片的dialog
 * <p/>
 * 设置positive和negative才会显示按钮
 * <p/>
 * Created by Will on 16/4/13.
 */
public class CommonDialog extends Dialog {

    public CommonDialog(Context context) {
        super(context);
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        private Context context;
        private String message; // message
        private DialogInterface.OnClickListener cancelClickListener;
        private DialogInterface.OnClickListener okClickListener;

        private int logoId; // 中间图标

        private boolean isCancel = false;  //是否有取消按钮
        private boolean isOk = false;  //是否有OK按钮
        private boolean isOtherPlace = false;//是否触摸其他地方消失dialog
        private boolean isTextView = false;//是否带有文字样式

        private ImageView imgView_cancel;
        private ImageView imgView_ok_middle;
        private ImageView imgView_ok;
        private TextView tv_ok;
        private TextView tv_cancel;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置触摸其他地方取消dialog
         *
         * @param isOtherPlace
         * @return
         */
        public Builder setCanceledOnTouchOtherPlace(boolean isOtherPlace) {
            this.isOtherPlace = isOtherPlace;
            return this;
        }

        /**
         * 设置中间图标资源
         *
         * @param resId
         * @return
         */
        public Builder setLogo(int resId) {
            this.logoId = resId;
            return this;
        }

        /**
         * 设置message文本
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置message文本
         *
         * @param resId
         * @return
         */
        public Builder setMessage(int resId) {
            this.message = (String) context.getText(resId);
            return this;
        }

        /**
         * 设定带有文本的dialog
         *
         * @param cancelListener 取消
         * @param okListener     重试
         * @return
         */
        public Builder setTextDialogListener(OnClickListener cancelListener, OnClickListener okListener) {
            this.isTextView = true;
            this.cancelClickListener = cancelListener;
            this.okClickListener = okListener;
            return this;
        }

        /**
         * 设置确认按钮和相应listener
         *
         * @param isOk
         * @param listener
         * @return
         */
        public Builder setPositiveButton(boolean isOk, DialogInterface.OnClickListener listener) {
            this.isOk = isOk;
            this.okClickListener = listener;
            return this;
        }

        /**
         * 设置取消按钮文本和监听
         *
         * @param isCancel
         * @param listener
         * @return
         */
        public Builder setNegativeButton(boolean isCancel, DialogInterface.OnClickListener listener) {
            this.isCancel = isCancel;
            this.cancelClickListener = listener;
            return this;
        }

        public CommonDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // instantiate the dialog with the custom Theme
            final CommonDialog dialog = new CommonDialog(context, R.style.CommonDialogStyle);

            View layout = inflater.inflate(R.layout.dialog_stop_heating, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            imgView_cancel = (ImageView) layout.findViewById(R.id.imgView_cancel);
            imgView_ok_middle = (ImageView) layout.findViewById(R.id.imgView_ok_middle);
            imgView_ok = (ImageView) layout.findViewById(R.id.imgView_ok);
            tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
            tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);

            if (!isOk && !isCancel) {
                imgView_cancel.setVisibility(View.GONE);
                imgView_ok_middle.setVisibility(View.GONE);
                imgView_ok.setVisibility(View.GONE);
                if (isTextView) {
                    tv_ok.setVisibility(View.VISIBLE);
                    tv_cancel.setVisibility(View.VISIBLE);
                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            cancelClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                    tv_ok.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            okClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else if (isOk && !isCancel) {
                imgView_cancel.setVisibility(View.GONE);
                imgView_ok_middle.setVisibility(View.VISIBLE);
                imgView_ok.setVisibility(View.GONE);

                imgView_ok_middle.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        okClickListener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
                    }
                });
            } else if (isOk && isCancel) {
                imgView_cancel.setVisibility(View.VISIBLE);
                imgView_ok_middle.setVisibility(View.GONE);
                imgView_ok.setVisibility(View.VISIBLE);

                imgView_ok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cancelClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });

                imgView_cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        okClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }

            if (isOtherPlace) {
                //设定触摸其他地方消失dialog
                layout.findViewById(R.id.dialog_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

            // 设置message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.tv_dialog_message)).setText(message);
            }

            if (logoId != 0) {
                ((ImageView) layout.findViewById(R.id.imgView_dialog_logo)).setImageResource(logoId);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
