package org.basecalculator.logic.binarynumber;

// Contains all of the possible ways to represent signed binary
public enum SignedType {
        TWOS_COMPLEMENT("Two's Complement"), ONES_COMPLEMENT("One's Complement"), SIGN_MAGNITUDE("Sign Magnitude");
        private final String value;

        // We need a custom toString method, as the default conversion results in poor results (e.g. due to not supporting apostrophes).
        // This is done by manually setting a String value.
        SignedType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
}
