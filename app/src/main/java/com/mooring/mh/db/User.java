package com.mooring.mh.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 本地用户类
 * <p>
 * Created by Will on 16/3/31.
 */
@Table(name = "local_user")
public class User {

    @Column(isId = true, name = "id")
    private int id;

    @Column(name = "_name")
    private String _name;

    @Column(name = "_sex")
    private int _sex; //0:male  1:female

    @Column(name = "_birthday")
    private String _birthday; // 2016-03-31

    @Column(name = "_height")
    private String _height; // 170cm--170inch

    @Column(name = "_weight")
    private String _weight; // 64kg--64ib

    @Column(name = "_location")
    private int _location; // 0:out  1:left  2:right

    @Column(name = "_header")
    private String _header;//成员头像

    @Column(name = "_platformId")
    private String _platformId;//平台ID

    public String get_birthday() {
        return _birthday;
    }

    public String get_height() {
        return _height;
    }

    public int get_location() {
        return _location;
    }

    public String get_name() {
        return _name;
    }

    public int get_sex() {
        return _sex;
    }

    public String get_weight() {
        return _weight;
    }

    public int getId() {
        return id;
    }

    public void set_birthday(String _birthday) {
        this._birthday = _birthday;
    }

    public void set_height(String _height) {
        this._height = _height;
    }

    public void set_location(int _location) {
        this._location = _location;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_sex(int _sex) {
        this._sex = _sex;
    }

    public void set_weight(String _weight) {
        this._weight = _weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_header() {
        return _header;
    }

    public void set_header(String _header) {
        this._header = _header;
    }

    public String get_platformId() {
        return _platformId;
    }

    public void set_platformId(String _platformId) {
        this._platformId = _platformId;
    }

    public String toString() {
        return "id:  " + id + "  name:  " + _name + "  height:  " + _height + "  weight:  "
                + _weight + "  sex:  " + _sex + "  header:  " + _header + "  location:  "
                + _location + "  platformId:  " + _platformId;
    }
}
