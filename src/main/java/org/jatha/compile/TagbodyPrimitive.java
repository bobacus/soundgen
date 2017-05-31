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
 * $Id: TagbodyPrimitive.java,v 1.2 2005/06/30 00:24:23 olagus Exp $
 */
package org.jatha.compile;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jatha.Jatha;
import org.jatha.dynatype.*;
import org.jatha.machine.*;

/**
 * <p>The TAGBODY special form. Read CLTL2 for more information</p>
 *
 * @author <a href="mailto:Ola.Bini@itc.ki.se">Ola Bini</a>
 * @version $Revision: 1.2 $
 */
public class TagbodyPrimitive extends LispPrimitive {
    public TagbodyPrimitive(final Jatha lisp) {
        super(lisp,"TAGBODY",1,Long.MAX_VALUE);
    }

    public void Execute(final SECDMachine machine) {
        machine.S.pop();
        machine.C.pop();
    }
    
    public LispValue CompileArgs(final LispCompiler compiler, final SECDMachine machine, final LispValue args, final LispValue valueList, final LispValue code) throws CompilerException {
        final Map tags = new HashMap();
        final List progns = new ArrayList();
        for(final Iterator iter = args.iterator();iter.hasNext();) {
            final LispValue val = (LispValue)iter.next();
            if(val.basic_symbolp()) {
                tags.put(val,new Integer(progns.size()));
            } else if(val.basic_listp()) {
                progns.add(val);
            }
        }
        final Map tags2 = new HashMap();
        for(final Iterator iter = tags.keySet().iterator();iter.hasNext();) {
            final LispValue tag = (LispValue)iter.next();
            final int index = ((Integer)tags.get(tag)).intValue();
            tags2.put(tag,progns.subList(index,progns.size()));
        }

        compiler.getLegalTags().push(tags.keySet());

        final Map tags3 = new HashMap();
        for(final Iterator iter = tags2.keySet().iterator();iter.hasNext();) {
            final LispValue tag = (LispValue)iter.next();
            final LispValue allProgns = f_lisp.makeList(f_lisp.makeCons(f_lisp.getEval().intern("PROGN"),f_lisp.makeList((List)tags2.get(tag))));
            tags3.put(tag,compiler.compileArgsLeftToRight(allProgns,valueList,code));
        }

        final Map metaCode = new HashMap();
        for(final Iterator iter = tags3.keySet().iterator();iter.hasNext();) {
            final LispValue tag = (LispValue)iter.next();
            final LispValue codeX = f_lisp.makeList(f_lisp.QUOTE,(LispValue)tags3.get(tag));
            final LispValue code2 = compiler.compile(codeX,valueList,f_lisp.NIL);
            metaCode.put(tag,code2);
        }

        final LispValue all = f_lisp.makeList(f_lisp.makeCons(f_lisp.getEval().intern("PROGN"),f_lisp.makeList(progns)));
        final LispValue theCode = compiler.compileArgsLeftToRight(all,valueList,f_lisp.makeList(code.car()));

        compiler.getLegalTags().pop();

        LispValue loadBindings = f_lisp.NIL;
        LispValue unloadBindings = f_lisp.NIL;
        for(final Iterator iter = compiler.getRegisteredDos().keySet().iterator();iter.hasNext();) {
            final Long key = (Long)iter.next();
            final LispValue tag = (LispValue)compiler.getRegisteredDos().get(key);
            if(tags.containsKey(tag)) {
                final LispValue tagSym = f_lisp.getEval().intern("#:T"+key);
                loadBindings = f_lisp.makeCons(machine.LDC,f_lisp.makeCons((LispValue)tags3.get(tag),
                                           f_lisp.makeCons(machine.SP_BIND,f_lisp.makeCons(tagSym,loadBindings))));
                unloadBindings = f_lisp.makeCons(machine.SP_UNBIND,f_lisp.makeCons(tagSym,unloadBindings));
                iter.remove();
            }            
        }
        return loadBindings.append(f_lisp.makeList(machine.TAG_B)).append(theCode).append(f_lisp.makeList(machine.TAG_E)).append(unloadBindings).append(code.cdr());
    }
}// TagbodyPrimitive
