package de.phoenix.submission.validate;

import java.util.Arrays;

public class CharSequenceValidator implements ContentValidator {

    private String[] forbiddenString;

    public CharSequenceValidator(String... charSequences) {
        this.forbiddenString = Arrays.copyOf(charSequences, charSequences.length);
    }

    public ContentValidatorResult validateCode(String code) {
        for (int i = 0; i < forbiddenString.length; ++i) {
            String forbiddenSequence = forbiddenString[i];
            if (code.contains(forbiddenSequence)) {
                return new ContentValidatorResult("Code can not use " + forbiddenSequence);
            }
        }

        return new ContentValidatorResult();
    }
}
