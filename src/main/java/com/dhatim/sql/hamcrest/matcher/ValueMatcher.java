package com.dhatim.sql.hamcrest.matcher;

import com.dhatim.sql.hamcrest.SqlQuery;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hamcrest.Description;

public class ValueMatcher<T> extends AbstractQueryMatcher {

    private final T value;
    private final boolean quoted;

    public ValueMatcher(String name, String xpath, T value, boolean quoted) {
        super(name, xpath);
        this.value = value;
        this.quoted = quoted;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(getName()).appendText(" is ").appendValue(value);
    }

    @Override
    protected boolean matchesSafelyDerived(SqlQuery item) {
        String stringValue = quoted ? String.format("'%s'", value.toString()) : value.toString();
        return item.getTextStream().anyMatch(stringValue::equals);
    }

    @Override
    protected void describeMismatchSafelyDerived(SqlQuery actual, Description mismatchDescription) {
        mismatchDescription.appendText(getName() + " for ").appendValue(value);
        mismatchDescription.appendText(" was ");
        mismatchDescription.appendValueList("[", ",", "]", actual.children().map(ParseTree::getText).collect(Collectors.toList()));
    }

}
