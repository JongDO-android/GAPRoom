package org.techtown.gaproom.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import org.techtown.gaproom.Model.CustomActionBar;
import org.techtown.gaproom.Model.CustomActionBar_login;
import org.techtown.gaproom.R;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        setActionBar();

    }
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("Session Call back :: ", "onSessionClosed " + errorResult.getErrorMessage());
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("nickname", result.getNickname());

                    if(result.getKakaoAccount().getGender() != null){
                        intent.putExtra("gender", result.getKakaoAccount().getGender().getValue());
                    }
                    else{
                        intent.putExtra("gender", "none");
                    }
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("Session Call back :: ", " onFailure : " + exception.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }
    private void setActionBar(){
        CustomActionBar_login ca = new CustomActionBar_login(this, getSupportActionBar());
        ca.setActionBar();
    }
}
