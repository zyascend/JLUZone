package com.zyascend.JLUZone.entity;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Editor;
import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.entity.MainImage;
import com.zyascend.JLUZone.entity.News;
import com.zyascend.JLUZone.entity.Score;
import com.zyascend.JLUZone.entity.StuInfo;
import com.zyascend.JLUZone.entity.Term;
import com.zyascend.JLUZone.entity.Todo;

import com.zyascend.JLUZone.entity.CourseDao;
import com.zyascend.JLUZone.entity.EditorDao;
import com.zyascend.JLUZone.entity.JobDao;
import com.zyascend.JLUZone.entity.MainImageDao;
import com.zyascend.JLUZone.entity.NewsDao;
import com.zyascend.JLUZone.entity.ScoreDao;
import com.zyascend.JLUZone.entity.StuInfoDao;
import com.zyascend.JLUZone.entity.TermDao;
import com.zyascend.JLUZone.entity.TodoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig courseDaoConfig;
    private final DaoConfig editorDaoConfig;
    private final DaoConfig jobDaoConfig;
    private final DaoConfig mainImageDaoConfig;
    private final DaoConfig newsDaoConfig;
    private final DaoConfig scoreDaoConfig;
    private final DaoConfig stuInfoDaoConfig;
    private final DaoConfig termDaoConfig;
    private final DaoConfig todoDaoConfig;

    private final CourseDao courseDao;
    private final EditorDao editorDao;
    private final JobDao jobDao;
    private final MainImageDao mainImageDao;
    private final NewsDao newsDao;
    private final ScoreDao scoreDao;
    private final StuInfoDao stuInfoDao;
    private final TermDao termDao;
    private final TodoDao todoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        courseDaoConfig = daoConfigMap.get(CourseDao.class).clone();
        courseDaoConfig.initIdentityScope(type);

        editorDaoConfig = daoConfigMap.get(EditorDao.class).clone();
        editorDaoConfig.initIdentityScope(type);

        jobDaoConfig = daoConfigMap.get(JobDao.class).clone();
        jobDaoConfig.initIdentityScope(type);

        mainImageDaoConfig = daoConfigMap.get(MainImageDao.class).clone();
        mainImageDaoConfig.initIdentityScope(type);

        newsDaoConfig = daoConfigMap.get(NewsDao.class).clone();
        newsDaoConfig.initIdentityScope(type);

        scoreDaoConfig = daoConfigMap.get(ScoreDao.class).clone();
        scoreDaoConfig.initIdentityScope(type);

        stuInfoDaoConfig = daoConfigMap.get(StuInfoDao.class).clone();
        stuInfoDaoConfig.initIdentityScope(type);

        termDaoConfig = daoConfigMap.get(TermDao.class).clone();
        termDaoConfig.initIdentityScope(type);

        todoDaoConfig = daoConfigMap.get(TodoDao.class).clone();
        todoDaoConfig.initIdentityScope(type);

        courseDao = new CourseDao(courseDaoConfig, this);
        editorDao = new EditorDao(editorDaoConfig, this);
        jobDao = new JobDao(jobDaoConfig, this);
        mainImageDao = new MainImageDao(mainImageDaoConfig, this);
        newsDao = new NewsDao(newsDaoConfig, this);
        scoreDao = new ScoreDao(scoreDaoConfig, this);
        stuInfoDao = new StuInfoDao(stuInfoDaoConfig, this);
        termDao = new TermDao(termDaoConfig, this);
        todoDao = new TodoDao(todoDaoConfig, this);

        registerDao(Course.class, courseDao);
        registerDao(Editor.class, editorDao);
        registerDao(Job.class, jobDao);
        registerDao(MainImage.class, mainImageDao);
        registerDao(News.class, newsDao);
        registerDao(Score.class, scoreDao);
        registerDao(StuInfo.class, stuInfoDao);
        registerDao(Term.class, termDao);
        registerDao(Todo.class, todoDao);
    }
    
    public void clear() {
        courseDaoConfig.clearIdentityScope();
        editorDaoConfig.clearIdentityScope();
        jobDaoConfig.clearIdentityScope();
        mainImageDaoConfig.clearIdentityScope();
        newsDaoConfig.clearIdentityScope();
        scoreDaoConfig.clearIdentityScope();
        stuInfoDaoConfig.clearIdentityScope();
        termDaoConfig.clearIdentityScope();
        todoDaoConfig.clearIdentityScope();
    }

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public EditorDao getEditorDao() {
        return editorDao;
    }

    public JobDao getJobDao() {
        return jobDao;
    }

    public MainImageDao getMainImageDao() {
        return mainImageDao;
    }

    public NewsDao getNewsDao() {
        return newsDao;
    }

    public ScoreDao getScoreDao() {
        return scoreDao;
    }

    public StuInfoDao getStuInfoDao() {
        return stuInfoDao;
    }

    public TermDao getTermDao() {
        return termDao;
    }

    public TodoDao getTodoDao() {
        return todoDao;
    }

}
