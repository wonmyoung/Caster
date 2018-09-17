package com.casting.component.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonActivity;
import com.casting.model.Member;
import com.casting.model.global.ActiveMember;
import com.casting.model.request.PostMember;

import java.util.Observable;
import java.util.Observer;

public class ProfileEditActivity extends CommonActivity implements Observer, TextView.OnEditorActionListener, IResponseListener {

    private static final int REQUEST_GALLERY = 10;

    private CircleImageView UserProfilePic;
    private CircleImageView UserProfilePicCover;
    private TextView        ProfileEditButton;
    private EditText        UserNickName;
    private EditText        UserIntroduction;

    private String      mProfilePicPath;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_profile_edit);

        UserProfilePic = find(R.id.userMemberUserPic);
        UserProfilePicCover = find(R.id.userMemberUserPicCover);
        UserProfilePicCover.setOnClickListener(this);
        ProfileEditButton = find(R.id.userProfileEditButton);
        ProfileEditButton.setOnClickListener(this);
        UserNickName = find(R.id.userMemberName);
        UserNickName.setOnEditorActionListener(this);
        UserIntroduction = find(R.id.userMemberIntroduction);
        UserIntroduction.setOnEditorActionListener(this);
    }

    @Override
    protected void onClickEvent(View v)
    {
        if (v.equals(UserProfilePicCover))
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

            startActivityForResult(intent, REQUEST_GALLERY);
        }
        else if (v.equals(ProfileEditButton))
        {
            String nickName = UserNickName.getText().toString();
            String description = UserIntroduction.getText().toString();

            Member member = ActiveMember.getInstance().getMember();

            member.setUserName(nickName);
            member.setDescription(description);

            PostMember postMember = new PostMember();
            postMember.setResponseListener(this);
            postMember.setMember(member);

            RequestHandler.getInstance().request(postMember);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQUEST_GALLERY:
                updateUserPic(data.getData());
                break;
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent)
    {
        if (textView.equals(UserNickName) && action == EditorInfo.IME_ACTION_NEXT)
        {
            UserIntroduction.requestFocus();
        }
        else if (textView.equals(UserIntroduction) && action == EditorInfo.IME_ACTION_DONE)
        {
            UtilityUI.setForceKeyboardDown(this, UserIntroduction);
        }

        return false;
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest baseRequest = response.getSourceRequest();

        if (baseRequest instanceof PostMember)
        {

        }
    }

    private void updateUserPic(Uri uri)
    {
        String[] s = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, s, null, null, null);

        try
        {
            if (cursor != null && cursor.moveToFirst())
            {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                mProfilePicPath = cursor.getString(index);

                ExifInterface e = new ExifInterface(mProfilePicPath);

                int degree = 0;

                int orientation = e.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                {
                    degree = 90;
                }
                else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                {
                    degree = 180;
                }
                else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                {
                    degree = 270;
                }
                else
                {
                    degree = 0;
                }

                Bitmap bitmap = BitmapFactory.decodeFile(mProfilePicPath);

                Matrix matrix = new Matrix();
                matrix.postRotate(degree);

                Bitmap destBitmap = Bitmap.createBitmap(
                        bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                UserProfilePic.setImageBitmap(destBitmap);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
