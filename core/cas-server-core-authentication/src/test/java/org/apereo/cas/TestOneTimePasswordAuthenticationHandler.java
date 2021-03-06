package org.apereo.cas;

import org.apereo.cas.authentication.AbstractAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.BasicCredentialMetaData;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.DefaultAuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.OneTimePasswordCredential;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;

import lombok.val;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Test one-time password authentication handler.
 *
 * @author Marvin S. Addison
 * @since 4.0.0
 */
public class TestOneTimePasswordAuthenticationHandler extends AbstractAuthenticationHandler {

    private final Map<String, String> credentialMap;

    /**
     * Creates a new instance with a map that defines the one-time passwords that can be authenticated.
     *
     * @param credentialMap Non-null map of one-time password identifiers to password values.
     */
    public TestOneTimePasswordAuthenticationHandler(final Map<String, String> credentialMap) {
        super("", null, null, null);
        this.credentialMap = credentialMap;
    }

    @Override
    public AuthenticationHandlerExecutionResult authenticate(final Credential credential)
        throws GeneralSecurityException {
        val otp = (OneTimePasswordCredential) credential;
        val valueOnRecord = credentialMap.get(otp.getId());
        if (otp.getPassword().equals(valueOnRecord)) {
            return new DefaultAuthenticationHandlerExecutionResult(this, new BasicCredentialMetaData(otp),
                new DefaultPrincipalFactory().createPrincipal(otp.getId()));
        }
        throw new FailedLoginException();
    }

    @Override
    public boolean supports(final Class<? extends Credential> clazz) {
        return OneTimePasswordCredential.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean supports(final Credential credential) {
        return credential instanceof OneTimePasswordCredential;
    }
}
