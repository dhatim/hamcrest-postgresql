package com.dhatim.sql.hamcrest;

import com.dhatim.sql.hamcrest.matcher.AllMatcher;
import com.dhatim.sql.hamcrest.matcher.IdentifierMatcher;
import com.dhatim.sql.hamcrest.matcher.NotEmpty;
import com.dhatim.sql.hamcrest.matcher.ValueMatcher;
import com.dhatim.sql.hamcrest.matcher.XPathMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class QueryMatchers {
    
    @SafeVarargs
    @Factory
    public static Matcher<SqlQuery> query(Matcher<? super SqlQuery>...matchers) {
        return allOf(matchers);
    }
    
    @Factory
    public static Matcher<SqlQuery> from(Matcher<String> tableNameMatcher) {
        return from(table(tableNameMatcher));
    }
    
    @SafeVarargs
    @Factory
    public static Matcher<SqlQuery> from(Matcher<? super SqlQuery>... matchers) {
        return xpath("from", "//from_clause", allOf(matchers));
    }
    
    @SafeVarargs
    @Factory
    public static Matcher<SqlQuery> having(Matcher<? super SqlQuery>... matchers) {
        return having(allOf(matchers));
    }
    
    @Factory
    public static Matcher<SqlQuery> having() {
        return having(notEmpty());
    }
    
    private static Matcher<SqlQuery> having(Matcher<? super SqlQuery> matcher) {
        return xpath("having", "//having_clause", matcher);
    }
    
    @Factory
    public static Matcher<SqlQuery> table(Matcher<String> identifierMatcher) {
        return xpath("table", "//table_primary//table_name", identifier(identifierMatcher));
    }
    
    @Factory
    public static Matcher<SqlQuery> identifier(Matcher<String> identifierMatcher) {
        return new IdentifierMatcher(identifierMatcher);
    }
    
    /*@Factory
    public static FromMatcher innerJoin(String tableName) {
        
    }
    
    @Factory
    public static FromMatcher innerJoin(String tableName, WhereMatcher... matchers) {
        
    }*/
    
    @SafeVarargs
    @Factory
    public static Matcher<SqlQuery> leftJoin(Matcher<String> tableNameMatcher, Matcher<? super SqlQuery>... matchers) {
        return join(allOf(value("join type", "//join_type", "left"), table(tableNameMatcher), allOf(matchers)));
    }
    
    @Factory
    public static Matcher<SqlQuery> leftJoin(Matcher<String> tableNameMatcher) {
        return join(allOf(value("join type", "//join_type", "left"), table(tableNameMatcher)));
    }
    
    @Factory
    private static Matcher<SqlQuery> join(Matcher<? super SqlQuery> matcher) {
        return xpath("join", "//table_reference/joined_table/joined_table_primary", matcher);
    }
    
    /*@Factory
    public static WhereMatcher greater(OperandMatcher left, OperandMatcher right) {
        
    }
    
    @Factory
    public static <T> WhereMatcher greater(OperandMatcher left, T right) {
        return greater(left, value(right));
    }
    
    @Factory
    public static OperandMatcher count() {
        
    }
    
    @Factory
    public static <T> OperandMatcher value(T value) {
        
    }*/
    
    @Factory
    private static Matcher<SqlQuery> value(String name, String xpath, String value) {
        return value(name, xpath, value, true);
    }
    
    @Factory
    private static Matcher<SqlQuery> value(String name, String xpath, String value, boolean ignoreCase) {
        return new ValueMatcher(name, xpath, value, ignoreCase);
    }
    
    @Factory
    private static Matcher<SqlQuery> xpath(String name, String xpath, Matcher<? super SqlQuery> matcher) {
        return XPathMatcher.xpath(name, xpath, matcher);
    }
    
    @SafeVarargs
    @Factory
    private static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery>... matchers) {
        return AllMatcher.allOf(matchers);
    }
    
    private static Matcher<SqlQuery> notEmpty() {
        return new NotEmpty();
    }
    
}
