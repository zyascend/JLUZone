package com.zyascend.JLUZone.evaluate;

import com.zyascend.JLUZone.entity.EvaluateItem;
import com.zyascend.JLUZone.entity.EvaluateResult;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/12/5.
 */

public interface EvaluateContract {
    interface View {
        void onLoadList(List<EvaluateItem> list);
        void onEvaluated();
        void onFail();
    }
    interface Presenter{
        void loadEvaluateList();
        void evaluate(String itemId, int type,String advice);
    }
}
