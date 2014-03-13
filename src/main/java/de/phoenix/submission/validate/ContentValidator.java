package de.phoenix.submission.validate;

/**
 * Validate with to implement method, if a source code fragment is valid or not.
 */
public interface ContentValidator {

    /**
     * Validate, if the source code is valid or not.
     * 
     * @param code
     *            The source code to check
     * @return A {@link ContentValidatorResult} with no reason, when the code
     *         was valid. Otherwise, set a reason, why the source code isn't
     *         valid.
     */
    public ContentValidatorResult validateCode(String code);

    public static class ContentValidatorResult {

        private final String reason;

        /**
         * Result, that source code is valid.
         */
        public ContentValidatorResult() {
            this.reason = null;
        }

        /**
         * Result, that source is not valid.
         * 
         * @param reason
         *            The further description, why the source code is not valid.
         */
        public ContentValidatorResult(String reason) {
            this.reason = reason;
        }

        /**
         * @return <code>null</code>, if the source code was valid. Otherwise a
         *         description for the invalid
         */
        public String getReason() {
            return reason;
        }

        /**
         * @return <code>true</code> if, and only if, the reason was null.
         *         Otherwise <code>false</code>
         */
        public boolean isValid() {
            return reason == null;
        }

        @Override
        public String toString() {
            return "ContentValidatorResult={isValid: " + isValid() + ";reason = " + reason + "}";
        }
    }
}
