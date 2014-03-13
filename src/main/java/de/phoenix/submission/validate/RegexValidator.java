package de.phoenix.submission.validate;

import java.util.regex.Pattern;

/**
 * Executes regular expressions to check, if the source code contains forbidden
 * fragements.
 */
public class RegexValidator implements ContentValidator {

    private final Pattern pattern;
    private final String errorMessage;

    /**
     * Construct a validator on regular expression base.
     * 
     * @param regex
     *            The regular expression to search for
     * @param errorMessage
     *            The error message, when the regex matches
     */
    public RegexValidator(String regex, String errorMessage) {
        this.pattern = Pattern.compile(regex);
        this.errorMessage = errorMessage;
    }

    @Override
    public ContentValidatorResult validateCode(String code) {
        if (pattern.matcher(code).matches()) {
            return new ContentValidatorResult(errorMessage);
        } else
            return new ContentValidatorResult();
    }
}
