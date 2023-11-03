package com.example.chatlxt.Dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.chatlxt.Entity.DaoBean.Chat;
import com.example.chatlxt.Entity.DaoBean.Contact;
import com.example.chatlxt.Entity.DaoBean.Message;

import com.example.chatlxt.Dao.ChatDao;
import com.example.chatlxt.Dao.ContactDao;
import com.example.chatlxt.Dao.MessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatDaoConfig;
    private final DaoConfig contactDaoConfig;
    private final DaoConfig messageDaoConfig;

    private final ChatDao chatDao;
    private final ContactDao contactDao;
    private final MessageDao messageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatDaoConfig = daoConfigMap.get(ChatDao.class).clone();
        chatDaoConfig.initIdentityScope(type);

        contactDaoConfig = daoConfigMap.get(ContactDao.class).clone();
        contactDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        chatDao = new ChatDao(chatDaoConfig, this);
        contactDao = new ContactDao(contactDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);

        registerDao(Chat.class, chatDao);
        registerDao(Contact.class, contactDao);
        registerDao(Message.class, messageDao);
    }
    
    public void clear() {
        chatDaoConfig.clearIdentityScope();
        contactDaoConfig.clearIdentityScope();
        messageDaoConfig.clearIdentityScope();
    }

    public ChatDao getChatDao() {
        return chatDao;
    }

    public ContactDao getContactDao() {
        return contactDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

}
