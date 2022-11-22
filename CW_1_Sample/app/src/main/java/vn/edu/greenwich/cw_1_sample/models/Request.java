package vn.edu.greenwich.cw_1_sample.models;

import java.io.Serializable;

public class Request implements Serializable {
    protected long _id;
    protected String _content;
    protected String _date;
    protected String _time;
    protected String _type;
    protected long _residentId;
    protected int _price;
    protected int _amount;

    public Request() {
        _id = -1;
        _content = null;
        _date = null;
        _time = null;
        _type = null;
        _residentId = -1;
        _price = 0;
        _amount = 0;
    }

    public Request(long id, String content, String date, String time, String type, long residentId,int price,int amount) {
        _id = id;
        _content = content;
        _date = date;
        _time = time;
        _type = type;
        _residentId = residentId;
        _price = price;
        _amount = amount;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        _content = content;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String date) {
        _date = date;
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String time) {
        _time = time;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    public long getResidentId() {
        return _residentId;
    }

    public void setResidentId(long residentId) {
        _residentId = residentId;
    }

    public int getPrice() {
        return _price;
    }

    public void setPrice(int _price) {
        this._price = _price;
    }

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int _amount) {
        this._amount = _amount;
    }

    public boolean isEmpty() {
        if (-1 == _id && null == _content && null == _date && null == _time && null == _type && -1 == _residentId && 0 == _price && 0 == _amount)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _type + "][" + _date + "] " + _content;
    }
}