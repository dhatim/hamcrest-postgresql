package org.dhatim.sql.hamcrest.matcher;

import org.hamcrest.Matcher;

public interface NamedMatcher<T> extends Matcher<T> {
    String getName();
}
