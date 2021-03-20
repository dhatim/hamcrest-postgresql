package org.dhatim.sql.hamcrest.matcher;

import org.dhatim.sql.hamcrest.SqlQuery;
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
    protected boolean matchesSafelyDerived(SqlQuery item) {
        return item.children().map(ParseTree::getText).anyMatch(ignoreCase ? value::equalsIgnoreCase : value::equals);
    }

    @Override
    protected void describeMismatchSafelyDerived(SqlQuery actual, Description mismatchDescription) {
        mismatchDescription.appendText(getName());
        mismatchDescription.appendText(" was ");
        mismatchDescription.appendValueList("[", ",", "]", actual.children().map(ParseTree::getText).collect(Collectors.toList()));
    }

}
