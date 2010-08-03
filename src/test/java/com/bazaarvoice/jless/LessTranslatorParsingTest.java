package com.bazaarvoice.jless;

import com.bazaarvoice.jless.ast.Node;
import com.bazaarvoice.jless.print.Optimization;
import com.bazaarvoice.jless.print.Printer;
import org.apache.commons.io.IOUtils;
import org.parboiled.RecoveringParseRunner;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.support.ParsingResult;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

@Test
public class LessTranslatorParsingTest {

    private LessTranslator _transformer = new LessTranslator();

    protected ParsingResult<Node> parseLess(String fileName) {
        return parseLess(fileName, true);
    }

    protected ParsingResult<Node> parseLess(String fileName, boolean alwaysPrintStatus) {
        InputStream lessStream = getClass().getResourceAsStream("/less/" + fileName + ".less");
        String lessInput = "";
        
        try {
            lessInput = IOUtils.toString(lessStream, "UTF-8");
        } catch (IOException e) {
            System.out.println("Unable to read " + fileName + ".less");
            e.printStackTrace();
        }

        ParsingResult<Node> result = runParser(lessInput);

        Assert.assertFalse(result.hasErrors(), getResultStatus(result));

        if (alwaysPrintStatus) {
            System.out.print(getResultStatus(result));
            System.out.flush();
        }

        return result;
    }

    protected ParsingResult<Node> runParser(String lessInput) {
        return RecoveringParseRunner.run(_transformer.getParser().Document(), lessInput);
    }

    private String getResultStatus(ParsingResult<Node> result) {
        StringBuilder sb = new StringBuilder();

        if (result.hasErrors()) {
            sb.append("\nParse Errors:\n").append(ErrorUtils.printParseErrors(result));
        }

        /*if (result.parseTreeRoot != null) {
            sb.append("Parse Tree:\n").append(ParseTreeUtils.printNodeTree(result)).append('\n');
        }*/

        if (result.resultValue != null) {
//            sb.append("Abstract Syntax Tree:\n").append(GraphUtils.printTree(result.resultValue, new ToStringFormatter<Node>(null))).append('\n');
            sb.append(printResult(result));
        }

        return sb.toString();
    }

    protected String printResult(ParsingResult<Node> result) {
        Printer p = new Printer(Optimization.LESS_RUBY);
        result.resultValue.accept(p);
        return p.toString();
    }
    
    protected void runTestFor(String fileName) {
        parseLess(fileName);
    }

    public void testCss() {
        runTestFor("css");
    }

    public void testCss3() {
        runTestFor("css-3");
    }

    /*public void testBazaarvoiceDisplayShared() {
        runTestFor("bazaarvoiceDisplayShared");
    }*/

    @AfterMethod
    public void flushOutput() {
        System.out.flush();
    }
}
    