package com.casting.component.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.network.base.NetworkResponse;
import com.casting.commonmodule.view.component.CommonFragment;
import com.casting.component.activity.MainActivity;
import com.casting.model.request.RegisterMember;
import com.casting.view.InsertForm;

public class RegisterFragment extends CommonFragment implements TextView.OnEditorActionListener, IResponseListener {

    private InsertForm  mInsertForm1;
    private InsertForm  mInsertForm2;
    private InsertForm  mInsertForm3;
    private InsertForm  mInsertForm4;
    private Button      mRegisterButton;

    public RegisterFragment() {
        super(R.layout.layout_register);
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception
    {
        mInsertForm1 = find(R.id.registrationForm1);
        mInsertForm2 = find(R.id.registrationForm1);
        mInsertForm3 = find(R.id.registrationForm1);
        mInsertForm4 = find(R.id.registrationForm1);

        mInsertForm1.setEditorActionListener(this);
        mInsertForm2.setEditorActionListener(this);
        mInsertForm3.setEditorActionListener(this);
        mInsertForm4.setEditorActionListener(this);

        mRegisterButton = find(R.id.registerButton);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onClickEvent(View v)
    {
        if (v.equals(mInsertForm1))
        {

        }
        else if (v.equals(mInsertForm1))
        {

        }
        else if (v.equals(mInsertForm2))
        {

        }
        else if (v.equals(mInsertForm3))
        {

        }
        else if (v.equals(mInsertForm4))
        {

        }
        else if (v.equals(mRegisterButton))
        {
            String password1 = mInsertForm3.getInsertedText();
            String password2 = mInsertForm4.getInsertedText();

            if (TextUtils.isEmpty(password1))
            {
                Toast.makeText(getContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(password2))
            {
                Toast.makeText(getContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }
            else if (!password1.equalsIgnoreCase(password2))
            {
                Toast.makeText(getContext(), "비밀번호가 맞지 않습니다 다시 확인해주세요", Toast.LENGTH_SHORT).show();
            }
            else
            {
                RegisterMember registerMember = new RegisterMember();
                registerMember.setNickName(mInsertForm1.getInsertedText());
                registerMember.setEmailAddress(mInsertForm2.getInsertedText());
                registerMember.setPassword(mInsertForm3.getInsertedText());
                registerMember.setResponseListener(this);

                RequestHandler.getInstance().request(registerMember);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {

        return false;
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        if (response instanceof NetworkResponse)
        {
            Intent intent = new Intent(getContext(), MainActivity.class);

            Activity a = getActivity();

            if (a != null && !a.isFinishing())
            {
                a.startActivity(intent);
                a.finish();
            }
        }
    }
}
