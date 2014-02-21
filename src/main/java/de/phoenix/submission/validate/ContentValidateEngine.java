package de.phoenix.submission.validate;

import java.util.ArrayList;
import java.util.List;

import de.phoenix.submission.validate.ContentValidator.ContentValidatorResult;

public class ContentValidateEngine {

    private List<ContentValidator> contentCheckerList;

    public ContentValidateEngine() {
        this.contentCheckerList = new ArrayList<ContentValidator>();
    }

    public ContentValidateEngine registerContentChecker(ContentValidator cChecker) {
        this.contentCheckerList.add(cChecker);
        return this;
    }

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
