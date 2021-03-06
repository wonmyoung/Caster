package com.casting.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.session.facebook.FacebookSessionSDK;
import com.casting.commonmodule.utility.CommonPreference;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.model.Member;
import com.casting.model.global.ActiveMember;
import com.casting.model.request.Login;
import com.casting.model.request.LoginFaceBook;
import com.casting.model.request.RegisterFacebookMember;
import com.casting.model.request.RequestFacebookSession;
import com.casting.view.insert.InsertForm;
import com.facebook.CallbackManager;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends BaseFCActivity implements IResponseListener, TextView.OnEditorActionListener, Observer {

    private ViewGroup   mRegisterFrame;
    private Button      mButton0;
    private Button      mButton1;
    private TextView    mBottomTextView;

    private InsertForm  mLoginIDForm;
    private InsertForm  mLoginPWForm;
    private Button      mLoginButton;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_login);

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

    @SuppressWarnings("unchecked")
    @Override
    protected void onClickEvent(View v)
    {
        if (v.equals(mButton0))
        {
            Intent intent = new Intent(this, RegisterActivity.class);

            startActivity(intent);
        }
        else if (v.equals(mButton1))
        {
            RequestFacebookSession requestFacebookSession = new RequestFacebookSession();
            requestFacebookSession.setAppCompatActivity(this);
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
    public void onBackPressed()
    {
        int visibility = mLoginIDForm.getVisibility();
        if (visibility == View.VISIBLE)
        {
            mRegisterFrame.setVisibility(View.VISIBLE);
            mBottomTextView.setVisibility(View.VISIBLE);

            mLoginIDForm.setVisibility(View.GONE);
            mLoginPWForm.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.GONE);
        }
        else
        {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CallbackManager callbackManager = FacebookSessionSDK.getInstance().getCallbackManager();
        callbackManager.onActivityResult(requestCode , resultCode , data);
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        if (request.isRight(Login.class))
        {
            Login login = (Login) request;

            onEmailLoginResponse(login, response);
        }
        else if (request.isRight(RequestFacebookSession.class))
        {
            onFacebookSessionResponse(response);
        }
        else if (request.isRight(LoginFaceBook.class))
        {
            onFacebookLoginResponse(response);
        }
        else if (request.isRight(RegisterFacebookMember.class))
        {
            onFacebookRegister(response);
        }
    }

    private void onEmailLoginResponse(Login login, BaseResponse response)
    {
        if (login.isSuccessResponse())
        {
            Member member = (Member) response.getResponseModel();

            loadMainPage(member);
        }
        else
        {
            Toast.makeText(this, "계정 정보를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void onFacebookSessionResponse(BaseResponse response)
    {
        Member member = (Member) response.getResponseModel();

        if (member != null)
        {
            LoginFaceBook loginFaceBook = new LoginFaceBook();
            loginFaceBook.setResponseListener(this);
            loginFaceBook.setFaceBookMember(member);

            RequestHandler.getInstance().request(loginFaceBook);
        }
    }

    private void onFacebookLoginResponse(BaseResponse response)
    {
        LoginFaceBook loginFaceBook = (LoginFaceBook) response.getSourceRequest();

        Member member = loginFaceBook.getFaceBookMember();

        if (loginFaceBook.isSuccessResponse())
        {
            loadMainPage(member);
        }
        else
        {
            RegisterFacebookMember registerFacebookMember = new RegisterFacebookMember();
            registerFacebookMember.setResponseListener(this);
            registerFacebookMember.setFacebookMember(member);

            RequestHandler.getInstance().request(registerFacebookMember);
        }
    }

    private void onFacebookRegister(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        RegisterFacebookMember registerFacebookMember = (RegisterFacebookMember) request;

        if (registerFacebookMember.isResponse())
        {
            Member member = registerFacebookMember.getFacebookMember();

            loadMainPage(member);
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
            UtilityUI.setForceKeyboardDown(this, mLoginPWForm.getInsertView());
        }

        return false;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof ActiveMember && !isFinishing())
        {
            try
            {
                finish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void loadMainPage(Member member)
    {
        if (member != null)
        {
            CommonPreference commonPreference = CommonPreference.getInstance();
            commonPreference.setSharedValueByString(MEMBER_EMAIL, member.getEmail());
            commonPreference.setSharedValueByString(MEMBER_PW, member.getPassWord());
            commonPreference.setSharedValueByString(AUTH_TOKEN, member.getAuthToken());

            ActiveMember.getInstance().setMember(member);

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);

            finish();
        }
    }
}
