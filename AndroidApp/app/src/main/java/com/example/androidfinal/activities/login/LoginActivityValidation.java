package com.example.androidfinal.activities.login;

import android.widget.EditText;

import com.example.androidfinal.databinding.ActivityLoginBinding;

public class LoginActivityValidation {
    private final ActivityLoginBinding activityLoginBinding;

    public LoginActivityValidation(ActivityLoginBinding activityLoginBinding) {
        this.activityLoginBinding = activityLoginBinding;
    }

    // Validate the login form fields
    public boolean validate() {
        EditText emailController = activityLoginBinding.accountRegisterEmail;
        EditText passwordController = activityLoginBinding.accountRegisterPassword;

        // Check if email address is empty
        if (emailController.getText().toString().isEmpty()) {
            emailController.setError("Email address is required");
            return false;
        }

        // Check if password is empty
        if (passwordController.getText().toString().isEmpty()) {
            passwordController.setError("Password is required");
            return false;
        }

        // All fields are valid
        return true;
    }
}
