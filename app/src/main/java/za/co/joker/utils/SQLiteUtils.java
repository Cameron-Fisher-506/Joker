package za.co.joker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteUtils extends SQLiteOpenHelper
{
    
    private static final String DB_NAME = "Joker.db";
    private static final int DB_VERSION = 1;
    
    //TABLES
    private static final String RESPONSE_TABLE = "response";
    
    public SQLiteUtils(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createResponseTable();
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    
    }

    private void createResponseTable()
    {
        SQLiteDatabase dbWrite = null;
        
        try
        {
            String request = "CREATE TABLE IF NOT EXISTS " +RESPONSE_TABLE+"(" +
                                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                     "url VARCHAR(1000)," +
                                     "body VARCHAR(8000)," +
                                     "response VARCHAR(8000)," +
                                     "createdTime VARCHAR(50)," +
                                     "updateFlag INTEGER DEFAULT 0" +
                                     ")";
            
            dbWrite = getWritableDatabase();
            
            if(dbWrite != null)
            {
                dbWrite.execSQL(request);
            }
            
        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                                     "\nClass: SQLiteUtils" +
                                     "\nMethod: createResponseTable" +
                                     "\nCreatedTime: " + DTUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }finally
        {
            if(dbWrite != null)
            {
                dbWrite.close();
                dbWrite = null;
            }
        }
    }

    public void cacheResponse(String url, String response, String createdTime)
    {
        createResponseTable();
        
        SQLiteDatabase dbRead = null;
        SQLiteDatabase dbWrite = null;
        Cursor cursor = null;
        try
        {
            
            dbRead = this.getReadableDatabase();
            dbWrite = this.getWritableDatabase();
            
            if(dbRead != null && dbWrite != null)
            {
                //check if a response for a key already exists
                String query = "SELECT *" +
                        " FROM " + RESPONSE_TABLE +
                        " WHERE url = \"" + url + "\";";
                
                cursor = dbRead.rawQuery(query, null, null);
                if(cursor != null)
                {
                    if(cursor.moveToFirst())
                    {
                        //update the existing response
                        int id = cursor.getInt(0);
                        
                        ContentValues contentValues = new ContentValues();

                        contentValues.put("url", url);
                        contentValues.put("response", response);
                        contentValues.put("createdTime", createdTime);
                        contentValues.put("updateFlag", 0);
                        
                        dbWrite.update(RESPONSE_TABLE, contentValues, "id = " + id, null);
                        
                        return;
                    }
                }
                
                ContentValues contentValues = new ContentValues();
                
                contentValues.put("url", url);
                contentValues.put("response", response);
                contentValues.put("createdTime", createdTime);
                
                dbWrite.insert(RESPONSE_TABLE, null,contentValues);
            }
            
            
        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                                     "\nClass: SQLiteUtils" +
                                     "\nMethod: cacheResponse" +
                                     "\nURL: " + url +
                                     "\nresponse: " + response +
                                     "\ntimestamp: " + createdTime +
                                     "\nCreatedTime: " + DTUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }finally
        {
            if(dbRead != null)
            {
                dbRead.close();
                dbRead = null;
            }
            
            if(dbWrite != null)
            {
                dbWrite.close();
                dbWrite = null;
            }
            
            if(cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }
    
    public String getResponse(String url)
    {
        String toReturn = null;
        
        createResponseTable();
        
        SQLiteDatabase dbRead = null;
        SQLiteDatabase dbWrite = null;
        Cursor cursor = null;
        
        try
        {
            dbRead = this.getReadableDatabase();
            dbWrite = this.getWritableDatabase();
            
            String query = "SELECT *" +
                " FROM " + RESPONSE_TABLE +
                " WHERE url = \"" + url + "\";";

            cursor = dbRead.rawQuery(query, null, null);
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    int id = cursor.getInt(0);
                    toReturn = cursor.getString(3);
                    
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("updateFlag", 1);
                    dbWrite.update(RESPONSE_TABLE, contentValues, "id = " + id, null);
                }
            }
            
        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "\n\nError Message: " + e.getMessage() +
                                              "\nClass: SQLiteUtils" +
                                              "\nMethod: getResponse" +
                                              "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }finally {
            if(dbWrite != null)
            {
                dbWrite.close();
                dbWrite = null;
            }
            
            if(dbRead != null)
            {
                dbRead.close();
                dbRead = null;
            }
            
            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }
        
        return toReturn;
    }
}