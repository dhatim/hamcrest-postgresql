package org.dhatim.sql.hamcrest.matcher;

import org.dhatim.sql.hamcrest.SqlQuery;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Ordered extends TypeSafeDiagnosingMatcher<SqlQuery> {

    private final Iterable<Matcher<? super SqlQuery>> matchers;

    public Ordered(Iterable<Matcher<? super SqlQuery>> matchers) {
        this.matchers = matchers;
    }

    @Override
    protected boolean matchesSafely(SqlQuery item, Description mismatchDescription) {
        // Copy matchers
        LinkedList<Matcher<? super SqlQuery>> matcherList = new LinkedList<>();
        for (Matcher<? super SqlQuery> m : matchers) {
            matcherList.add(m);
        }

        int lastRemoveIndex = 0;
        for (int i = 0; i < item.getChildren().size(); i++) {
            //System.out.println("DEBUG for " + i + "/" + item.getChildren().size());
            SqlQuery current = item.derive(i, i + 1);

            Matcher<? super SqlQuery> matcher = matcherList.getFirst();
            boolean match = matcher.matches(current);
            //System.out.println("DEBUG [" + matcherList.size() + "] match " + StringDescription.toString(matcher) + " on " + current.getTextChildren().toString() + " => " + match);

            if (/*matcherList.getFirst().matches(current)*/match) {
                matcherList.removeFirst();
                lastRemoveIndex = i + 1;
                if (matcherList.isEmpty()) {
                    break;
                }
            }
        }

        if (matcherList.isEmpty()) {
            return true;
        } else {
            matcherList.getFirst().describeMismatch(item.derive(lastRemoveIndex, item.getChildren().size()), mismatchDescription);
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendList("(", " " + "and" + " ", ")", matchers);
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static Matcher<SqlQuery> allOf(Iterable<Matcher<? super SqlQuery>> matchers) {
        return new Ordered(matchers);
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    @SafeVarargs
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery>... matchers) {
        return allOf(Arrays.asList(matchers));
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery> first, Matcher<? super SqlQuery> second) {
        List<Matcher<? super SqlQuery>> matchers = new ArrayList<>(2);
        matchers.add(first);
        matchers.add(second);
        return allOf(matchers);
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery> first, Matcher<? super SqlQuery> second, Matcher<? super SqlQuery> third) {
        List<Matcher<? super SqlQuery>> matchers = new ArrayList<>(3);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        return allOf(matchers);
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery> first, Matcher<? super SqlQuery> second, Matcher<? super SqlQuery> third, Matcher<? super SqlQuery> fourth) {
        List<Matcher<? super SqlQuery>> matchers = new ArrayList<>(4);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        return allOf(matchers);
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery> first, Matcher<? super SqlQuery> second, Matcher<? super SqlQuery> third, Matcher<? super SqlQuery> fourth, Matcher<? super SqlQuery> fifth) {
        List<Matcher<? super SqlQuery>> matchers = new ArrayList<>(5);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        return allOf(matchers);
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery> first, Matcher<? super SqlQuery> second, Matcher<? super SqlQuery> third, Matcher<? super SqlQuery> fourth, Matcher<? super SqlQuery> fifth, Matcher<? super SqlQuery> sixth) {
        List<Matcher<? super SqlQuery>> matchers = new ArrayList<>(6);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        matchers.add(sixth);
        return allOf(matchers);
    }


}
