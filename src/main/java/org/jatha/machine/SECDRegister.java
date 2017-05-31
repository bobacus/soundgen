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

package org.jatha.machine;

import org.jatha.dynatype.*;
import org.jatha.Jatha;


// @date    Mon Feb  3 19:02:37 1997
/**
 * SECDRegister is essentially a Stack, with
 * an additional <tt>assign()</tt> method
 * which resets the value, and a <tt>value()</tt>
 * method which retrieves the whole stack.
 *
 * @author  Micheal S. Hewett    hewett@cs.stanford.edu
 */
public class SECDRegister extends StandardLispSymbol
{

  public SECDRegister(Jatha lisp, String name)
  {
    super(lisp, name);
    assign(lisp.NIL);
  }


  // POP and PUSH are defined by LispSymbol

  /**
   * Assign sets the value of a register.
   *
   * @param newValue
   */
  public void assign(LispValue newValue)
  {
    setf_symbol_value(newValue);
  }

  /**
   * Returns the value of a register.
   *
   * @return LispValue
   */
  public LispValue value()
  {
    return symbol_value();
  }



}


