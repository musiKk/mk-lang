package com.github.musiKk.mklang;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.V15;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmTest {
    public static void main(String[] args) throws Exception {
        // System.err.println(Paths.get("").toAbsolutePath());

        ClassVisitor tracer = new Tracer();

        ClassReader r = new ClassReader(new FileInputStream(new File("target/classes/test/Test.class")));
        r.accept(tracer, 0);
    }

    static class Tracer extends ClassVisitor {
        Tracer() {
            super(Opcodes.ASM9);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                String[] exceptions) {
            System.err.println("visit method:");
            System.err.println(" - access:     " + access);
            System.err.println(" - name:       " + name);
            System.err.println(" - descriptor: " + descriptor);
            System.err.println(" - signature:  " + signature);
            System.err.println(" - exceptions: " + Arrays.toString(exceptions));

            return new MethodTracer();
        }
    }

    static class MethodTracer extends MethodVisitor {
        MethodTracer() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            System.err.println("visitFieldInsn:");
            System.err.println(" - opcode:     " + opcode);
            System.err.println(" - owner:      " + owner);
            System.err.println(" - name:       " + name);
            System.err.println(" - descriptor: " + descriptor);
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
                Object... bootstrapMethodArguments) {
            System.err.println("visitInvokeDynamicInsn:");
            System.err.println(" - name:                     " + name);
            System.err.println(" - descriptor:               " + descriptor);
            System.err.println(" - bootstrapMethodHandle:    " + bootstrapMethodHandle);
            System.err.println(" - bootstrapMethodArguments: " + Arrays.toString(bootstrapMethodArguments));
        }
    }

    static void writeClass() throws Exception {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(
                V15,
                ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Comparable",
                null,
                "java/lang/Object",
                new String[] { "pkg/Measurable" });
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, Integer.valueOf(-1)).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, Integer.valueOf(0)).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, Integer.valueOf(1)).visitEnd();
        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();

        byte[] b = cw.toByteArray();
        FileOutputStream fos = new FileOutputStream(new File("/tmp/MyComparable.class"));
        fos.write(b);
        fos.flush();
        fos.close();

        System.err.println("done!");
    }
}
