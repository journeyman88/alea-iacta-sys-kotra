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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.random.SingleResultComparator;
import net.unknowndomain.alea.random.dice.DicePool;
import net.unknowndomain.alea.random.dice.bag.D12;
import net.unknowndomain.alea.random.dice.bag.D4;
import net.unknowndomain.alea.random.dice.bag.D6;
import net.unknowndomain.alea.random.dice.bag.D8;
import net.unknowndomain.alea.roll.GenericResult;
import net.unknowndomain.alea.roll.GenericRoll;

/**
 *
 * @author journeyman
 */
public class KotraRoll implements GenericRoll
{
    
    private final DicePool<D12> limitPool;
    private final DicePool<D8> traitPool;
    private final DicePool<D6> difficultyPool;
    private final DicePool<D4> threatPool;
    private final Set<KotraModifiers> mods;
    private final Locale lang;
    
    public KotraRoll(KotraOptions opts, Locale lang)
    {
        this.mods = new HashSet<>();
        this.mods.addAll(opts.getModifiers());
        int limit = 0, trait = 0, diff = 0, threat = 0, bonus = 0;
        if (opts.getLimitNumber() != null)
        {
            limit += opts.getLimitNumber();
            if (limit > 6)
            {
                limit = 6;
            }
        }
        if (opts.getTraitNumber() != null)
        {
            trait += opts.getTraitNumber();
            if (trait > 6)
            {
                trait = 6;
            }
        }
        if (opts.getDifficultyNumber() != null)
        {
            diff += opts.getDifficultyNumber();
            if (diff > 6)
            {
                diff = 6;
            }
        }
        if (opts.getThreatNumber() != null)
        {
            threat += opts.getThreatNumber();
            if (threat > 6)
            {
                threat = 6;
            }
        }
        if (diff > 0)
        {
            trait -=diff;
            if (trait < 0)
            {
                limit += trait;
                if (limit < 0)
                {
                    limit = 0;
                }
                trait = 0;
            }
        }
        if (threat > 0)
        {
            diff -= threat;
            if (diff < 0)
            {
                diff = 0;
            }
        }
        this.limitPool = new DicePool<>(D12.INSTANCE, limit);
        this.traitPool = new DicePool<>(D8.INSTANCE, trait);
        this.difficultyPool = new DicePool<>(D6.INSTANCE, diff);
        this.threatPool = new DicePool<>(D4.INSTANCE, threat);
        this.lang = lang;
    }
    
    @Override
    public GenericResult getResult()
    {
        List<SingleResult<Integer>> positiveResults = new LinkedList<>();
        positiveResults.addAll(limitPool.getResults());
        positiveResults.addAll(traitPool.getResults());
        List<SingleResult<Integer>> negativeResults = new LinkedList<>();
        negativeResults.addAll(difficultyPool.getResults());
        negativeResults.addAll(threatPool.getResults());
        KotraResults results = buildResults(positiveResults, negativeResults);
        results.setVerbose(mods.contains(KotraModifiers.VERBOSE));
        results.setLang(lang);
        return results;
    }
    
    private KotraResults buildResults(List<SingleResult<Integer>> positiveResults, List<SingleResult<Integer>> negativeResults)
    {
        SingleResultComparator comp = new SingleResultComparator(true);
        positiveResults.sort(comp);
        negativeResults.sort(comp);
        List<SingleResult<Integer>> allRes = new ArrayList<>(positiveResults.size() + negativeResults.size());
        allRes.addAll(positiveResults);
        allRes.addAll(negativeResults);
        KotraResults results = new KotraResults(allRes);
        for (SingleResult<Integer> pos : positiveResults)
        {
            if (pos.getValue() == 12)
            {
                results.addTriumph();
            }
        }
        for (SingleResult<Integer> neg : negativeResults)
        {
            if (neg.getValue() == 1)
            {
                results.addDisaster();
            }
        }
        int maxPos = 1;
        if (!positiveResults.isEmpty())
        {
            maxPos = positiveResults.get(0).getValue();
        }
        int maxNeg = 6;
        if (!negativeResults.isEmpty())
        {
            maxNeg = negativeResults.get(0).getValue();
        }
        if (maxPos >= 6)
        {
            results.setSuccessLevel(results.getSuccessLevel()+1);
        }
        if (maxNeg >= 6)
        {
            results.setSuccessLevel(results.getSuccessLevel()+1);
        }
        if (mods.contains(KotraModifiers.OVERDRIVE))
        {
            if ((maxPos >= 6) && (maxPos < 12))
            {
                results.addTriumph();
            }
            if (maxNeg >= 6)
            {
                results.addTriumph();
            }
            if ((maxNeg < 6) && (maxNeg > 1))
            {
                results.addDisaster();
            }
            if (maxPos < 6)
            {
                results.addDisaster();
            }
        }
        return results;
    }
}
