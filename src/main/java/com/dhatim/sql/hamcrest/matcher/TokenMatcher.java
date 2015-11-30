package com.dhatim.sql.hamcrest.matcher;

import com.dhatim.sql.hamcrest.SqlQuery;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hamcrest.Description;

public class TokenMatcher extends AbstractQueryMatcher {

    private final String token;

    public TokenMatcher(String name, String xpath, String token) {
        super(name, xpath);
        this.token = token;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(getName()).appendText(" is ").appendText(token);
    }

    @Override
    protected boolean matchesSafelyDerived(SqlQuery item) {
        return item.getTextStream().anyMatch(token::equals);
    }

    @Override
    protected void describeMismatchSafelyDerived(SqlQuery actual, Description mismatchDescription) {
        mismatchDescription.appendText(getName());
        mismatchDescription.appendText(" was ");
        mismatchDescription.appendValueList("[", ",", "]", actual.children().map(ParseTree::getText).collect(Collectors.toList()));
    }

}