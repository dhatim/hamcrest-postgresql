package org.dhatim.sql.hamcrest;


import org.dhatim.sql.hamcrest.matcher.*;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class QueryMatchers {

    private static final String NO_PATH = "";

    @SafeVarargs
    public static Matcher<SqlQuery> query(Matcher<? super SqlQuery>... matchers) {
        return allOf(matchers);
    }

    @SafeVarargs
    public static Matcher<SqlQuery> select(Matcher<? super SqlQuery>... matchers) {
        return xpath("select", "//select_list", orderedAllOf(matchers));
    }

    public static Matcher<SqlQuery> from(Matcher<String> tableNameMatcher) {
        return from(table(tableNameMatcher));
    }

    @SafeVarargs
    public static Matcher<SqlQuery> from(Matcher<? super SqlQuery>... matchers) {
        return xpath("from", "//from_clause", allOf(matchers));
    }

    @SafeVarargs
    public static Matcher<SqlQuery> having(Matcher<? super SqlQuery>... matchers) {
        return having(allOf(matchers));
    }

    public static Matcher<SqlQuery> having() {
        return having(notEmpty());
    }

    private static Matcher<SqlQuery> having(Matcher<? super SqlQuery> matcher) {
        return xpath("having", "//having_clause", matcher);
    }

    public static Matcher<SqlQuery> where(Matcher<? super SqlQuery> matcher) {
        return xpath("where", "//where_clause", matcher);
    }

    public static Matcher<SqlQuery> table(Matcher<String> identifierMatcher) {
        return xpath("table", "//table_primary//table_name", identifier(identifierMatcher));
    }

    public static Matcher<SqlQuery> identifier(Matcher<String> identifierMatcher) {
        return new IdentifierMatcher(identifierMatcher);
    }

    /*    public static FromMatcher innerJoin(String tableName) {

    }

        public static FromMatcher innerJoin(String tableName, WhereMatcher... matchers) {

    }*/

    public static Matcher<SqlQuery> cast(Matcher<? super SqlQuery> operand, String type) {
        return xpath("primary", "//casted_value_expression_primary/*", orderedAllOf(xpath("operand", "//value_expression_primary", operand), node("::"), keyword("type", "//cast_target", type)));
    }

    public static Matcher<SqlQuery> nullCast(String type) {
        return xpath("primary", "//null_casted_value_expression/*", orderedAllOf(node("NULL"), node("::"), xpath("cast type", "//cast_target", keyword("//data_type", NO_PATH, type))));
    }

    @SafeVarargs
    public static Matcher<SqlQuery> leftJoin(Matcher<String> tableNameMatcher, Matcher<? super SqlQuery>... matchers) {
        return join(allOf(keyword("join type", "//join_type", "left"), table(tableNameMatcher), allOf(matchers)));
    }

    @SafeVarargs
    public static Matcher<SqlQuery> row(Matcher<? super SqlQuery>... matchers) {
        return xpath("row", "//row_value_constructor/*", orderedAllOf(matchers));
    }

    public static Matcher<SqlQuery> leftJoin(Matcher<String> tableNameMatcher) {
        return join(allOf(keyword("join type", "//join_type", "left"), table(tableNameMatcher)));
    }

    private static Matcher<SqlQuery> join(Matcher<? super SqlQuery> matcher) {
        return xpath("join", "//table_reference/joined_table/joined_table_primary", matcher);
    }

    private static Matcher<SqlQuery> comparison(String name, String operator, Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return xpath(name, "//comparison_predicate/*", orderedAllOf(left, symbol("//comp_op", operator), right));
    }

    public static Matcher<SqlQuery> equality(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return comparison("equality", "=", left, right);
    }

    public static <T> Matcher<SqlQuery> equality(Matcher<? super SqlQuery> left, T right) {
        return equality(left, value(right));
    }

    public static Matcher<SqlQuery> equal(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return equality(left, right);
    }

    public static <T> Matcher<SqlQuery> equal(Matcher<? super SqlQuery> left, T right) {
        return equality(left, right);
    }

    public static Matcher<SqlQuery> unequal(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return xpath("unequality", "//comparison_predicate/*", orderedAllOf(left, token("//comp_op", "NOT_EQUAL"), right));
    }

    public static <T> Matcher<SqlQuery> unequal(Matcher<? super SqlQuery> left, T right) {
        return unequal(left, right);
    }

    public static Matcher<SqlQuery> greaterEqual(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return comparison("greaterEqual", ">=", left, right);
    }

    public static <T> Matcher<SqlQuery> greaterEqual(Matcher<? super SqlQuery> left, T right) {
        return greaterEqual(left, value(right));
    }

    public static Matcher<SqlQuery> greater(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return comparison("greater", ">", left, right);
    }

    public static <T> Matcher<SqlQuery> greater(Matcher<? super SqlQuery> left, T right) {
        return greaterEqual(left, value(right));
    }

    public static Matcher<SqlQuery> lessEqual(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return comparison("lessEqual", "<=", left, right);
    }

    public static <T> Matcher<SqlQuery> lessEqual(Matcher<? super SqlQuery> left, T right) {
        return lessEqual(left, value(right));
    }

    public static Matcher<SqlQuery> less(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return comparison("less", "<", left, right);
    }

    public static <T> Matcher<SqlQuery> less(Matcher<? super SqlQuery> left, T right) {
        return less(left, value(right));
    }

    private static Matcher<SqlQuery> symbol(String xpath, String literal) {
        return xpath(String.format("symbol '%s'", literal), String.format("%s//'%s'", xpath, literal), notEmpty());
    }

    private static Matcher<SqlQuery> token(String xpath, String token) {
        return xpath(String.format("token %s", token), String.format("%s//%s", xpath, token), notEmpty());
    }

    private static Matcher<SqlQuery> token(String token) {
        return token("", token);
    }

    public static Matcher<SqlQuery> symbol(String literal) {
        return symbol("", literal);
    }

    public static Matcher<SqlQuery> node(String nodeName) {
        return new TokenMatcher(String.format("token '%s'", nodeName), "", nodeName);
        //return xpath(String.format("node name '%s'", nodeName), nodeName, notEmpty());
    }

    /*    public static WhereMatcher greater(OperandMatcher left, OperandMatcher right) {

    }

        public static <T> WhereMatcher greater(OperandMatcher left, T right) {
        return greater(left, value(right));
    }

        public static OperandMatcher count() {

    }

        public static <T> OperandMatcher value(T value) {

    }*/

    @SafeVarargs
    public static Matcher<SqlQuery> call(Matcher<String> functionName, Matcher<SqlQuery>... params) {
        return xpath("function", "//routine_invocation/*", orderedAllOf(xpath("function name", "//function_name/*", identifier(functionName)), xpath("arguments", "//sql_argument_list/*", orderedAllOf(params))));
    }

    public static <T> Matcher<SqlQuery> position(Matcher<SqlQuery> searched, Matcher<SqlQuery> text) {
        return xpath("position", "//position_invocation/*", xpath("arguments", "//string_expression/*", orderedAllOf(searched, text)));
    }

    private static Matcher<SqlQuery> compute(String name, String containerRule, Matcher<SqlQuery> leftMatcher, String operator, Matcher<SqlQuery> rightMatcher) {
        return xpath(name, "//numeric_value_expression/*", orderedAllOf(rule(containerRule, containerRule, leftMatcher), symbol(operator), rule(containerRule, containerRule, rightMatcher)));
    }

    public static Matcher<SqlQuery> add(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return compute("addition", "term", leftMatcher, "+", rightMatcher);
    }

    public static Matcher<SqlQuery> sub(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return compute("substraction", "term", leftMatcher, "-", rightMatcher);
    }

    private static Matcher<SqlQuery> compute2(String name, String containerRule, Matcher<SqlQuery> leftMatcher, String operator, Matcher<SqlQuery> rightMatcher) {
        return xpath(name, "//numeric_value_expression/term/*", orderedAllOf(rule(containerRule, containerRule, leftMatcher), symbol(operator), rule(containerRule, containerRule, rightMatcher)));
    }

    public static Matcher<SqlQuery> mul(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return compute2("multiplication", "factor", leftMatcher, "*", rightMatcher);
    }

    public static Matcher<SqlQuery> div(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return compute2("division", "factor", leftMatcher, "/", rightMatcher);
    }

    public static Matcher<SqlQuery> concat(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher) {
        return xpath("concat", "//character_value_expression/*", orderedAllOf(leftMatcher, rightMatcher));
    }

    @SuppressWarnings("unchecked")
    public static Matcher<SqlQuery> concat(Matcher<SqlQuery> leftMatcher, Matcher<SqlQuery> rightMatcher, Matcher<SqlQuery> thirdMatcher, Matcher<SqlQuery>... others) {
        Matcher<SqlQuery>[] all = Stream.concat(Stream.of(leftMatcher, rightMatcher, thirdMatcher), Stream.of(others)).toArray(Matcher[]::new);
        return xpath("concat", "//character_value_expression/*", orderedAllOf(all));
    }

    public static Matcher<SqlQuery> column(Matcher<String> columnNameMatcher) {
        return xpath("column", "//column_reference", identifier(columnNameMatcher));
    }

    public static Matcher<SqlQuery> column(String columnName) {
        return column(equalTo(columnName));
    }

    private static <T> Matcher<SqlQuery> value(T value) {
        return new ValueMatcher<T>("value", NO_PATH, value, false);
    }

    public static Matcher<SqlQuery> uuidLiteral(String uuid) {
        return new ValueMatcher<String>("literal", "//uuid_literal/*", uuid, true);
    }

    public static Matcher<SqlQuery> literal(Number value) {
        return new ValueMatcher<Number>("literal", NO_PATH, value, false);
    }

    public static Matcher<SqlQuery> literal(String value) {
        return new ValueMatcher<String>("literal", NO_PATH, value, true);
    }

    public static <T> Matcher<SqlQuery> literal(T value) {
        return new ValueMatcher<T>("literal", NO_PATH, value, false);
    }

    public static <T> Matcher<SqlQuery> quotedLiteral(T value) {
        return new ValueMatcher<T>("quoted literal", NO_PATH, value, true);
    }

    public static <T> Matcher<SqlQuery> mayQuotedLiteral(T value) {
        return anyOf(value(value), quotedLiteral(value));
    }

    public static <T> Matcher<SqlQuery> literalContaining(T value) {
        return new ValueContainingMatcher<T>("literal containing", NO_PATH, value);
    }

    public static <T> Matcher<SqlQuery> intervalLiteral(String value) {
        return xpath("interval", "//interval_literal/*", literal(value));
    }

    public static <T> Matcher<SqlQuery> dateLiteral(String value) {
        return xpath("date", "//date_literal/*", literal(value));
    }

    public static <T> Matcher<SqlQuery> overlaps() {
        return xpath("overlaps", "//overlaps_predicate/*", keyword("overlaps keyword", NO_PATH, "overlaps"));
    }

    public static Matcher<SqlQuery> like(Matcher<SqlQuery> operand, Matcher<SqlQuery> pattern) {
        return xpath("like matcher", "//pattern_matching_predicate/*", orderedAllOf(operand, keyword("like keyword", NO_PATH, "like"), pattern));
    }

    private static Matcher<SqlQuery> keyword(String name, String xpath, String keyword) {
        return new StringMatcher(name, xpath, keyword, true);
    }

    private static Matcher<SqlQuery> value(String name, String xpath, String value) {
        return value(name, xpath, value, false);
    }

    private static Matcher<SqlQuery> value(String name, String xpath, String value, boolean ignoreCase) {
        return new StringMatcher(name, xpath, value, ignoreCase);
    }

    private static Matcher<SqlQuery> xpath(String name, String xpath, Matcher<? super SqlQuery> matcher) {
        return XPathMatcher.xpath(name, xpath, matcher);
    }

    private static Matcher<SqlQuery> rule(String name, String ruleName, Matcher<? super SqlQuery> matcher) {
        return xpath(name, "//" + ruleName + "/*", matcher);
    }

    @SafeVarargs
    public static Matcher<SqlQuery> allOf(Matcher<? super SqlQuery>... matchers) {
        return AllMatcher.allOf(matchers);
    }

    public static Matcher<SqlQuery> not(Matcher<SqlQuery> matcher) {
        return xpath("not", "//boolean_factor/*", orderedAllOf(keyword("not keyword", NO_PATH, "not"), matcher));
    }

    public static Matcher<SqlQuery> and(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return rule("and", "and_predicate", orderedAllOf(left, token("AND"), right));
    }

    public static Matcher<SqlQuery> and(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> middle, Matcher<? super SqlQuery> right) {
        return rule("and", "and_predicate", orderedAllOf(left, token("AND"), middle, token("AND"), right));
    }

    public static Matcher<SqlQuery> or(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> right) {
        return rule("or", "or_predicate", orderedAllOf(left, token("OR"), right));
    }

    public static Matcher<SqlQuery> or(Matcher<? super SqlQuery> left, Matcher<? super SqlQuery> middle, Matcher<? super SqlQuery> right) {
        return rule("or", "or_predicate", orderedAllOf(left, token("OR"), middle, token("OR"), right));
    }

    public static Matcher<SqlQuery> any() {
        return new IsAnything<SqlQuery>();
    }

    @SafeVarargs
    public static Matcher<SqlQuery> orderedAllOf(Matcher<? super SqlQuery>... matchers) {
        return Ordered.allOf(matchers);
    }

    private static Matcher<SqlQuery> notEmpty() {
        return new NotEmpty();
    }

    private static Matcher<SqlQuery> print(Matcher<SqlQuery> query) {
        return new DebugMatcher(query);
    }

}
