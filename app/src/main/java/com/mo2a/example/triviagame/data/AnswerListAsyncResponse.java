package com.mo2a.example.triviagame.data;

import com.mo2a.example.triviagame.model.Question;
import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
