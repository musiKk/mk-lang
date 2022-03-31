package com.github.musiKk.mklang;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V15;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Collectors;

import com.github.musiKk.mklang.Compiler.Context;
import com.github.musiKk.mklang.ast.CompilationUnit;
import com.github.musiKk.mklang.ast.DataDeclarationStatement;
import com.github.musiKk.mklang.ast.DottedName;
import com.github.musiKk.mklang.ast.Expression;
import com.github.musiKk.mklang.ast.Expression.FunctionCall;
import com.github.musiKk.mklang.ast.Expression.IntConstant;
import com.github.musiKk.mklang.ast.Expression.StringConstant;
import com.github.musiKk.mklang.ast.FieldDeclaration;
import com.github.musiKk.mklang.ast.Statement;
import com.github.musiKk.mklang.ast.Visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Compiler {

    private File destination;

    public static void main(String[] args) throws Exception {
        Example e = new Example(new FileReader("src/main/resources/test.tst"));
        // e.disable_tracing();
        CompilationUnit cu = e.compilationUnit();

        Compiler c = new Compiler(new File("/tmp/asdf/output"));
        c.compile(cu, "test");
    }

    public void compile(CompilationUnit cu, String name) throws Exception {
        if (destination.exists()) {
            if (destination.isFile()) {
                throw new RuntimeException("destination " + destination + " is a file");
            }
            Files.walkFileTree(destination.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    file.toFile().delete();
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (!dir.toFile().equals(destination)) {
                        dir.toFile().delete();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        destination.mkdirs();

        Context c = new Context(destination, name);
        CompilationUnitVisitor cuv = new CompilationUnitVisitor(c);
        cuv.visitCompilationUnit(cu);
    }

    @AllArgsConstructor
    static class Context {
        File destination;
        String name;
    }

}

@AllArgsConstructor
class CompilationUnitVisitor extends Visitor.Adapter {

    private Context c;

    @Override
    public void visitCompilationUnit(CompilationUnit cu) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);
        // ClassVisitor cv = new CheckClassAdapter(cw);
        ClassVisitor cv = cw;
        cv.visit(V15, ACC_PUBLIC, c.name, null, "java/lang/Object", null);

        // <init>
        MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        Visitor ddsv = new DataDeclarationStatementVisitor(c);
        Visitor tlev = new CodeEmitterVisitor(mv);

        for (Statement s : cu.getStatements()) {
            s.accept(ddsv);
            s.accept(tlev);
        }

        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        cv.visitEnd();

        try {
            String fileName = c.name + ".class";
            byte[] bytes = cw.toByteArray();
            System.err.println("writing file " + fileName + " (" + bytes.length + " bytes)");
            FileOutputStream fos = new FileOutputStream(new File(c.destination, fileName));
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

@AllArgsConstructor
class CodeEmitterVisitor extends Visitor.Adapter {
    MethodVisitor mv;

    @Override
    public void visitFunctionCallExpression(FunctionCall functionCall) {
        if (new DottedName("print").equals(functionCall.getName())) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            for (Expression e : functionCall.getArgs()) {
                e.accept(this);
            }

            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        } else {
            System.err.println("unknown function " + functionCall.getName());
        }
    }

    @Override
    public void visitStringConstant(StringConstant stringConstant) {
        mv.visitLdcInsn(stringConstant.getValue());
    }

    @Override
    public void visitIntConstant(IntConstant intConstant) {
        mv.visitLdcInsn(intConstant.getValue());
    }
}

@AllArgsConstructor
class DataDeclarationStatementVisitor extends Visitor.Adapter {

    private Context c;

    @Override
    public void visitDataDeclarationStatement(DataDeclarationStatement dataDeclarationStatement) {
        String className = c.name + "$" + dataDeclarationStatement.getName();

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);

        cw.visit(V15, ACC_PUBLIC, className, null, "java/lang/Object", null);

        for (FieldDeclaration d : dataDeclarationStatement.getFieldDeclarations()) {
            String type = "L" + d.getType().getSegments().stream().collect(Collectors.joining(".")) + ";";
            cw.visitField(ACC_PUBLIC, d.getName(), type, null, null).visitEnd();
        }

        cw.visitEnd();

        try {
            String fileName = className + ".class";
            byte[] bytes = cw.toByteArray();
            System.err.println("writing file " + fileName + " (" + bytes.length + " bytes)");
            FileOutputStream fos = new FileOutputStream(new File(c.destination, fileName));
            fos.write(cw.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
