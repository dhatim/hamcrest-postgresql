package org.dhatim.sql.hamcrest.matcher;

import org.dhatim.sql.hamcrest.SqlQuery;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class XPathMatcher extends AbstractQueryMatcher {

    private final Matcher<? super SqlQuery> matcher;

    public XPathMatcher(String name, String xpath, Matcher<? super SqlQuery> matcher) {
        super(name, xpath);
        this.matcher = matcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has " + getName() + " ").appendDescriptionOf(matcher);
    }

    @Override
    protected boolean matchesSafelyDerived(SqlQuery item) {
        return matcher.matches(item);
    }

    @Override
    protected void describeMismatchSafelyDerived(SqlQuery actual, Description mismatchDescription) {
        if (!matcher.matches(actual)) {
            mismatchDescription.appendText(getName() + "/");
            matcher.describeMismatch(actual, mismatchDescription);
        }
    }

    public static Matcher<SqlQuery> xpath(String name, String xpath, Matcher<? super SqlQuery> matcher) {
        return new XPathMatcher(name, xpath, matcher);
    }

}
