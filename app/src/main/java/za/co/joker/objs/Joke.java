package za.co.joker.objs;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import za.co.joker.utils.ConstantUtils;
import za.co.joker.utils.DTUtils;

public class Joke implements Comparable<Joke>
{
    private String value;
    private String url;
    private String updatedAt;
    private String id;
    private String urlIcon;
    private String createdAt;
    private List<Category> categories;

    public Joke()
    {

    }

    public Joke(String value, String url, String updatedAt, String id, String urlIcon, String createdAt, List<Category> categories) {
        this.value = value;
        this.url = url;
        this.updatedAt = updatedAt;
        this.id = id;
        this.urlIcon = urlIcon;
        this.createdAt = createdAt;
        this.categories = categories;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void populate(JSONObject jsonObject)
    {
        try
        {
            if(jsonObject != null)
            {
                this.value = jsonObject.has("value") ? jsonObject.getString("value") : null;
                this.url = jsonObject.has("url") ? jsonObject.getString("url") : null;
                this.updatedAt = jsonObject.has("updated_at") ? jsonObject.getString("updated_at") : null;
                this.id = jsonObject.has("id") ? jsonObject.getString("id") : null;
                this.urlIcon = jsonObject.has("icon_url") ? jsonObject.getString("icon_url") : null;
                this.createdAt = jsonObject.has("created_at") ? jsonObject.getString("created_at") : null;

                if(jsonObject.has("categories"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    if(jsonArray != null && jsonArray.length() > 0)
                    {
                        this.categories = new ArrayList<Category>();
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            Category category = new Category();
                            category.setName(jsonArray.getString(i));

                            this.categories.add(category);
                        }
                    }
                }
            }
        }catch(JSONException e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: Joke - populate"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

    }

    public JSONObject toJSONObject()
    {
        JSONObject toReturn = new JSONObject();

        try
        {
            toReturn.put("value", this.value);
            toReturn.put("url", this.url);
            toReturn.put("updated_at", this.updatedAt);
            toReturn.put("id", this.id);
            toReturn.put("icon_url", this.urlIcon);
            toReturn.put("created_at", this.createdAt);

            if(this.categories != null && this.categories.size() > 0)
            {
                JSONArray jsonArray = new JSONArray();
                for(int i = 0; i < this.categories.size(); i++)
                {
                    jsonArray.put(this.categories.get(i).getName());
                }
                toReturn.put("categories", jsonArray);
            }
        }catch (JSONException e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: Joke - toJSONObject"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }


        return toReturn;
    }

    @Override
    public int compareTo(Joke o) {
        return o.getCreatedAt().compareTo(this.getCreatedAt());
    }

    public static Comparator<Joke> sortByYearAscending = new Comparator<Joke>() {
        @Override
        public int compare(Joke o1, Joke o2) {
            return o1.getCreatedAt().compareTo(o2.getCreatedAt());
        }
    };

    public static Comparator<Joke> sortByYearDescending = new Comparator<Joke>() {
        @Override
        public int compare(Joke o1, Joke o2) {
            return o2.getCreatedAt().compareTo(o1.getCreatedAt());
        }
    };

}
