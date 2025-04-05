package application;

import java.util.ArrayList;
import java.util.List;

public class AnswersList { // list hold all answers

    private List<Answer> answers;

    public AnswersList() {
        this.answers = new ArrayList<>();
    }

    public void addAnswer(Answer answer) { // Method to add a new answers
        answers.add(answer);
    }

    public List<Answer> getAnswers() { //Methods to restore all the answers
        return answers;
    }

    public List<Answer> searchAnswers(int questionId) { // Methods to research for the answers
        List<Answer> result = new ArrayList<>();
        for (Answer a : answers) {
            if (a.getQuestionId() == questionId) {
                result.add(a);
            }
        }
        return result;
    }
}
