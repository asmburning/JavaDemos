package org.lxy.effective.ch2.ex2Builder;

import lombok.Builder;

@Builder
public abstract class Bird {
    private String name;
    private String type;

    // Subclasses must override this method to return "this"

}
