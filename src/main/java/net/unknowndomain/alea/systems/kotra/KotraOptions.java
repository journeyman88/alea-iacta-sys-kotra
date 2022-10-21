/*
 * Copyright 2022 Marco Bignami.
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import net.unknowndomain.alea.systems.annotations.RpgSystemData;
import net.unknowndomain.alea.systems.annotations.RpgSystemOption;


/**
 *
 * @author journeyman
 */
@RpgSystemData(bundleName = "net.unknowndomain.alea.systems.kotra.RpgSystemBundle")
public class KotraOptions extends RpgSystemOptions
{
    @RpgSystemOption(name = "limit-dice", shortcode = "l", description = "kotra.options.limit", argName = "numberOfLimitDice")
    private Integer limitNumber;
    @RpgSystemOption(name = "trait-dice", shortcode = "t", description = "kotra.options.traits", argName = "numberOfTraitDice")
    private Integer traitNumber;
    @RpgSystemOption(name = "difficulty-dice", shortcode = "d", description = "kotra.options.difficulty", argName = "numberOfDifficultyDice")
    private Integer difficultyNumber;
    @RpgSystemOption(name = "threat-dice", shortcode = "h", description = "kotra.options.threat", argName = "numberOfThreatDice")
    private Integer threatNumber;
    @RpgSystemOption(name = "overdrive", shortcode = "o", description = "kotra.options.overdrive")
    private boolean overdrive;
                        
    @Override
    public boolean isValid()
    {
        return !(
                isHelp() || (traitNumber == null)
                );
    }

    public Collection<KotraModifiers> getModifiers()
    {
        Set<KotraModifiers> mods = new HashSet<>();
        if (isVerbose())
        {
            mods.add(KotraModifiers.VERBOSE);
        }
        if (isOverdrive())
        {
            mods.add(KotraModifiers.OVERDRIVE);
        }
        return mods;
    }
    
    public Integer getLimitNumber()
    {
        return limitNumber;
    }
    
    public Integer getTraitNumber()
    {
        return traitNumber;
    }
    
    public Integer getDifficultyNumber() 
    {
        return difficultyNumber;
    }
    
    public Integer getThreatNumber() 
    {
        return threatNumber;
    }

    /**
     * @return the overdrive
     */
    public boolean isOverdrive() {
        return overdrive;
    }
    
}