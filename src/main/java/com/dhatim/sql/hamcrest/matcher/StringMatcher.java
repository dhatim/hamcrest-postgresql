package com.dhatim.sql.hamcrest.matcher;

import com.dhatim.sql.hamcrest.SqlQuery;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hamcrest.Description;

public class StringMatcher extends AbstractQueryMatcher {

    private final String value;
    private final boolean ignoreCase;

    public StringMatcher(String name, String xpath, String value, boolean ignoreCase) {
        super(name, xpath);
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(getName()).appendText(" is ").appendValue(value);
    }

    @Override
    protected boolean matchesSafely2(SqlQuery item) {
        return item.children().map(ParseTree::getText).anyMatch(ignoreCase ? value::equalsIgnoreCase : value::equals);
    }

    @Override
    protected void describeMismatchSafely2(SqlQuery actual, Description mismatchDescription) {
        mismatchDescription.appendText(getName());
        mismatchDescription.appendText(" was ");
        mismatchDescription.appendValueList("[", ",", "]", actual.children().map(ParseTree::getText).collect(Collectors.toList()));
    }

}
