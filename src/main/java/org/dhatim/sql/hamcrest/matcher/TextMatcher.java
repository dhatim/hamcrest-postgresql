package org.dhatim.sql.hamcrest.matcher;

import org.dhatim.sql.hamcrest.SqlQuery;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class TextMatcher extends TypeSafeMatcher<SqlQuery> implements NamedMatcher<SqlQuery> {

    private final String xpath;
    private final String description;
    private final String text;

    public TextMatcher(String xpath, String description, String text) {
        this.xpath = xpath;
        this.text = text;
        this.description = description;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description).appendText(" = ").appendValue(text);
    }

    @Override
    protected boolean matchesSafely(SqlQuery item) {
        System.out.println("TEXT DEBUG | MATCHES ------------------");
        System.out.println("TEXT DEBUG | was " + item.toString());
        SqlQuery derived = item.derive(xpath);
        System.out.println("TEXT DEBUG | is " + derived.toString());
        return derived.getTextStream().anyMatch(s -> text.equals(s));
    }

    @Override
    public String getName() {
        return description;
    }

}
