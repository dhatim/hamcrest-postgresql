package org.dhatim.sql.hamcrest.matcher;

import org.dhatim.sql.hamcrest.SqlQuery;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public abstract class AbstractQueryMatcher extends TypeSafeMatcher<SqlQuery> implements NamedMatcher<SqlQuery> {

    private final String xpath;
    private final String name;

    public AbstractQueryMatcher(String name, String xpath) {
        this.xpath = xpath;
        this.name = name;
    }

    @Override
    protected boolean matchesSafely(SqlQuery item) {
        return matchesSafelyDerived(derive(item));
    }

    protected abstract boolean matchesSafelyDerived(SqlQuery item);

    @Override
    protected void describeMismatchSafely(SqlQuery actual, Description mismatchDescription) {
        describeMismatchSafelyDerived(derive(actual), mismatchDescription);
    }

    protected abstract void describeMismatchSafelyDerived(SqlQuery actual, Description mismatchDescription);

    @Override
    public String getName() {
        return name;
    }

    private SqlQuery derive(SqlQuery actual) {
        return "".equals(xpath) ? actual : actual.derive(xpath);
    }

}
