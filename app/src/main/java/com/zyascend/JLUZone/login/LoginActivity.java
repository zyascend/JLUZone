package com.zyascend.JLUZone.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zyascend.JLUZone.entity.StuInfo;
import com.zyascend.JLUZone.main.MainActivity;
import com.zyascend.JLUZone.user.UserPresenter;
import com.zyascend.JLUZone.utils.ActivityUtils;

import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * Created by Administrator on 2016/7/6.
 */
public class LoginActivity extends BaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {

    private static final String TAG = "TAG_LoginActivity";
    @Bind(R.id.id_tv_welcome)
    TextView TvWelcome;
    @Bind(R.id.id_et_user)
    EditText EtUser;
    @Bind(R.id.id_et_passWord)
    EditText EtPassWord;
    @Bind(R.id.id_cb_rememberPassWord)
    CheckBox CbRememberPassWord;
    @Bind(R.id.id_cb_antoLogin)
    CheckBox CbAntoLogin;
    @Bind(R.id.id_cb_outside)
    CheckBox CbOutside;
    @Bind(R.id.id_btn_login)
    Button BtnLogin;
    @Bind(R.id.btn_over_login)
    Button overLogin;

    private ProgressDialog mProgressDialog;


    @Override
    protected void doOnCreate() {

        String type = getIntent().getStringExtra(UserPresenter.INTENT_LOGIN_TYPE);
        if (!TextUtils.equals(type, UserPresenter.TYPE_OUT)){
            mPresenter.getStuInfo();
        }

    }

    @Override
    protected void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;

    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void loadFragment() {

    }

    @OnClick({R.id.id_btn_login,R.id.btn_over_login})
    public void onClick(View view) {

       switch (view.getId()){
           case R.id.id_btn_login:

               String name = EtUser.getText().toString();
               String passWord = EtPassWord.getText().toString();
               boolean isAuto = CbAntoLogin.isChecked();
               boolean isRem = CbRememberPassWord.isChecked();
               boolean isOut = CbOutside.isChecked();

               mPresenter.login(new StuInfo(1,0,0,name,"",passWord,isAuto,isRem,isOut));
               break;
           case R.id.btn_over_login:
               ActivityUtils.enterActivity(this,MainActivity.class);
               break;
       }

    }

    @Override
    public void loadStuInfo(StuInfo stuInfo) {
        if (stuInfo != null){
            Log.d(TAG, "loadStuInfo: "+ stuInfo.getAccount()+stuInfo.passWord);
            EtUser.setText(stuInfo.getAccount());
            EtPassWord.setText(stuInfo.getPassWord());
            CbAntoLogin.setChecked(stuInfo.getIsAutoLogin());
            CbOutside.setChecked(stuInfo.getIsLoginOutside());
            CbRememberPassWord.setChecked(stuInfo.getIsRememberPass());
            //下一次打开自动登录
//            if (stuInfo.getIsAutoLogin()){
//                mPresenter.login(stuInfo);
//            }
        }

    }

    @Override
    public void showLoginSuccess() {
        dismissLoginProgress();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void showLoginFailure() {
        Toast.makeText(mContext, "登录失败，请检查网络或帐号密码", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginProgress() {

        mProgressDialog.setMessage("登录中...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    @Override
    public void dismissLoginProgress() {
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }


}
