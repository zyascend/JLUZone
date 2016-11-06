package com.zyascend.JLUZone.login;

import com.zyascend.JLUZone.base.BaseView;
import com.zyascend.JLUZone.entity.StuInfo;

/**
 *
 * Created by Administrator on 2016/7/6.
 */
public interface LoginContract {
    interface Presenter {
        void saveStuInfo(StuInfo stuInfo);
        void getStuInfo();
        void login(StuInfo stuInfo);
        void cancel();
    }

    interface View {
        void loadStuInfo(StuInfo stuInfo);
        void showLoginSuccess();
        void showLoginFailure();
        void showLoginProgress();
        void dismissLoginProgress();
    }
}
