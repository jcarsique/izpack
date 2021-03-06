/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2007-2009 Dennis Reil
 * Copyright 2010 Rene Krell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.core.rules.process;

import com.izforge.izpack.api.data.AutomatedInstallData;
import com.izforge.izpack.api.rules.CompareCondition;
import com.izforge.izpack.api.rules.ComparisonOperator;
import com.izforge.izpack.core.substitutor.VariableSubstitutorBase;
import com.izforge.izpack.core.substitutor.VariableSubstitutorImpl;
import com.izforge.izpack.util.Debug;

public class ComparenumericsCondition extends CompareCondition
{
    private static final long serialVersionUID = -5512232923336878003L;

    @Override
    public boolean isTrue()
    {
        boolean result = false;
        AutomatedInstallData installData = getInstallData();
        if (installData != null && operand1 != null && operand2 != null)
        {
            VariableSubstitutorBase subst = new VariableSubstitutorImpl(installData.getVariables());
            String arg1 = subst.substitute(operand1);
            String arg2 = subst.substitute(operand2);
            if (operator == null)
            {
                operator = ComparisonOperator.EQUAL;
            }
            try
            {
                int leftValue = Integer.valueOf(arg1);
                int rightValue = Integer.valueOf(arg2);
                switch (operator)
                {
                case EQUAL:
                    result = leftValue == rightValue;
                    break;
                case NOTEQUAL:
                    result = leftValue != rightValue;
                    break;
                case GREATER:
                    result = leftValue > rightValue;
                    break;
                case GREATEREQUAL:
                    result = leftValue >= rightValue;
                    break;
                case LESS:
                    result = leftValue < rightValue;
                    break;
                case LESSEQUAL:
                    result = leftValue <= rightValue;
                    break;
                default:
                    break;
                }
            }
            catch (NumberFormatException nfe)
            {
                Debug.log("One of the values to compare is not in numeric format");
            }
        }
        return result;
    }
}