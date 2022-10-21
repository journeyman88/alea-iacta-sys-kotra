/*
 * Copyright 2020 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.systems.kotra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.unknowndomain.alea.messages.MsgBuilder;
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.roll.LocalizedResult;

/**
 *
 * @author journeyman
 */
public class KotraResults extends LocalizedResult
{
    private final static String BUNDLE_NAME = "net.unknowndomain.alea.systems.kotra.RpgSystemBundle";
    
    private final List<SingleResult<Integer>> diceResults;
    private int successLevel = 0;
    private int triumphCount = 0;
    private int disasterCount = 0;
    
    public KotraResults(List<SingleResult<Integer>> results)
    {
        List<SingleResult<Integer>> tmp = new ArrayList<>(results.size());
        tmp.addAll(results);
        this.diceResults = Collections.unmodifiableList(tmp);
    }

    public List<SingleResult<Integer>> getResults()
    {
        return diceResults;
    }

    @Override
    protected void formatResults(MsgBuilder messageBuilder, boolean verbose, int indentValue)
    {
        messageBuilder.append(translate("kotra.results.successLevel."+successLevel)).appendNewLine();
        if (triumphCount > 0)
        {
            messageBuilder.append(translate("kotra.results.triumphs", triumphCount)).appendNewLine();
        }
        if (disasterCount > 0)
        {
            messageBuilder.append(translate("kotra.results.disasters", disasterCount)).appendNewLine();
        }
        if (verbose)
        {
            messageBuilder.append("Roll ID: ").append(getUuid()).appendNewLine();
            messageBuilder.append(translate("kotra.results.diceResults")).append(" [ ");
            for (SingleResult<Integer> t : getResults())
            {
                messageBuilder.append("( ").append(t.getLabel()).append(" => ");
                messageBuilder.append(t.getValue()).append(") ");
            }
            messageBuilder.append("]").appendNewLine();
        }
    }

    @Override
    protected String getBundleName()
    {
        return BUNDLE_NAME;
    }

    public int getSuccessLevel()
    {
        return successLevel;
    }

    public void setSuccessLevel(int successLevel)
    {
        this.successLevel = successLevel;
    }

    public void addTriumph()
    {
        triumphCount++;
    }

    public void addDisaster()
    {
        disasterCount++;
    }
    
    public int getTriumphCount() 
    {
        return triumphCount;
    }
    
    public int getDisasterCount()
    {
        return disasterCount;
    }
    
}
