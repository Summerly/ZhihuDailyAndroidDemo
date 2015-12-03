package com.example.pein.demo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.pein.demo.dao.STORY;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STORY".
*/
public class STORYDao extends AbstractDao<STORY, Long> {

    public static final String TABLENAME = "STORY";

    /**
     * Properties of entity STORY.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property StoryId = new Property(1, String.class, "storyId", false, "STORY_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Images = new Property(3, String.class, "images", false, "IMAGES");
    };


    public STORYDao(DaoConfig config) {
        super(config);
    }
    
    public STORYDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"STORY_ID\" TEXT NOT NULL ," + // 1: storyId
                "\"TITLE\" TEXT NOT NULL ," + // 2: title
                "\"IMAGES\" TEXT);"); // 3: images
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STORY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, STORY entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getStoryId());
        stmt.bindString(3, entity.getTitle());
 
        String images = entity.getImages();
        if (images != null) {
            stmt.bindString(4, images);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public STORY readEntity(Cursor cursor, int offset) {
        STORY entity = new STORY( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // storyId
            cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // images
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, STORY entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStoryId(cursor.getString(offset + 1));
        entity.setTitle(cursor.getString(offset + 2));
        entity.setImages(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(STORY entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(STORY entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
