package com.zyascend.JLUZone.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COURSE".
*/
public class CourseDao extends AbstractDao<Course, Long> {

    public static final String TABLENAME = "COURSE";

    /**
     * Properties of entity Course.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Place = new Property(2, String.class, "place", false, "PLACE");
        public final static Property BeginWeek = new Property(3, int.class, "beginWeek", false, "BEGIN_WEEK");
        public final static Property EndWeek = new Property(4, int.class, "endWeek", false, "END_WEEK");
        public final static Property DayOfWeek = new Property(5, int.class, "dayOfWeek", false, "DAY_OF_WEEK");
        public final static Property Time = new Property(6, String.class, "time", false, "TIME");
        public final static Property Teacher = new Property(7, String.class, "teacher", false, "TEACHER");
        public final static Property TermId = new Property(8, int.class, "termId", false, "TERM_ID");
    }


    public CourseDao(DaoConfig config) {
        super(config);
    }
    
    public CourseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COURSE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"PLACE\" TEXT," + // 2: place
                "\"BEGIN_WEEK\" INTEGER NOT NULL ," + // 3: beginWeek
                "\"END_WEEK\" INTEGER NOT NULL ," + // 4: endWeek
                "\"DAY_OF_WEEK\" INTEGER NOT NULL ," + // 5: dayOfWeek
                "\"TIME\" TEXT," + // 6: time
                "\"TEACHER\" TEXT," + // 7: teacher
                "\"TERM_ID\" INTEGER NOT NULL );"); // 8: termId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COURSE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Course entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String place = entity.getPlace();
        if (place != null) {
            stmt.bindString(3, place);
        }
        stmt.bindLong(4, entity.getBeginWeek());
        stmt.bindLong(5, entity.getEndWeek());
        stmt.bindLong(6, entity.getDayOfWeek());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
 
        String teacher = entity.getTeacher();
        if (teacher != null) {
            stmt.bindString(8, teacher);
        }
        stmt.bindLong(9, entity.getTermId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Course entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String place = entity.getPlace();
        if (place != null) {
            stmt.bindString(3, place);
        }
        stmt.bindLong(4, entity.getBeginWeek());
        stmt.bindLong(5, entity.getEndWeek());
        stmt.bindLong(6, entity.getDayOfWeek());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
 
        String teacher = entity.getTeacher();
        if (teacher != null) {
            stmt.bindString(8, teacher);
        }
        stmt.bindLong(9, entity.getTermId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Course readEntity(Cursor cursor, int offset) {
        Course entity = new Course( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // place
            cursor.getInt(offset + 3), // beginWeek
            cursor.getInt(offset + 4), // endWeek
            cursor.getInt(offset + 5), // dayOfWeek
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // time
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // teacher
            cursor.getInt(offset + 8) // termId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Course entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPlace(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBeginWeek(cursor.getInt(offset + 3));
        entity.setEndWeek(cursor.getInt(offset + 4));
        entity.setDayOfWeek(cursor.getInt(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTeacher(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTermId(cursor.getInt(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Course entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Course entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Course entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
