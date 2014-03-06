package de.phoenix.submission.validate;

public interface ContentValidator {

    public ContentValidatorResult validateCode(String code);

    public static class ContentValidatorResult {

        private final String reason;

        public ContentValidatorResult() {
            this.reason = null;
        }

        public ContentValidatorResult(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

        public boolean isValid() {
            return reason == null;
        }

        @Override
        public String toString() {
            return "ContentValidatorResult={isValid: " + isValid() + ";reason = " + reason + "}";
        }
    }
}
