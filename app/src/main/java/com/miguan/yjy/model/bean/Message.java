package com.miguan.yjy.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Copyright (c) 2016/12/26. LiaoPeiKun Inc. All rights reserved.
 */

public class Message implements Parcelable {

    private int id;

    private int relation_id;

    private int askid;

    private String user_name;

    private String content;

    private long created_at;

    private String img;

    private int type;

    private int otype;

    private int product_id;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public int getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(int relation_id) {
        this.relation_id = relation_id;
    }

    public int getAskid() {
        return askid;
    }

    public void setAskid(int askid) {
        this.askid = askid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(created_at * 1000);
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOtype() {
        return otype;
    }

    public void setOtype(int otype) {
        this.otype = otype;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.relation_id);
        dest.writeInt(this.askid);
        dest.writeString(this.user_name);
        dest.writeString(this.content);
        dest.writeLong(this.created_at);
        dest.writeString(this.img);
        dest.writeInt(this.type);
        dest.writeInt(this.otype);
        dest.writeInt(this.product_id);
    }

    protected Message(Parcel in) {
        this.id = in.readInt();
        this.relation_id = in.readInt();
        this.askid = in.readInt();
        this.user_name = in.readString();
        this.content = in.readString();
        this.created_at = in.readLong();
        this.img = in.readString();
        this.type = in.readInt();
        this.otype = in.readInt();
        this.product_id = in.readInt();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
