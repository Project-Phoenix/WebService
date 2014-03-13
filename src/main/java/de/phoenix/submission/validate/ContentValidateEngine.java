package de.phoenix.submission.validate;

import java.util.ArrayList;
import java.util.List;

import de.phoenix.submission.validate.ContentValidator.ContentValidatorResult;

/**
 * The engine to check, if a source code contains forbidden content. The engine
 * is supplied by instances of {@link ContentValidator} and if one
 * {@link ContentValidator} matches, the engine will not resume. <br>
 * Only content, that passes all checks, are accepted.
 */
public class ContentValidateEngine {

    private List<ContentValidator> contentCheckerList;

    public ContentValidateEngine() {
        this.contentCheckerList = new ArrayList<ContentValidator>();
    }

    /**
     * Add an instance of {@link ContentValidator} to the engine. The order of
     * invoke is the order of registering.
     * 
     * @param cChecker
     *            Instance to add to engine
     * @return This engine
     */
    public ContentValidateEngine registerContentChecker(ContentValidator cChecker) {
        this.contentCheckerList.add(cChecker);
        return this;
    }

    /**
     * Invokes all registered validators to validate a certain source code. It
     * does NOT check if the syntax is correct
     * 
     * @param sourceCode
     *            The source code to validate
     * @return A result containing. If there is no reason, the source code was
     *         valid. Returns a valid result, when no validator is registered or
     *         the source code is either null or an empty String
     */
    public ContentValidatorResult validate(String sourceCode) {
        if (contentCheckerList.isEmpty()) {
            return new ContentValidatorResult();
        }
        if (sourceCode == null || sourceCode.isEmpty()) {
            System.out.println("SourceCode is null or empty - no checks neccessary");
            return new ContentValidatorResult();
        }

        ContentValidatorResult result = null;

        for (ContentValidator cChecker : contentCheckerList) {
            result = cChecker.validateCode(sourceCode);
            if (!result.isValid())
                break;
        }

        return result;
    }

}
