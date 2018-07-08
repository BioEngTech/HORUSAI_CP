package horusai.masterapp.utils;

public class firebaseExceptions {

    private String errorText ="";

        public String exceptionType (String errorCode){

            if (errorCode.equals("ERROR_INVALID_CUSTOM_TOKEN")) {

                errorText = "The custom token format is incorrect. Please check the documentation.";

            } else if (errorCode.equals("ERROR_CUSTOM_TOKEN_MISMATCH")) {

                errorText = "The custom token corresponds to a different audience.";

            } else if (errorCode.equals("ERROR_INVALID_CREDENTIAL")) {

                errorText = "The supplied auth credential is malformed or has expired.";

            } else if (errorCode.equals("ERROR_INVALID_EMAIL")) {

                errorText = "The email address is badly formatted.";

            } else if (errorCode.equals("ERROR_WRONG_PASSWORD")) {

                errorText = "The password is invalid.";

            } else if (errorCode.equals("ERROR_USER_MISMATCH")) {

                errorText = "The supplied credentials do not correspond to the previously signed in user.";

            } else if (errorCode.equals("ERROR_REQUIRES_RECENT_LOGIN")) {

                errorText = "This operation is sensitive and requires recent authentication. Log in again before retrying this request.";

            } else if (errorCode.equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")) {

                errorText = "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.";

            } else if (errorCode.equals("ERROR_CREDENTIAL_ALREADY_IN_USE")) {

                errorText = "This credential is already associated with a different user account.";

            } else if (errorCode.equals("ERROR_USER_DISABLED")) {

                errorText = "The user account has been disabled by an administrator.";

            } else if (errorCode.equals("ERROR_USER_TOKEN_EXPIRED")) {

                errorText = "The user's credential is no longer valid. The user must sign in again.";

            } else if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {

                errorText = "The email address is already in use by another account.";

            } else if (errorCode.equals("ERROR_USER_NOT_FOUND")) {

                errorText = "There is no user record corresponding to this identifier. The user may have been deleted.";


            } else if (errorCode.equals("ERROR_OPERATION_NOT_ALLOWED")) {

                errorText = "This operation is not allowed. You must enable this service in the console.";

            } else if (errorCode.equals("ERROR_WEAK_PASSWORD")) {

                errorText = "The given password is invalid.";

            } else if (errorCode.equals("ERROR_INVALID_USER_TOKEN")) {

                errorText = "The user's credential is no longer valid. The user must sign in again.";
            }

            return errorText;
        }
}
