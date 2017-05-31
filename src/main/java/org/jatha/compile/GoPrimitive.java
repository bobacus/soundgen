/*
 * Jatha - a Common LISP-compatible LISP library in Java.
 * Copyright (C) 1997-2005 Micheal Scott Hewett
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For further information, please contact Micheal Hewett at
 *   hewett@cs.stanford.edu
 *
 */
/**
 * $Id: GoPrimitive.java,v 1.2 2005/06/30 00:24:23 olagus Exp $
 */
package org.jatha.compile;

import org.jatha.Jatha;
import org.jatha.dynatype.*;
import org.jatha.machine.*;

/**
 * <p>Implements the GO primitive</p>
 *
 * @author <a href="mailto:Ola.Bini@itc.ki.se">Ola Bini</a>
 * @version $Revision: 1.2 $
 */
public class GoPrimitive extends LispPrimitive {
    private long counter = 0L;

    public GoPrimitive(final Jatha lisp) {
        super(lisp,"GO",1);
    }

    public void Execute(final SECDMachine machine) {
        /*        System.err.println("our registers (before):");
        System.err.println("S: " + machine.S.value());
        System.err.println("E: " + machine.E.value());
        System.err.println("C: " + machine.C.value());
        System.err.println("D: " + machine.D.value());
        System.err.println("X: " + machine.X.value());

        System.err.println("x1");*/
        final LispValue tag = machine.S.pop().car();
        //        System.err.println("x2");
        machine.S.assign(f_lisp.NIL);
        //        System.err.println("x3");
        final LispValue code = machine.B.gethash(tag).car();
        //        System.err.println("x4 - " + machine.X.value().first().first());

        machine.E.assign(machine.X.value().first().first());
        //        System.err.println("x5 - " + machine.X.value().first().second());
        machine.D.assign(machine.X.value().first().second());
        /*        System.err.println("x6 - " + machine.X);
        System.err.println("x6 - " + machine.X.value());
        System.err.println("x6 - " + machine.X.value().first());
        System.err.println("x6 - " + machine.X.value().first().third());*/
        ((StandardLispHashTable)machine.B).assign((StandardLispHashTable)machine.X.value().first().third());
        //        System.err.println("x7");

        machine.C.assign(code);
        /*
        LispValue full = machine.D.value();
        full = removeUntilTagbody(machine,full);
        machine.D.assign(full);
        machine.C.assign(code);
        if(machine.E.value().car() == f_lisp.NIL) {
            machine.E.assign(machine.E.value().cdr()); // black magic.
        }
        */
        /*
        System.err.println("our registers (after):");
        System.err.println("S: " + machine.S.value());
        System.err.println("E: " + machine.E.value());
        System.err.println("C: " + machine.C.value());
        System.err.println("D: " + machine.D.value());
        System.err.println("X: " + machine.X.value());*/
    }

    public LispValue removeUntilTagbody(final SECDMachine machine,final LispValue input) {
        LispValue walker = input;
        LispValue inner = input.car();
        final java.util.List unbinds = new java.util.ArrayList();
        while(!(inner.car() instanceof TagbodyPrimitive) && walker != f_lisp.NIL) {
            if(inner.car() == machine.SP_UNBIND ) {
                unbinds.add(inner.first());
                unbinds.add(inner.second());
            }
            inner = inner.cdr();
            while(inner.car() == f_lisp.NIL && walker != f_lisp.NIL) {
                walker = walker.cdr();
                inner = walker.car();
            }
        }
        return (walker == f_lisp.NIL)?f_lisp.makeList(f_lisp.makeList(unbinds)):f_lisp.makeCons(f_lisp.makeList(unbinds).append(inner),walker.cdr());
    }

    public LispValue CompileArgs(final LispCompiler compiler, final SECDMachine machine, final LispValue args, final LispValue valueList, final LispValue code) throws CompilerException {
        final LispValue tag = args.car();
        if(!compiler.isLegalTag(tag)) {
            throw new IllegalArgumentException("Tag " + tag + " is not legal in this lexical context");
        }
        long nextVal = 0L;
        synchronized(this) {
            nextVal = counter++;
        }
        compiler.getRegisteredDos().put(new Long(nextVal),tag);
        return compiler.compileArgsLeftToRight(f_lisp.makeList(f_lisp.makeList(f_lisp.QUOTE,f_lisp.makeList(f_lisp.getEval().intern("#:T"+nextVal)))),valueList,code);
    }
}// GoPrimitive
