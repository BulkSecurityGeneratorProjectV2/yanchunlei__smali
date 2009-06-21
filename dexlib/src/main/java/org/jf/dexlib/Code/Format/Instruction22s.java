/*
 * [The "BSD licence"]
 * Copyright (c) 2009 Ben Gruver
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib.Code.Format;

import org.jf.dexlib.DexFile;
import org.jf.dexlib.IndexedItem;
import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.Opcode;
import org.jf.dexlib.Util.NumberUtils;

public class Instruction22s extends Instruction
{
    public static final Instruction.InstructionFactory Factory = new Factory();

    public Instruction22s(DexFile dexFile, Opcode opcode, byte regA, byte regB, short litC) {
        super(dexFile, opcode, (IndexedItem)null);

        if (regA >= 1<<4 ||
            regB >= 1<<4) {
            throw new RuntimeException("The register number must be less than v16");
        }

        encodedInstruction = new byte[4];
        encodedInstruction[0] = opcode.value;
        encodedInstruction[1] = (byte)((regB << 4) | regA);
        encodedInstruction[2] = (byte)litC;
        encodedInstruction[3] = (byte)(litC >> 8);
    }

    private Instruction22s(DexFile dexFile, Opcode opcode, byte[] rest) {
        super(dexFile, opcode, rest);
    }

    private Instruction22s() {
    }

    public Format getFormat() {
        return Format.Format22s;
    }

    protected Instruction makeClone() {
        return new Instruction22s();
    }

    private static class Factory implements Instruction.InstructionFactory {
        public Instruction makeInstruction(DexFile dexFile, Opcode opcode, byte[] rest) {
            return new Instruction22s(dexFile, opcode, rest);
        }
    }


    public byte getRegisterA() {
        return NumberUtils.decodeLowUnsignedNibble(encodedInstruction[1]);
    }

    public byte getRegisterB() {
        return NumberUtils.decodeHighUnsignedNibble(encodedInstruction[1]);
    }

    public short getLiteral() {
        return NumberUtils.decodeShort(encodedInstruction[2], encodedInstruction[3]);
    }
}