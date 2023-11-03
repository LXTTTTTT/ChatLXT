package com.example.chatlxt.Entity.DaoBean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Locale;

// 联系人实体类
@Entity
public class Contact {

    @Id(autoincrement = true)  // 设置为主键，autoincrement = true - 自增
    private Long id;

    public String name;  // 名字
    public String phone;  // 手机号码
    public String first_character;  // 首字母
    public boolean is_choose;  // 是否选中
    @Generated(hash = 1862291350)
    public Contact(Long id, String name, String phone, String first_character,
            boolean is_choose) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.first_character = first_character;
        this.is_choose = is_choose;
    }
    @Generated(hash = 672515148)
    public Contact() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFirst_character() {
        return this.first_character;
    }
    public void setFirst_character(String first_character) {
        this.first_character = first_character;
    }
    public boolean getIs_choose() {
        return this.is_choose;
    }
    public void setIs_choose(boolean is_choose) {
        this.is_choose = is_choose;
    }


}
