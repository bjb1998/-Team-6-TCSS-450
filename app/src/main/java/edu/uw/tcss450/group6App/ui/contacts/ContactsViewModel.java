package edu.uw.tcss450.group6App.ui.contacts;

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

import edu.uw.tcss450.group6App.ui.contacts.search.ContactInfo;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mUsers;
    private String currentEmail;
    private int currentContactSenderStatus;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mUsers = new MutableLiveData<>();
        mUsers.setValue(new JSONObject());
    }

    public void setCurrentEmail(String email){
        currentEmail = email;
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mUsers.observe(owner, observer);
    }

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
                cContact.setDidSend(message.getBoolean("didsend"));
                list.add(cContact);
            }
            //inform observers of the change (setValue)
            return list;
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ConvertToList");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void getContacts(int verified){
        String url = "https://team-6-tcss-450-web.herokuapp.com/contacts/get";

        JSONObject body = new JSONObject();
        try {
            body.put("userEmail", currentEmail);
            body.put("verified", verified);
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

    public void acceptContact(String otherEmail){
        String url = "https://team-6-tcss-450-web.herokuapp.com/contacts/accept";

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



    public void removeContact(String otherEmail){
        String url = "https://team-6-tcss-450-web.herokuapp.com/contacts/remove";

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

    public void createChat(String otherEmail){
        String url = "https://team-6-tcss-450-web.herokuapp.com/chat/create";

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
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        Log.v("OH CRAP", "No Contacts Here");
    }

    public int getCurrentContactSenderStatus() {
        return currentContactSenderStatus;
    }
}
