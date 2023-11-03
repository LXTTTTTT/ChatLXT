package com.example.chatlxt.Entity.DaoBean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.example.chatlxt.Dao.DaoSession;
import com.example.chatlxt.Dao.MessageDao;
import com.example.chatlxt.Dao.ChatDao;

// 聊天
@Entity
public class Chat {

    @Id(autoincrement = true)  // 设置为主键，autoincrement = true - 自增
    private Long id;

    public Long createTime;  // 创建时间
    @ToOne
    public Message firstQuestion;  // 首条提问
    @ToOne
    public Message firstAnswer;  // 首条回答

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1596497024)
    private transient ChatDao myDao;
    @Generated(hash = 395529260)
    public Chat(Long id, Long createTime) {
        this.id = id;
        this.createTime = createTime;
    }
    @Generated(hash = 519536279)
    public Chat() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    @Generated(hash = 1403329283)
    private transient boolean firstQuestion__refreshed;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 454602761)
    public Message getFirstQuestion() {
        if (firstQuestion != null || !firstQuestion__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            targetDao.refresh(firstQuestion);
            firstQuestion__refreshed = true;
        }
        return firstQuestion;
    }
    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 2071173072)
    public Message peakFirstQuestion() {
        return firstQuestion;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 725208744)
    public void setFirstQuestion(Message firstQuestion) {
        synchronized (this) {
            this.firstQuestion = firstQuestion;
            firstQuestion__refreshed = true;
        }
    }
    @Generated(hash = 1516739579)
    private transient boolean firstAnswer__refreshed;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1807369303)
    public Message getFirstAnswer() {
        if (firstAnswer != null || !firstAnswer__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            targetDao.refresh(firstAnswer);
            firstAnswer__refreshed = true;
        }
        return firstAnswer;
    }
    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 436002723)
    public Message peakFirstAnswer() {
        return firstAnswer;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1938748525)
    public void setFirstAnswer(Message firstAnswer) {
        synchronized (this) {
            this.firstAnswer = firstAnswer;
            firstAnswer__refreshed = true;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1004576325)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChatDao() : null;
    }

}
