package com.github.musiKk.mklang.compiler.output;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V15;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.github.musiKk.mklang.ast.FunctionSignatureDeclaration;
import com.github.musiKk.mklang.ast.FunctionSignatureDeclaration.Parameter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Outputter {

    private String name;
    private ClassWriter cw;

    public static Outputter createClass(String name) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);
        cw.visit(V15, ACC_PUBLIC, name, null, "java/lang/Object", null);
        return new Outputter(name, cw);
    }

    public MethodOutputter createMethod(FunctionSignatureDeclaration signature) {
        var argTypes = signature.getParameters().stream()
            .map(Outputter::typeToParam)
            .toList();
        var methodDescriptor = Type.getMethodDescriptor(Type.VOID_TYPE, argTypes.toArray(new Type[0]));


        MethodVisitor mv = cw.visitMethod(
            Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
            signature.getName().toString(),
            methodDescriptor, null, null);
        return new MethodOutputter(mv);
    }

    private static Type typeToParam(Parameter param) {
        return Type.getObjectType(param.getType().toString().replace('.', '/'));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public class MethodOutputter {
        MethodVisitor mv;

        public void addMul() {
            mv.visitInsn(Opcodes.IMUL);
        }

        public void addAdd() {
            mv.visitInsn(Opcodes.IADD);
        }

        public void addIntConstant(long value) {
            mv.visitLdcInsn(value);
        }

        public void addStringConstant(String string) {
            mv.visitLdcInsn(string);
        }

        public Outputter finish() {
            return Outputter.this;
        }
    }

}
