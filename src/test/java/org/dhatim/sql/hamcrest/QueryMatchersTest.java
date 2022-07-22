package org.dhatim.sql.hamcrest;

import org.junit.jupiter.api.Test;

import static org.dhatim.sql.hamcrest.QueryMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;


public class QueryMatchersTest {

    private static SqlQuery sql(String sql) {
        return SqlQuery.of(sql);
    }

    @Test
    public void testSimpleFromTableName() {
        assertThat(sql("select id from mytable"), query(from(table(equalToIgnoringCase("mytable")))));
    }

    @Test
    public void testSimpleLeftJoin() {
        assertThat(sql("SELECT * FROM mytable m LEFT JOIN myothertable o ON my.id = o.outid"), query(from(table(equalToIgnoringCase("mytable")), leftJoin(equalToIgnoringCase("myothertable")))));
    }

    @Test
    public void testPosition() {
        assertThat(sql("SELECT POSITION('3' IN '123456')"), query(position(any(), literal("123456"))));
    }

    @Test
    public void testNot() {
        assertThat(sql("SELECT NOT funcName('hello')"), query(not(call(is("funcName"), any()))));
    }

    @Test
    public void testCast() {
        assertThat(sql("SELECT '5'::integer"), query(select(cast(literal("5"), "integer"))));
    }

    @Test
    public void testNullCast() {
        assertThat(sql("SELECT NULL::integer"), query(select(nullCast("integer"))));
    }

    @Test
    public void testJsonCast() {
        assertThat(sql("SELECT '{}'::jsonb"), query(select(cast(literal("{}"), "jsonb"))));
    }

    @Test
    public void testRowSelect() {
        assertThat(sql("SELECT 'E', (4, 'E')"), query(select(row(literal(4), literal("E")))));
    }

    @Test
    public void testRowWhere() {
        assertThat(sql("SELECT * FROM t1 WHERE (col1, col2) = (1, 2)"), query(where(equal(row(column("col1"), column("col2")), row(literal(1), literal(2))))));
    }

    @Test
    public void testAnyUuidCast() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 = ANY('{}'::uuid[])"), query());
    }

    @Test
    public void testLike() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 LIKE '%Lorem'"), query(where(like(column("col1"), literal("%Lorem")))));
    }

    @Test
    public void testUUIDLiteral() {
        assertThat(sql("SELECT UUID 'c96ff414-8559-484c-bd43-c978130a5ee4'"), query(uuidLiteral("c96ff414-8559-484c-bd43-c978130a5ee4")));
        assertThat(sql("SELECT 'c96ff414-8559-484c-bd43-c978130a5ee4'::uuid"), query(uuidLiteral("c96ff414-8559-484c-bd43-c978130a5ee4")));
    }

    @Test
    public void testDateLiteral() {
        assertThat(sql("SELECT DATE '2010-10-10'"), query(dateLiteral("2010-10-10")));
        assertThat(sql("SELECT '2010-10-10'::date"), query(dateLiteral("2010-10-10")));
    }

    @Test
    public void testConds() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 = 1"), query(where(equal(column("col1"), literal(1)))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 > 1"), query(where(greater(column("col1"), literal(1)))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 < 1"), query(where(less(column("col1"), literal(1)))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 >= 1"), query(where(greaterEqual(column("col1"), literal(1)))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 <= 1"), query(where(lessEqual(column("col1"), literal(1)))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 <> 1"), query(where(unequal(column("col1"), literal(1)))));
    }

    @Test
    public void testAnd() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 = 1 AND col2 = 2"), query(where(and(equal(column("col1"), literal(1)), equal(column("col2"), literal(2))))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 = 1 AND col2 = 2 AND col3 = 3"), query(where(and(equal(column("col1"), literal(1)), equal(column("col2"), literal(2)), equal(column("col3"), literal(3))))));
    }

    @Test
    public void testOr() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 = 1 OR col2 = 2"), query(where(or(equal(column("col1"), literal(1)), equal(column("col2"), literal(2))))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 = 1 OR col2 = 2 OR col3 = 3"), query(where(or(equal(column("col1"), literal(1)), equal(column("col2"), literal(2)), equal(column("col3"), literal(3))))));
    }

    @Test
    public void testCompute() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 = 1 + 2"), query(where(equal(column("col1"), add(literal(1), literal(2))))));
        assertThat(sql("SELECT * FROM t1 WHERE col1 = (1 + 2) * 5"), query(where(equal(column("col1"), mul(add(literal(1), literal(2)), literal(5))))));
    }

}
