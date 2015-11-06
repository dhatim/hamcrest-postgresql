package com.dhatim.sql.hamcrest.matcher;

import com.dhatim.sql.hamcrest.SqlQuery;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hamcrest.Description;

public class ValueContainingMatcher<T> extends AbstractQueryMatcher {

    private final T value;

    public ValueContainingMatcher(String name, String xpath, T value) {
        super(name, xpath);
        this.value = value;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(getName()).appendText(" is ").appendValue(value);
    }

    @Override
    protected boolean matchesSafely2(SqlQuery item) {
        return item.getTextStream().anyMatch(s -> s.contains(value.toString()));
    }

    @Override
    protected void describeMismatchSafely2(SqlQuery actual, Description mismatchDescription) {
        mismatchDescription.appendText(getName());
        mismatchDescription.appendText(" was ");
        mismatchDescription.appendValueList("[", ",", "]", actual.children().map(ParseTree::getText).collect(Collectors.toList()));
    }

}
