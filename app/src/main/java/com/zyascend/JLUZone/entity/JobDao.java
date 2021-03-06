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
 * DAO for table "JOB".
*/
public class JobDao extends AbstractDao<Job, Long> {

    public static final String TABLENAME = "JOB";

    /**
     * Properties of entity Job.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Date = new Property(2, String.class, "date", false, "DATE");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
        public final static Property Hits = new Property(4, int.class, "hits", false, "HITS");
        public final static Property Type = new Property(5, String.class, "type", false, "TYPE");
    }


    public JobDao(DaoConfig config) {
        super(config);
    }
    
    public JobDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"JOB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"DATE\" TEXT," + // 2: date
                "\"ADDRESS\" TEXT," + // 3: address
                "\"HITS\" INTEGER NOT NULL ," + // 4: hits
                "\"TYPE\" TEXT);"); // 5: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"JOB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Job entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
        stmt.bindLong(5, entity.getHits());
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(6, type);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Job entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
        stmt.bindLong(5, entity.getHits());
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(6, type);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Job readEntity(Cursor cursor, int offset) {
        Job entity = new Job( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // address
            cursor.getInt(offset + 4), // hits
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Job entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setHits(cursor.getInt(offset + 4));
        entity.setType(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Job entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Job entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Job entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
