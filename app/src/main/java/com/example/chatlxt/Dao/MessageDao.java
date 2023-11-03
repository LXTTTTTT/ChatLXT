package com.example.chatlxt.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.chatlxt.Entity.DaoBean.Message;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE".
*/
public class MessageDao extends AbstractDao<Message, Long> {

    public static final String TABLENAME = "MESSAGE";

    /**
     * Properties of entity Message.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CreateTime = new Property(1, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Role = new Property(3, String.class, "role", false, "ROLE");
        public final static Property Belong = new Property(4, Long.class, "belong", false, "BELONG");
        public final static Property Answer = new Property(5, Long.class, "answer", false, "ANSWER");
        public final static Property Status = new Property(6, int.class, "status", false, "STATUS");
    }


    public MessageDao(DaoConfig config) {
        super(config);
    }
    
    public MessageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CREATE_TIME\" INTEGER," + // 1: createTime
                "\"CONTENT\" TEXT," + // 2: content
                "\"ROLE\" TEXT," + // 3: role
                "\"BELONG\" INTEGER," + // 4: belong
                "\"ANSWER\" INTEGER," + // 5: answer
                "\"STATUS\" INTEGER NOT NULL );"); // 6: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Message entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(2, createTime);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String role = entity.getRole();
        if (role != null) {
            stmt.bindString(4, role);
        }
 
        Long belong = entity.getBelong();
        if (belong != null) {
            stmt.bindLong(5, belong);
        }
 
        Long answer = entity.getAnswer();
        if (answer != null) {
            stmt.bindLong(6, answer);
        }
        stmt.bindLong(7, entity.getStatus());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Message entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(2, createTime);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String role = entity.getRole();
        if (role != null) {
            stmt.bindString(4, role);
        }
 
        Long belong = entity.getBelong();
        if (belong != null) {
            stmt.bindLong(5, belong);
        }
 
        Long answer = entity.getAnswer();
        if (answer != null) {
            stmt.bindLong(6, answer);
        }
        stmt.bindLong(7, entity.getStatus());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Message readEntity(Cursor cursor, int offset) {
        Message entity = new Message( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // createTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // role
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // belong
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // answer
            cursor.getInt(offset + 6) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Message entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreateTime(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRole(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBelong(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setAnswer(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setStatus(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Message entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Message entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Message entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
