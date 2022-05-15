package edu.uw.tcss450.group6App.ui.chat.chatMenu;

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

public class ChatMenuViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mChats;
    private String currentEmail;

    public ChatMenuViewModel(@NonNull Application application) {
        super(application);
        mChats = new MutableLiveData<>();
        mChats.setValue(new JSONObject());
    }

    public void setCurrentEmail(String email){
        currentEmail = email;
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mChats.observe(owner, observer);
    }

    /**
     * Convert the returned JSON to a list of ChatInfo Objects to be read on the app
     * @param response the JSON object
     */
    public List<ChatInfo> convertToList(final JSONObject response) {
        List<ChatInfo> list = new ArrayList<>();
        try {
            JSONArray messages = response.getJSONObject("message").getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ChatInfo cChat = new ChatInfo(
                        message.getString("name"),
                        message.getInt("chatid")
                );
                list.add(cChat);
            }
            //inform observers of the change (setValue)
            return list;
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ConvertToList");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * get the chats of the user from the web service
     */
    public void getChat(){
        String url = "https://team-6-tcss-450-web.herokuapp.com/chat/get";

        JSONObject body = new JSONObject();
        try {
            body.put("userEmail", currentEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mChats::setValue,
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
        Log.v("OH CRAP", "Wheres the chat????");
    }



}
