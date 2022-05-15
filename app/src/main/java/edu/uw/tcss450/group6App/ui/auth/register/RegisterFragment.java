package edu.uw.tcss450.group6App.ui.auth.register;

import static edu.uw.tcss450.group6App.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.group6App.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group6App.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.group6App.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group6App.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.group6App.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.group6App.utils.PasswordValidator.checkPwdUpperCase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6App.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.group6App.utils.PasswordValidator;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Begin the registration process
     * @param button the registration button
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * validate user first name
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    /**
     * validate user last name
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    /**
     * validate user email
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * validate if the passwords in both fields where inputted match
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    /**
     * validate the password itself
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password.\n" +
                        "Use 7 or more characters that must contain at least one lowercase letter, one Uppercase letter, " +
                        " and one special character"));
    }

    /**
     * verify everything with the server
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editUsername.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * move to the login screen
     */
    private void navigateToLogin() {
        RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();

        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    final String msg = response.getJSONObject("data").getString("message");
                    if(msg.equals("Email exists"))
                        binding.editEmail.setError(
                                "Error Authenticating: " +
                                        response.getJSONObject("data").getString("message"));
                    else if(msg.equals("Username exists")){
                        binding.editUsername.setError(
                                "Error Authenticating: " +
                                        response.getJSONObject("data").getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
