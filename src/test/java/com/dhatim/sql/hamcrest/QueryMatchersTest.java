package com.dhatim.sql.hamcrest;

import static org.hamcrest.Matchers.*;
import static com.dhatim.sql.hamcrest.QueryMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

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
    
}
