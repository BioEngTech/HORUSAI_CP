package horusai.masterapp.utils.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaosousa on 14/07/18.
 */

public class GeneralMailValidator {

    protected static List<String> allowedEmails = new ArrayList<String>(){{
        add("@hotmail.com");
        add("@gmail.com");
        add("@sapo.pt");
        add("yahoo.com");
    }};

    protected static List<String> getAllowedEmails() {
        return allowedEmails;
    }

    public static boolean isEmailValid(String email) {
        if (!email.contains("@")) {
            return false;
        }

        String emailProvider = email.substring(email.indexOf("@"),email.length());

        return getAllowedEmails().contains(emailProvider);
    }
}
