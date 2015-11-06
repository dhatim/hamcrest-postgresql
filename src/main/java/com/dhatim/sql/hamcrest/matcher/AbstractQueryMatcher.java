package com.dhatim.sql.hamcrest.matcher;

import com.dhatim.sql.hamcrest.SqlQuery;
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
        return matchesSafely2(item.derive(xpath));
    }
    
    protected abstract boolean matchesSafely2(SqlQuery item);
    
    @Override
    protected void describeMismatchSafely(SqlQuery actual, Description mismatchDescription) {
        describeMismatchSafely2(actual.derive(xpath), mismatchDescription);
    }
    
    protected abstract void describeMismatchSafely2(SqlQuery actual, Description mismatchDescription);

    @Override
    public String getName() {
        return name;
    }

}
