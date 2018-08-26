package com.casting.component.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.network.base.NetworkResponse;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.component.CommonFragment;
import com.casting.component.activity.MainActivity;
import com.casting.model.request.Login;
import com.casting.model.request.RequestFacebookSession;
import com.casting.view.InsertForm;

public class LoginFragment extends CommonFragment implements IResponseListener, TextView.OnEditorActionListener {

    private ViewGroup   mRegisterFrame;
    private Button      mButton0;
    private Button      mButton1;
    private TextView    mBottomTextView;

    private InsertForm  mLoginIDForm;
    private InsertForm  mLoginPWForm;
    private Button      mLoginButton;

    public LoginFragment()
    {
        super(R.layout.fragment_login);
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception
    {
        mRegisterFrame = find(R.id.registerFrame);
        mButton0 = find(R.id.registerFrameButton0);
        mButton0.setOnClickListener(this);
        mButton1 = find(R.id.registerFrameButton1);
        mButton1.setOnClickListener(this);
        mBottomTextView = find(R.id.loginAppearButton);
        mBottomTextView.setOnClickListener(this);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString1 = new SpannableString("이미 캐스터를 사용하고 있다면 ? ");
        SpannableString spannableString2 = new SpannableString("로그인");
        spannableString2.setSpan(new UnderlineSpan() , 0 , spannableString2.length(), 0);
        spannableStringBuilder.append(spannableString1);
        spannableStringBuilder.append(spannableString2);
        mBottomTextView.setText(spannableStringBuilder);

        mLoginIDForm = find(R.id.insertForm1);
        mLoginIDForm.setEditorActionListener(this);
        mLoginPWForm = find(R.id.insertForm2);
        mLoginPWForm.setEditorActionListener(this);

        mLoginButton = find(R.id.loginButton);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    protected boolean onBackPressed()
    {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onClickEvent(View v)
    {
        if (v.equals(mButton0))
        {
            RegisterFragment registerFragment = new RegisterFragment();

            replaceFragment(R.id.intro_layoutFrame, registerFragment);
        }
        else if (v.equals(mButton1))
        {

            RequestFacebookSession requestFacebookSession = new RequestFacebookSession();
            requestFacebookSession.setAppCompatActivity((AppCompatActivity) getActivity());
            requestFacebookSession.setResponseListener(this);

            RequestHandler.getInstance().request(requestFacebookSession);

        }
        else if (v.equals(mBottomTextView))
        {
            mRegisterFrame.setVisibility(View.GONE);
            mBottomTextView.setVisibility(View.GONE);

            mLoginIDForm.setVisibility(View.VISIBLE);
            mLoginPWForm.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.VISIBLE);
        }
        else if (v.equals(mLoginButton))
        {
            String emailAddress = mLoginIDForm.getInsertedText();
            String password = mLoginPWForm.getInsertedText();

            Login login = new Login();
            login.setEmailAddress(emailAddress);
            login.setPassword(password);
            login.setResponseListener(this);

            RequestHandler.getInstance().request(login);
        }
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

    @Override
    public boolean onEditorAction(TextView v, int action, KeyEvent event)
    {
        if (v.equals(mLoginIDForm.getInsertView()) && action == EditorInfo.IME_ACTION_NEXT)
        {
            mLoginPWForm.getInsertView().requestFocus();
        }
        else if (v.equals(mLoginPWForm.getInsertView()) && action == EditorInfo.IME_ACTION_NEXT)
        {
            UtilityUI.setForceKeyboardDown(getContext(), mLoginPWForm.getInsertView());
        }

        return false;
    }
}