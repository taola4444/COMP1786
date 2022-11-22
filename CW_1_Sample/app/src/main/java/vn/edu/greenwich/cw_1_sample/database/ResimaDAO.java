package vn.edu.greenwich.cw_1_sample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import vn.edu.greenwich.cw_1_sample.models.Request;
import vn.edu.greenwich.cw_1_sample.models.Resident;

public class ResimaDAO {
    protected ResimaDbHelper resimaDbHelper;
    protected SQLiteDatabase dbWrite, dbRead;

    public ResimaDAO(Context context) {
        resimaDbHelper = new ResimaDbHelper(context);

        dbRead = resimaDbHelper.getReadableDatabase();
        dbWrite = resimaDbHelper.getWritableDatabase();
    }

    public void close() {
        dbRead.close();
        dbWrite.close();
    }

    public void reset() {
        resimaDbHelper.onUpgrade(dbWrite, 0, 0);
    }

    // Resident.

    public long insertResident(Resident resident) {
        ContentValues values = getResidentValues(resident);

        return dbWrite.insert(ResidentEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Resident> getResidentList(Resident resident, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (null != resident) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (resident.getName() != null && !resident.getName().trim().isEmpty()) {
                selection += " AND " + ResidentEntry.COL_NAME + " LIKE ?";
                conditionList.add("%" + resident.getName() + "%");
            }

            if (resident.getDestination() != null && !resident.getDestination().trim().isEmpty()) {
                selection += " AND " + ResidentEntry.COL_DESTINATION + " LIKE ?";
                conditionList.add("%" + resident.getDestination()  + "%");
            }

            if (resident.getDescription() != null && !resident.getDescription().trim().isEmpty()) {
                selection += " AND " + ResidentEntry.COL_DESCRIPTION + " = ?";
                conditionList.add("%" + resident.getDescription() + "%");
            }

            if (resident.getStartDate() != null && !resident.getStartDate().trim().isEmpty()) {
                selection += " AND " + ResidentEntry.COL_START_DATE + " = ?";
                conditionList.add(resident.getStartDate());
            }

            if (resident.getQuality() != null && !resident.getStartDate().trim().isEmpty()) {
                selection += " AND " + ResidentEntry.COL_QUALITY + " = ?";
                conditionList.add(String.valueOf(resident.getQuality()));
            }

            if (resident.getVehicle() != null && !resident.getVehicle().trim().isEmpty()) {
                selection += " AND " + ResidentEntry.COL_VEHICLE + " = ?";
                conditionList.add(resident.getVehicle());
            }

            if (resident.getAdvice() != null && !resident.getAdvice().trim().isEmpty()) {
                selection += " AND " + ResidentEntry.COL_ADVICE + " = ?";
                conditionList.add(resident.getAdvice());
            }

            if (resident.getOwner() != -1) {
                selection += " AND " + ResidentEntry.COL_OWNER + " = ?";
                conditionList.add(String.valueOf(resident.getOwner()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getResidentFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Resident getResidentById(long id) {
        String selection = ResidentEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getResidentFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateResident(Resident resident) {
        ContentValues values = getResidentValues(resident);

        String selection = ResidentEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(resident.getId())};

        return dbWrite.update(ResidentEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteResident(long id) {
        String selection = ResidentEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(ResidentEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";

        return orderByColumn.trim();
    }

    protected ContentValues getResidentValues(Resident resident) {
        ContentValues values = new ContentValues();

        values.put(ResidentEntry.COL_NAME, resident.getName());
        values.put(ResidentEntry.COL_DESTINATION, resident.getDestination());
        values.put(ResidentEntry.COL_DESCRIPTION, resident.getDescription());
        values.put(ResidentEntry.COL_START_DATE, resident.getStartDate());
        values.put(ResidentEntry.COL_OWNER, resident.getOwner());
        values.put(ResidentEntry.COL_QUALITY, resident.getQuality());
        values.put(ResidentEntry.COL_VEHICLE, resident.getVehicle());
        values.put(ResidentEntry.COL_ADVICE, resident.getAdvice());

        return values;
    }

    protected ArrayList<Resident> getResidentFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Resident> list = new ArrayList<>();

        Cursor cursor = dbRead.query(ResidentEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Resident residentItem = new Resident();

            residentItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ResidentEntry.COL_ID)));
            residentItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(ResidentEntry.COL_NAME)));
            residentItem.setDescrtiption(cursor.getString(cursor.getColumnIndexOrThrow(ResidentEntry.COL_DESCRIPTION)));
            residentItem.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(ResidentEntry.COL_DESTINATION)));
            residentItem.setOwner(cursor.getInt(cursor.getColumnIndexOrThrow(ResidentEntry.COL_OWNER)));
            residentItem.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(ResidentEntry.COL_START_DATE)));
            residentItem.setQuality(cursor.getString(cursor.getColumnIndexOrThrow(ResidentEntry.COL_QUALITY)));
            residentItem.setVehicle(cursor.getString(cursor.getColumnIndexOrThrow(ResidentEntry.COL_VEHICLE)));
            residentItem.setAdvice(cursor.getString(cursor.getColumnIndexOrThrow(ResidentEntry.COL_ADVICE)));

            list.add(residentItem);
        }

        cursor.close();

        return list;
    }

    // Request.

    public long insertRequest(Request request) {
        ContentValues values = getRequestValues(request);

        return dbWrite.insert(RequestEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Request> getRequestList(Request request, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (request != null) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_CONTENT + " LIKE ?";
                conditionList.add("%" + request.getContent() + "%");
            }

            if (request.getDate() != null && !request.getDate().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_DATE + " = ?";
                conditionList.add(request.getDate());
            }

            if (request.getTime() != null && !request.getTime().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_TIME + " = ?";
                conditionList.add(request.getTime());
            }

            if (request.getType() != null && !request.getType().trim().isEmpty()) {
                selection += " AND " + RequestEntry.COL_TYPE + " = ?";
                conditionList.add(request.getType());
            }

            if (request.getPrice() != 0) {
                selection += " AND " + RequestEntry.COL_PRICE + " = ?";
                conditionList.add(String.valueOf(request.getPrice()));
            }

            if (request.getAmount() != 0) {
                selection += " AND " + RequestEntry.COL_AMOUNT + " = ?";
                conditionList.add(String.valueOf(request.getAmount()));
            }

            if (request.getResidentId() != -1) {
                selection += " AND " + RequestEntry.COL_RESIDENT_ID + " = ?";
                conditionList.add(String.valueOf(request.getResidentId()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getRequestFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Request getRequestById(long id) {
        String selection = RequestEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getRequestFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateRequest(Request request) {
        ContentValues values = getRequestValues(request);

        String selection = RequestEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(request.getId())};

        return dbWrite.update(RequestEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteRequest(long id) {
        String selection = RequestEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(RequestEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected ContentValues getRequestValues(Request request) {
        ContentValues values = new ContentValues();

        values.put(RequestEntry.COL_CONTENT, request.getContent());
        values.put(RequestEntry.COL_DATE, request.getDate());
        values.put(RequestEntry.COL_TIME, request.getTime());
        values.put(RequestEntry.COL_TYPE, request.getType());
        values.put(RequestEntry.COL_PRICE, request.getPrice());
        values.put(RequestEntry.COL_AMOUNT, request.getAmount());
        values.put(RequestEntry.COL_RESIDENT_ID, request.getResidentId());

        return values;
    }

    protected ArrayList<Request> getRequestFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Request> list = new ArrayList<>();

        Cursor cursor = dbRead.query(RequestEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Request requestItem = new Request();

            requestItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RequestEntry.COL_ID)));
            requestItem.setContent(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_CONTENT)));
            requestItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_DATE)));
            requestItem.setTime(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_TIME)));
            requestItem.setType(cursor.getString(cursor.getColumnIndexOrThrow(RequestEntry.COL_TYPE)));
            requestItem.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(RequestEntry.COL_PRICE)));
            requestItem.setAmount(cursor.getInt(cursor.getColumnIndexOrThrow(RequestEntry.COL_AMOUNT)));
            requestItem.setResidentId(cursor.getLong(cursor.getColumnIndexOrThrow(RequestEntry.COL_RESIDENT_ID)));

            list.add(requestItem);
        }

        cursor.close();

        return list;
    }
}