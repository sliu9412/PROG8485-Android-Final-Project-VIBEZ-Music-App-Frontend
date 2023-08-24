package com.example.androidfinal.activities.register;

import android.widget.EditText;

import com.example.androidfinal.databinding.ActivityRegisterBinding;

public class RegisterActivityValidation {
    private final ActivityRegisterBinding activityRegisterBinding;

    public RegisterActivityValidation(ActivityRegisterBinding activityRegisterBinding) {
        this.activityRegisterBinding = activityRegisterBinding;
    }

    // Validate the registration form fields
    public boolean validate() {
        EditText emailController = activityRegisterBinding.accountRegisterEmail;
        EditText userNameController = activityRegisterBinding.accountRegisterUsername;
        EditText passwordController = activityRegisterBinding.accountRegisterPassword;
        EditText passwordConfirmController = activityRegisterBinding.accountRegisterPasswordConfirm;

        // Reset previous error messages
        emailController.setError(null);
        userNameController.setError(null);
        passwordController.setError(null);
        passwordConfirmController.setError(null);

        // Check if email address is empty
        if (emailController.getText().toString().isEmpty()) {
            emailController.setError("Email address is required");
            return false;
        }

        // Check if username is empty
        if (userNameController.getText().toString().isEmpty()) {
            userNameController.setError("Username is required");
            return false;
        }

        // Check if password is empty
        if (passwordController.getText().toString().isEmpty()) {
            passwordController.setError("Password is required");
            return false;
        }

        // Check if password and confirm password match
        if (!passwordController.getText().toString().equals(passwordConfirmController.getText().toString())) {
            passwordConfirmController.setError("Confirm password does not match with the password");
            return false;
        }

        // All fields are valid
        return true;
    }
}
