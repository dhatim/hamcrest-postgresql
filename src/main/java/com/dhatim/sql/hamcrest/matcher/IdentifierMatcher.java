package com.dhatim.sql.hamcrest.matcher;

import com.dhatim.sql.hamcrest.SqlQuery;
import com.dhatim.sql.lang.PSQLParser;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class IdentifierMatcher extends AbstractQueryMatcher {

    private final Matcher<String> identifierMatcher;

    public IdentifierMatcher(Matcher<String> identifierMatcher) {
        super("identifier", "//identifier/*");
        this.identifierMatcher = identifierMatcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(getName()).appendText(" is ").appendDescriptionOf(identifierMatcher);
    }

    @Override
    protected boolean matchesSafelyDerived(SqlQuery item) {
        return item.children().map(this::extractIdentifier).anyMatch(identifierMatcher::matches);
    }

    @Override
    protected void describeMismatchSafelyDerived(SqlQuery actual, Description mismatchDescription) {
        mismatchDescription.appendText(getName());
        mismatchDescription.appendText(" was ");
        mismatchDescription.appendValueList("[", ",", "]", actual.children().map(this::extractIdentifier).collect(Collectors.toList()));
    }
    
    private String extractIdentifier(ParseTree parse) {
        if (parse instanceof TerminalNode) {
            int type = ((TerminalNode) parse).getSymbol().getType();
            if (type == PSQLParser.Identifier) {
                return parse.getText();
            } else if (type == PSQLParser.QuotedIdentifier) {
                return parse.getText().substring(1, parse.getText().length()-1);
            }
        }
        return parse.getText();
    }

}
