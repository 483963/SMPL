package lang.interpreter;

import java.io.File;
import java.io.FileReader;

import lang.parser.ast.ASTCode;
import lang.parser.ast.SMPL;
import lang.parser.ast.SMPLVisitor;

public class Interpreter {

    private static void usage() {
        System.out.println("Usage: SMPL [-d1] <source>");
        System.out.println("          -d1 -- output AST");
    }

    public static void main(String args[]) {
        boolean debugAST = false;
        String file = null;

        switch (args.length) {
            case 1:
                file = args[0];
                break;
            case 2:
                if (!args[0].equals("-d1")) {
                    debugAST = true;
                    file = args[1];
                    break;
                }
            default:
                usage();
                return;

        }

        try {
            SMPL language = new SMPL(new FileReader(file));
            ASTCode parser = language.code();
            SMPLVisitor nodeVisitor;
            if (debugAST)
                nodeVisitor = new ParserDebugger();
            else
                nodeVisitor = new Parser();
            parser.jjtAccept(nodeVisitor, null);
        } catch (ExceptionSemantic e) {
            System.err.println(e.getMessage());
        } catch (Flow.Return e) {
            System.out.println("Return outside function");
        } catch (Flow.Break e) {
            System.out.println("Break outside loop");
        } catch (Flow.Continue e) {
            System.out.println("continue outside loop");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
