package com.dhatim.sql.hamcrest;

import com.dhatim.sql.lang.PSQLLexer;
import com.dhatim.sql.lang.PSQLParser;
import com.dhatim.sql.lang.PSQLParser.SqlContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.xpath.XPath;

public class SqlQuery {
    
    private static class ParserListener extends BaseErrorListener {
        
        private final boolean raiseErrors;

        public ParserListener(boolean raiseErrors) {
            this.raiseErrors = raiseErrors;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            if (raiseErrors) {
                throw new SqlParserException(msg, e);
            }
        }
        
    }
    
    /**
     * Create parse of sql string
     * @param sql
     * @return sql string parsed into a <code>SqlQuery</code> object
     */
    public static SqlQuery of(String sql) {
        return new SqlQuery(parse(sql, true));
    }
    
    /**
     * For debugging purpose
     * @param sql
     */
    public static void printTree(String sql) {
        PSQLParser parser = parse(sql, false);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ParseTreeListener() {

            private int spaces = 0;

            @Override
            public void enterEveryRule(ParserRuleContext ctx) {
                ln("> " + nameOf(ctx.getRuleIndex()));
                spaces++;
            }

            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                spaces--;
                ln("< " + nameOf(ctx.getRuleIndex()));
            }

            @Override
            public void visitErrorNode(ErrorNode node) {
                ln("X " + node.getText());
            }

            @Override
            public void visitTerminal(TerminalNode node) {
                ln("| " + terminalNameOf(node.getSymbol().getType()) + " => " + node.getText());
            }
            
            private String nameOf(int id) {
                return parser.getRuleNames()[id];
            }
            
            private String terminalNameOf(int type) {
                return parser.getVocabulary().getDisplayName(type);
            }

            private void ln(String s) {
                System.out.println(toSpaces() + s);
            }

            private String toSpaces() {
                return space(spaces * 2);
            }
            
            private String space(int n) {
                return Stream.generate(() -> " ").limit(n).collect(Collectors.joining());
            }

        }, parser.sql());
    }
    
    private static PSQLParser parse(String sql, boolean raiseErrors) {
        CharStream inputStream = new ANTLRInputStream(sql);
        PSQLLexer lexer = new PSQLLexer(inputStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ParserListener(raiseErrors));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PSQLParser parser = new PSQLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserListener(raiseErrors));
        return parser;
    }
    
    private final PSQLParser parser;
    private final SqlContext tree;
    private final List<ParseTree> currentElements;
    
    private SqlQuery(PSQLParser parser) {
        this(parser, parser.sql());
    }
    
    private SqlQuery(PSQLParser parser, SqlContext tree) {
        this(parser, tree, Arrays.asList(tree));
    }
    
    private SqlQuery(PSQLParser parser, SqlContext tree, List<ParseTree> current) {
        this.parser = parser;
        this.tree = tree;
        this.currentElements = current;
    }
    
    public SqlQuery derive(String xpath) {
        List<ParseTree> list = currentElements.stream().flatMap(p -> XPath.findAll(p, xpath, parser).stream()).collect(Collectors.toList());
        return new SqlQuery(parser, tree, list);
    }
    
    public SqlQuery derive(int fromIndex, int toIndex) {
        return new SqlQuery(parser, tree, getChildren().subList(fromIndex, toIndex));
    }
    
    public List<ParseTree> getChildren() {
        return Collections.unmodifiableList(currentElements);
    }
    
    public List<String> getTextChildren() {
        return getChildren().stream().map(ParseTree::getText).collect(Collectors.toList());
    }
    
    public Stream<String> getTextStream() {
        return currentElements.stream().map(ParseTree::getText);
    }
    
    public Stream<ParseTree> children() {
        return currentElements.stream();
    }
    
    public String toString() {
        return getTextStream().collect(Collectors.joining(", ", "[", "]"));
    }
    
}
