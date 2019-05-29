package com.mo2a.example.triviagame.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mo2a.example.triviagame.controller.AppController;
import com.mo2a.example.triviagame.model.Question;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class QuestionBank {
    private static final String TAG = "QuestionBank";
    private ArrayList<Question> questionArrayList = new ArrayList<>();

    public ArrayList<Question> getQuestions(final AnswerListAsyncResponse callBack) {
        String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET
                , url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0 ; i< response.length(); i++) {
                            try {
                                Question question= new Question(response.getJSONArray(i).getString(0),response.getJSONArray(i).getBoolean(1));
                                questionArrayList.add(question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (null != callBack) callBack.processFinished(questionArrayList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
