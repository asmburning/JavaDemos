package org.lxy.effective.ch2.ex2Builder;

public class CalPizza extends Pizza {
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false; // Default

        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }

        @Override
        public CalPizza build() {
            return new CalPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private CalPizza(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }
}
