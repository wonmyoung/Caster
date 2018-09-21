package com.casting.model;

import android.text.TextUtils;

import com.casting.commonmodule.model.BaseModel;

public class CastingOption extends BaseModel {

    private static final String     MultiChoice     = "multiChoice";
    private static final String     AnswerQuestion  = "answerQuestion";

    private String      QuestionType;

    private String      SelectMaximum;
    private String      SelectMinimum;

    public enum Type {CHOICE,TWO_CHOICE,ESSAY;}

    private Type CastType;

    public Type getCastType() {

        if (!TextUtils.isEmpty(QuestionType))
        {
            switch (QuestionType)
            {
                case MultiChoice:
                    return Type.CHOICE;

                case AnswerQuestion:
                    return Type.ESSAY;
            }
        }

        return null;
    }

    public void setCastType(Type castType) {
        CastType = castType;
    }

    public String getQuestionType()
    {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public String getSelectMaximum() {
        return SelectMaximum;
    }

    public void setSelectMaximum(String selectMaximum) {
        SelectMaximum = selectMaximum;
    }

    public String getSelectMinimum() {
        return SelectMinimum;
    }

    public void setSelectMinimum(String selectMinimum) {
        SelectMinimum = selectMinimum;
    }
}
