package com.example.jobjoin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.CodeVerifierUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Login(View view) {
        AuthManager authManager = AuthManager.getInstance(this);
        AuthorizationService authService = authManager.getAuthService();

        AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest
                .Builder(
                authManager.getAuthConfig(),
                "my-awesome-app",
                "code",
                Uri.parse("gclprojects.chunlin.myapp:/oauth2callback"))
                .setScope("openid profile email offline_access");

        String codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier();
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepository(this);
        sharedPreferencesRepository.saveCodeVerifier(codeVerifier);

        authRequestBuilder.setCodeVerifier(codeVerifier);

        AuthorizationRequest authRequest = authRequestBuilder.build();

        Intent authIntent = new Intent(this, LoginAuthActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, authRequest.hashCode(), authIntent, 0);

        authService.performAuthorizationRequest(
                authRequest,
                pendingIntent);
    }
}