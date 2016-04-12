package com.dhatim.sql.hamcrest;

import static org.hamcrest.Matchers.*;

import com.dhatim.sql.hamcrest.matcher.AllMatcher;
import com.dhatim.sql.hamcrest.matcher.DebugMatcher;
import com.dhatim.sql.hamcrest.matcher.IdentifierMatcher;
import com.dhatim.sql.hamcrest.matcher.NotEmpty;
import com.dhatim.sql.hamcrest.matcher.Ordered;
import com.dhatim.sql.hamcrest.matcher.StringMatcher;
import com.dhatim.sql.hamcrest.matcher.TokenMatcher;
import com.dhatim.sql.hamcrest.matcher.ValueContainingMatcher;
import com.dhatim.sql.hamcrest.matcher.ValueMatcher;
import com.dhatim.sql.hamcrest.matcher.XPathMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class QueryMatchers {
    
    private static final String NO_PATH = "";
    
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
    public static Matcher<SqlQuery> where(Matcher<? super SqlQuery> matcher) {
        return xpath("where", "//where_clause", matcher);
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
        return join(allOf(keyword("join type", "//join_type", "left"), table(tableNameMatcher), allOf(matchers)));
    }
    
    @Factory
    public static Matcher<SqlQuery> leftJoin(Matcher<String> tableNameMatcher) {
        return join(allOf(keyword("join type", "//join_type", "left"), table(tableNameMatcher)));
    }
    
    @Factory
    private static Matcher<SqlQuery> join(Matcher<? super SqlQuery> matcher) {
        return xpath("join", "//table_reference/joined_table/joined_table_primary", matcher);
    }
    
    @Factory
    public static Matcher<SqlQuery> equality(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return xpath("equality", "//comparison_predicate/*", orderedAllOf(left, symbol("//comp_op", "="), right));
    }
    
    @Factory
    public static <T> Matcher<SqlQuery> equality(Matcher<? super SqlQuery> left, T right) {
        return equality(left, value(right));
    }
    
    @Factory
    private static Matcher<SqlQuery> symbol(String xpath, String literal) {
        return xpath(String.format("symbol '%s'", literal), String.format("%s//'%s'", xpath, literal), notEmpty());
    }
    
    @Factory
    public static Matcher<SqlQuery> symbol(String literal) {
        return symbol("", literal);
    }
    
    @Factory
    public static Matcher<SqlQuery> node(String nodeName) {
        return new TokenMatcher(String.format("token '%s'", nodeName), "", nodeName);
        //return xpath(String.format("node name '%s'", nodeName), nodeName, notEmpty());
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
    
    @SafeVarargs
    @Factory
    public static Matcher<SqlQuery> call(Matcher<String> functionName, Matcher<SqlQuery>... params) {
        return xpath("function", "//routine_invocation/*", orderedAllOf(xpath("function name", "//function_name/*", identifier(functionName)), xpath("arguments", "//sql_argument_list/*", orderedAllOf(params))));
    }
    
    @Factory
    public static Matcher<SqlQuery> add(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return compute(leftMatcher, "+", rightMatcher);
    }
    
    @Factory
    public static Matcher<SqlQuery> sub(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return compute(leftMatcher, "-", rightMatcher);
    }
    
    @Factory
    private static Matcher<SqlQuery> compute(Matcher<SqlQuery> leftMatcher, String operator, Matcher<SqlQuery> rightMatcher) {
        return xpath(operator, "//numeric_value_expression/*", orderedAllOf(leftMatcher, symbol(operator), rightMatcher));
    }
    
    @Factory
    public static Matcher<SqlQuery> concat(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return xpath("concat", "//character_value_expression/*", orderedAllOf(leftMatcher, rightMatcher));
    }
    
    @Factory
    public static Matcher<SqlQuery> column(Matcher<String> columnNameMatcher) {
        return xpath("column", "//column_reference", identifier(columnNameMatcher));
    }
    
    @Factory
    public static Matcher<SqlQuery> column(String columnName) {
        return column(equalTo(columnName));
    }
    
    @Factory
    private static <T> Matcher<SqlQuery> value(T value) {
        return new ValueMatcher<T>("value", NO_PATH, value, false);
    }
    
    @Factory
    public static Matcher<SqlQuery> literal(Number value) {
        return new ValueMatcher<Number>("literal", NO_PATH, value, false);
    }
    
    @Factory
    public static Matcher<SqlQuery> literal(String value) {
        return new ValueMatcher<String>("literal", NO_PATH, value, true);
    }
    
    @Factory
    public static <T> Matcher<SqlQuery> literal(T value) {
        return new ValueMatcher<T>("literal", NO_PATH, value, false);
    }
    
    @Factory
    public static <T> Matcher<SqlQuery> quotedLiteral(T value) {
        return new ValueMatcher<T>("quoted literal", NO_PATH, value, true);
    }
    
    @Factory
    public static <T> Matcher<SqlQuery> mayQuotedLiteral(T value) {
        return anyOf(value(value), quotedLiteral(value));
    }
    
    @Factory
    public static <T> Matcher<SqlQuery> literalContaining(T value) {
        return new ValueContainingMatcher<T>("literal containing", NO_PATH, value);
    }
    
    @Factory 
    public static <T> Matcher<SqlQuery> intervalLiteral(String value) {
        return xpath("interval", "//interval_literal/*", orderedAllOf(node("INTERVAL"), literal(value)));
    }
    
    @Factory 
    public static <T> Matcher<SqlQuery> dateLiteral(String value) {
        return xpath("date", "//date_literal/*", literal(value));
    }
    
    @Factory
    public static <T> Matcher<SqlQuery> overlaps() {
        return xpath("overlaps", "//overlaps_predicate/*", keyword("overlaps keyword", NO_PATH, "overlaps"));
    }
    
    @Factory
    private static Matcher<SqlQuery> keyword(String name, String xpath, String keyword) {
        return new StringMatcher(name, xpath, keyword, true);
    }
    
    @Factory
    private static Matcher<SqlQuery> value(String name, String xpath, String value) {
        return value(name, xpath, value, false);
    }
    
    @Factory
    private static Matcher<SqlQuery> value(String name, String xpath, String value, boolean ignoreCase) {
        return new StringMatcher(name, xpath, value, ignoreCase);
    }
    
    @Factory
    private static Matcher<SqlQuery> xpath(String name, String xpath, Matcher<? super SqlQuery> matcher) {
        return XPathMatcher.xpath(name, xpath, matcher);
    }
    
    @SafeVarargs
    @Factory
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery>... matchers) {
        return AllMatcher.allOf(matchers);
    }
    
    @SafeVarargs
    @Factory
    public static Matcher<SqlQuery> orderedAllOf(Matcher<? super SqlQuery>... matchers) {
        return Ordered.allOf(matchers);
    }
    
    private static Matcher<SqlQuery> notEmpty() {
        return new NotEmpty();
    }
    
    @Factory 
    private static Matcher<SqlQuery> print(Matcher<SqlQuery> query) {
        return new DebugMatcher(query);
    }
    
}
