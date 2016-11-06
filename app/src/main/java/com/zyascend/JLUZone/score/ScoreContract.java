package com.zyascend.JLUZone.score;

import com.zyascend.JLUZone.entity.Score;
import com.zyascend.JLUZone.entity.ScoreDetail;
import com.zyascend.JLUZone.entity.Term;

import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface ScoreContract {

    interface Presenter{

        void getScore(int params,int type);
        void getScoreDetail(String asId);
        void sort(int type,List<Score> scores);
    }

    interface View {
        void loadScore(List<Score> scoreList);
        void showFailure();
        void loadScoreDetail(ScoreDetail detail);
        void onSorted(List<Score> scores);
    }
}
