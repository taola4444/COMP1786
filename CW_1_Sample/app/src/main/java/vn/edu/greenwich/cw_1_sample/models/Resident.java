package vn.edu.greenwich.cw_1_sample.models;

import java.io.Serializable;

public class Resident implements Serializable {
    protected long _id;
    protected String _name;
    protected String _startDate;
    protected int _owner;
    protected String _destination;
    protected String _description;
    protected String _vehicle;
    protected String _quality;
    protected String _advice;

    public Resident() {
        _id = -1;
        _name = null;
        _startDate = null;
        _owner = -1;
        _destination = null;
        _description = null;
        _vehicle = null;
        _quality = null;
        _advice = null;
    }

    public Resident(long id, String name, String destination, String description, String startDate, int owner, String vehicle, String quality, String advice) {
        _id = id;
        _name = name;
        _startDate = startDate;
        _owner = owner;
        _description = description;
        _destination = destination;
        _vehicle = vehicle;
        _quality = quality;
        _advice = advice;
    }

    public long getId() { return _id; }
    public void setId(long id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }

    public String getStartDate() {
        return _startDate;
    }
    public void setStartDate(String startDate) {
        _startDate = startDate;
    }

    public int getOwner() {
        return _owner;
    }
    public void setOwner(int owner) {
        _owner = owner;
    }

    public String getDestination() {
        return _destination;
    }
    public void setDestination(String destination) {
        _destination = destination;
    }

    public String getDescription() {
        return _description;
    }
    public void setDescrtiption(String description) {
        _description = description;
    }

    public String getVehicle() {
        return _vehicle;
    }

    public void setVehicle(String _vehicle) {
        this._vehicle = _vehicle;
    }

    public String getQuality() {
        return _quality;
    }

    public void setQuality(String _quality) {
        this._quality = _quality;
    }

    public String getAdvice() {
        return _advice;
    }

    public void setAdvice(String _advice) {
        this._advice = _advice;
    }

    public boolean isEmpty() {
        if (-1 == _id && null == _name && null == _startDate && -1 == _owner && null == _description && null == _destination && null == _advice && null == _vehicle && null == _quality)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _startDate + "] " + _name;
    }
}