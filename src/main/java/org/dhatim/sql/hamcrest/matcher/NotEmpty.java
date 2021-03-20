package org.dhatim.sql.hamcrest.matcher;

import org.dhatim.sql.hamcrest.SqlQuery;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class NotEmpty extends TypeSafeMatcher<SqlQuery> {

    @Override
    public void describeTo(Description description) {
    }

    @Override
    protected void describeMismatchSafely(SqlQuery actual, Description mismatchDescription) {
        mismatchDescription.appendText(" was not empty ").appendValueList("[", ", ", "]", actual.getTextChildren());
    }

    @Override
    protected boolean matchesSafely(SqlQuery item) {
        return item.children().count() != 0;
    }

}
