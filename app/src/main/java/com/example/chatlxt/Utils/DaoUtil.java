package com.example.chatlxt.Utils;

import android.content.Context;


import com.example.chatlxt.Dao.DaoMaster;
import com.example.chatlxt.Dao.DaoSession;
import com.example.chatlxt.Entity.DaoBean.Contact;
import com.example.chatlxt.Entity.DaoBean.Message;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

// 数据库使用工具
public class DaoUtil {

    private Context mContext;
    //创建数据库的名字
    private static final String DB_NAME = "ChatLXT.db";

    //它里边实际上是保存数据库的对象
    private static DaoMaster mDaoMaster;

    //创建数据库的工具
    private static DaoMaster.DevOpenHelper mHelper;

    //管理gen里生成的所有的Dao对象里边带有基本的增删改查的方法
    private static DaoSession mDaoSession;


    private DaoUtil() {
    }

// 单例 ------------------------------------
    //多线程中要被共享的使用volatile关键字修饰  GreenDao管理类
    private volatile static DaoUtil instance;
    public static DaoUtil getInstance() {
        if (instance == null) {
            synchronized (DaoUtil.class) {
                if (instance == null) {
                    instance = new DaoUtil();
                }
            }
        }
        return instance;
    }

// 初始化
    public void init(Context context) {
        this.mContext = context;
    }


// 判断是否有存在数据库，如果没有则创建
    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            mHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

// 完成对数据库的添加、删除、修改、查询操作，
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

// 获取所有联系人方法 --------------------------
    public static List<Contact> getContacts(){
        return getInstance().getDaoSession().getContactDao().loadAll();
    }
    public static List<Message> getMessages(){
        return getInstance().getDaoSession().getMessageDao().loadAll();
    }

// 联系人数据库查询指令器（查询联系人时用这个） ----------------------
    public static QueryBuilder getContactBuilder(){
        return getInstance().getDaoSession().getContactDao().queryBuilder();
    }
    public static QueryBuilder getMessageBuilder(){
        return getInstance().getDaoSession().getMessageDao().queryBuilder();
    }

// 关闭数据库：关闭所有的操作，数据库开启后，使用完毕要关闭 ---------
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    public void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }
}

/**
 * 1.插入
 * insert(User entity)： 插入一条记录, 当指定主键在表中存在时会发生异常
 * insertOrReplace(User entity) ： 当指定主键在表中存在时会覆盖数据,有该数据时则更新，推荐同步数据库时使用该方法
 * save(User entity):　save 类似于insertOrReplace，区别在于save会判断传入对象的key，有key的对象执行更新，无key的执行插入。当对象有key但并不在数据库时会执行失败.适用于保存本地列表。
 *
 * insertInTx(T... entities)：使用事务在数据库中插入给定的实体。
 * insertInTx(Iterable<T> entities)：使用事务操作，将给定的实体集合插入数据库。
 * insertInTx(Iterable<T> entities, boolean setPrimaryKey)：使用事务操作，将给定的实体集合插入数据库，并设置是否设定主键 。
 *
 * insertOrReplaceInTx(T... entities)：使用事务操作，将给定的实体插入数据库，若此实体类存在，则覆盖
 * insertOrReplaceInTx(Iterable<T> entities)：使用事务操作，将给定的实体插入数据库，若此实体类存在，则覆盖 。
 * insertOrReplaceInTx(Iterable<T> entities, boolean setPrimaryKey)：使用事务操作，将给定的实体插入数据库，若此实体类存在，则覆盖，并设置是否设定主键 。
 * insertWithoutSettingPk(T entity)：将给定的实体插入数据库,但不设定主键。
 *
 * // 新增数据插入相关API
 * save(T entity)：将给定的实体插入数据库
 * saveInTx(Iterable<T> entities)：将给定的实体集合插入数据库
 * saveInTx(T... entities)：使用事务操作，将给定的实体插入数据库
 */

/**
 * 2.查询
 * //查询全部
 * List<User> list = mUserDao.queryBuilder().list();
 *
 * //查询 name等于xyh8的数据
 * List<User> list= mUserDao.queryBuilder().where(UserDao.Properties.Name.eq("xyh8")).list();
 *
 * //查询 name不等于xyh8的数据
 * List<User> list= mUserDao.queryBuilder().where(UserDao.Properties.Name.notEq("xyh8")).list();
 *
 * //like  模糊查询
 * //查询 name以xyh3开头的数据
 * List<User> list = mUserDao.queryBuilder().where(UserDao.Properties.Name.like("xyh3%")).list();
 *
 * //between 区间查询 年龄在20到30之间
 *  List<User> list = mUserDao.queryBuilder().where(UserDao.Properties.Age.between(20,30)).list();
 *
 * //gt: greater than 半开区间查询，年龄大于18
 * List<User> list = mUserDao.queryBuilder().where(UserDao.Properties.Age.gt(18)).list();
 *
 * //ge: greater equal 半封闭区间查询，年龄大于或者等于18
 * List<User> list = mUserDao.queryBuilder().where(UserDao.Properties.Age.ge(18)).list();
 *
 * //lt: less than 半开区间查询，年龄小于18
 * List<User> list = mUserDao.queryBuilder().where(UserDao.Properties.Age.lt(18)).list();
 *
 * //le: less equal 半封闭区间查询，年龄小于或者等于18
 * List<User> list = mUserDao.queryBuilder().where(UserDao.Properties.Age.le(18)).list();
 */

/**
 * 3.排序
 * //名字以xyh8开头，年龄升序排序
 *  List<User> list = mUserDao.queryBuilder()
 *                 .where(UserDao.Properties.Name.like("xyh8%"))
 *                 .orderAsc(UserDao.Properties.Age)
 *                 .list();
 *
 * //名字以xyh8开头，年龄降序排序
 *  List<User> list = mUserDao.queryBuilder()
 *                 .where(UserDao.Properties.Name.like("xyh8%"))
 *                 .orderDesc(UserDao.Properties.Age)
 *                 .list();
 */

/**
 *  4.更新
 *  update(T entity) ：更新给定的实体
 *
 * updateInTx(Iterable<T> entities) ：使用事务操作，更新给定的实体
 *
 * updateInTx(T... entities)：使用事务操作，更新给定的实体
 *        studentDao.update(student);
 *         studentDao.updateInTx(student);
 */

/**
 * 5.删除
 * //删除全部
 *  mUserDao.deleteAll();
 *
 * delete(T entity)：从数据库中删除给定的实体
 *
 * deleteByKey(K key)：从数据库中删除给定Key所对应的实体
 *
 * deleteInTx(T... entities)：使用事务操作删除数据库中给定的实体
 *
 * deleteInTx(<T> entities)：使用事务操作删除数据库中给定实体集合中的实体
 *
 * deleteByKeyInTx(K... keys)：使用事务操作删除数据库中给定的所有key所对应的实体
 *
 * deleteByKeyInTx(Iterable<K> keys)：使用事务操作删除数据库中给定的所有key所对应的实体
 */