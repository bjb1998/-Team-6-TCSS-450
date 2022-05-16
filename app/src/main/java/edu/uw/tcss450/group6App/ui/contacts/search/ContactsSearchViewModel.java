package edu.uw.tcss450.group6App.ui.contacts.search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group6App.ui.chat.ChatMessage;

public class ContactsSearchViewModel extends AndroidViewModel {

    /**
     * A Map of Lists of Chat Messages.
     * The Key represents the Chat ID
     * The value represents the List of (known) messages for that that room.
     */
    private MutableLiveData<JSONObject> mUsers;
    private String currentEmail;
    private MutableLiveData<String> mError;

    public ContactsSearchViewModel(@NonNull Application application) {
        super(application);
        mUsers = new MutableLiveData<>();
        mError = new MutableLiveData<>();
        mUsers.setValue(new JSONObject());
        mError.setValue("");
    }

    public MutableLiveData<String> getmError(){return mError;}

    public void setCurrentEmail(String email){
        currentEmail = email;
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mUsers.observe(owner, observer);
    }

    /**
     *  Call the web service to search the database with the given input
     * @param name the inputted thing to search
     * @param option the type of search the user wants to do (Email, name, username)
     */
    public void searchUsers(final String name, final String option){
        String url = "https://team-6-tcss-450-web.herokuapp.com/search";
        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
            body.put("option", option);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mUsers::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);

    }

    /**
     * Convert the returned JSON to a list of ChatInfo Objects to be read on the app
     * @param response the JSON object
     */
    public List<ContactInfo> convertToList(final JSONObject response) {
        List<ContactInfo> list = new ArrayList<>();
        try {
            JSONArray messages = response.getJSONObject("message").getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ContactInfo cContact = new ContactInfo(
                        message.getString("firstname"),
                        message.getString("lastname"),
                        message.getString("username"),
                        message.getString("email")
                );
                list.add(cContact);
            }
            //inform observers of the change (setValue)
            return list;
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void connectContact(String otherEmail){
        String url = "https://team-6-tcss-450-web.herokuapp.com/contacts";

        JSONObject body = new JSONObject();
        try {
            body.put("userEmail", currentEmail);
            body.put("otherEmail", otherEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::handleSuccess,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);

    }

    private void handleSuccess(final JSONObject response) {
        Log.d("MESSAGE", response.toString());
    }

    private void handleError(final VolleyError error) {
        mError.setValue("Contact Already Exists");
        Log.d("Error", mError.getValue());
    }
}