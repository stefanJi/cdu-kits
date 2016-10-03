package cn.youcute.library.activities.menuFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/28.
 * 反馈
 */

public class FragFeedBack extends Fragment implements NetRequest.FeedBackCallBack {
    private View rootView;
    private EditText etContent, etContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
            initView();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    private void initView() {
        etContent = (EditText) rootView.findViewById(R.id.et_content_feed);
        etContact = (EditText) rootView.findViewById(R.id.et_contact_feed);
        etContact.setSingleLine();
        Button btnFeedBack = (Button) rootView.findViewById(R.id.btn_feedback);
        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etContent.getText().toString().equals("")) {
                    AppControl.getInstance().showToast("反馈内容不能为空哦");
                    return;
                }
                AppControl.getInstance().getNetRequest().feedBack(etContent.getText().toString(),
                        etContact.getText().toString(), FragFeedBack.this);
                showDialog();
            }
        });
    }

    @Override
    public void feedBackSuccess() {
        showSuccessDialog();
    }

    @Override
    public void feedBackError(String error) {
        showFailedDialog(error);
    }

    private SweetAlertDialog dialog;

    private void showDialog() {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText("反馈中...");
        dialog.setContentText("正在上传反馈哟");
        dialog.show();
    }

    private void showSuccessDialog() {
        if (dialog == null) {
            dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        } else {
            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        }
        dialog.setTitleText("OK");
        dialog.setContentText("谢谢反馈");
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
                getActivity().finish();
            }
        });
    }

    private void showFailedDialog(String info) {
        if (dialog == null) {
            dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        } else {
            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        }
        dialog.setTitleText("失败");
        dialog.setContentText(info);
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });


    }


}
