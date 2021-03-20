package org.dhatim.sql.hamcrest.matcher;

import org.dhatim.sql.hamcrest.SqlQuery;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DebugMatcher extends TypeSafeMatcher<SqlQuery> {

    private final Matcher<SqlQuery> matcher;

    public DebugMatcher(Matcher<SqlQuery> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void describeTo(Description description) {
        matcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(SqlQuery actual, Description mismatchDescription) {
        matcher.describeMismatch(actual, mismatchDescription);
    }

    @Override
    protected boolean matchesSafely(SqlQuery item) {
        item.printTree();
        return matcher.matches(item);
    }

}
