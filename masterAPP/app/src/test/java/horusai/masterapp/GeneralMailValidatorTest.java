package horusai.masterapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import horusai.masterapp.utils.mail.GeneralMailValidator;


/**
 * Created by joaosousa on 14/07/18.
 */

@RunWith(Parameterized.class)
public class GeneralMailValidatorTest {

    @Parameters(name = "{index}: {0}")
    public static Collection<String> allowedEmails() {
        return Arrays.asList("@hotmail.com",
                                    "@gmail.com",
                                    "@sapo.pt",
                                    "@yahoo.com");
    }

    @Parameter(value = 0)
    public String allowedEmail;

    @Test
    public void addAllowedEmailTest() {
        assert GeneralMailValidator.isEmailValid(allowedEmail);
    }
}
