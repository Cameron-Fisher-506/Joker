package za.co.joker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import za.co.joker.objs.Joke;

public class SQLiteUtils extends SQLiteOpenHelper
{
    
    private static final String DB_NAME = "Joker.db";
    private static final int DB_VERSION = 1;
    
    //TABLES
    private static final String RESPONSE_TABLE = "response";
    private static final String FAVOURITES_TABLE = "favourites";
    
    public SQLiteUtils(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createResponseTable();
        createFavouritesTable();
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

    private void createFavouritesTable()
    {
        SQLiteDatabase dbWrite = null;

        try
        {
            String request = "CREATE TABLE IF NOT EXISTS " +FAVOURITES_TABLE+"(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "value VARCHAR(1000)," +
                    "url VARCHAR(1000)," +
                    "updatedAt VARCHAR(1000)," +
                    "idJoke VARCHAR(1000)," +
                    "urlIcon VARCHAR(1000)," +
                    "createdAt VARCHAR(1000)" +
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
                    "\nMethod: createFavouritesTable" +
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

    public void cacheFavourites(String value, String url, String updatedAt, String id, String urlIcon, String createdAt)
    {
        createFavouritesTable();

        SQLiteDatabase dbWrite = null;
        try
        {
            dbWrite = this.getWritableDatabase();

            if(dbWrite != null && value != null && url != null && updatedAt != null && id != null && urlIcon != null && createdAt != null)
            {
                ContentValues contentValues = new ContentValues();

                contentValues.put("value", value);
                contentValues.put("url", url);
                contentValues.put("updatedAt", updatedAt);
                contentValues.put("idJoke", id);
                contentValues.put("urlIcon", urlIcon);
                contentValues.put("createdAt", createdAt);

                dbWrite.insert(FAVOURITES_TABLE, null,contentValues);
            }

        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nClass: SQLiteUtils" +
                    "\nMethod: cacheFavourites" +
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

    public List<Joke> getAllFavourites()
    {
        List<Joke> toReturn = null;

        createFavouritesTable();

        SQLiteDatabase dbReader = null;
        Cursor cursor = null;

        try
        {
            dbReader = getReadableDatabase();

            if(dbReader != null)
            {
                String query = "SELECT *" + " FROM " + FAVOURITES_TABLE + ";";

                cursor = dbReader.rawQuery(query, null, null);

                if(cursor != null && cursor.getCount() > 0)
                {
                    toReturn = new ArrayList<Joke>();
                    while(cursor.moveToNext())
                    {
                        Joke joke = new Joke();
                        joke.setValue(cursor.getString(1));
                        joke.setUrl(cursor.getString(2));
                        joke.setUpdatedAt(cursor.getString(3));
                        joke.setId(cursor.getString(4));
                        joke.setUrlIcon(cursor.getString(5));
                        joke.setCreatedAt(cursor.getString(6));

                        toReturn.add(joke);
                    }
                }
            }
        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nClass: SQLiteUtils" +
                    "\nMethod: getAllFavourites" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }finally
        {
            if(dbReader != null)
            {
                dbReader.close();
                dbReader = null;
            }

            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }


        return toReturn;

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