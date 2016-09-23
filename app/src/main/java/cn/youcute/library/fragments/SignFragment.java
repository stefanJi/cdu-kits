package cn.youcute.library.fragments;

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
import cn.youcute.library.bean.User;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/9/23.
 * 登录
 */
public class SignFragment extends Fragment implements NetRequest.SignCallBack {
    private View rootView;
    private EditText etAccount, etPassword;
    private Button btnSign;
    private SpUtil spUtil;
    private String account, password;
    private SweetAlertDialog dialog;
    private SignFragmentCallBack callBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_sign, container, false);
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

    public void setCallBack(SignFragmentCallBack signFragmentCallBack) {
        this.callBack = signFragmentCallBack;
    }

    private void initView() {
        spUtil = AppControl.getInstance().getSpUtil();
        etAccount = (EditText) rootView.findViewById(R.id.et_accout);
        etPassword = (EditText) rootView.findViewById(R.id.et_password);
        btnSign = (Button) rootView.findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = etAccount.getText().toString();
                password = etPassword.getText().toString();
                if (account.equals("") || password.equals("")) {
                    AppControl.getInstance().showToast("输入有误");
                    return;
                }
                sign();
            }
        });
        //将本地保存的账户设置到输入框
        User user = spUtil.getUser();
        if (!user.account.equals("0")) {
            etAccount.setText(user.account);
            etPassword.setText(user.password);
        }
    }

    /**
     * 登录
     */
    private void sign() {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("登录中")
                .setContentText("别急，慢慢来");
        dialog.setCancelable(false);
        dialog.show();
        AppControl.getInstance().getNetRequest().sign(account, password, this);
    }

    @Override
    public void signSuccess(String session) {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText("登录成功");
        dialog.dismissWithAnimation();
        callBack.signOk(session);
    }

    @Override
    public void signFailed() {
        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("登录失败");
        dialog.setContentText("换个姿势，再来一次试试");
        dialog.setConfirmText("重试");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });
        btnSign.setVisibility(View.VISIBLE);
        callBack.signNo();
    }

    public interface SignFragmentCallBack {
        void signOk(String session);

        void signNo();
    }
}
