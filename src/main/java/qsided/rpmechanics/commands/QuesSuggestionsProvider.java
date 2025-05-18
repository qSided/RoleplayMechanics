package qsided.rpmechanics.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class QuesSuggestionsProvider implements SuggestionProvider<ServerCommandSource> {
    
    
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        
        Collection<String> skills = new ArrayList<>();
        
        skills.add("agility");
        skills.add("axes");
        skills.add("bows");
        skills.add("cooking");
        skills.add("crafting");
        skills.add("enchanting");
        skills.add("endurance");
        skills.add("farming");
        skills.add("mining");
        skills.add("smithing");
        skills.add("swimming");
        skills.add("swords");
        skills.add("woodcutting");
        
        for (String skill : skills) {
            suggestionsBuilder.suggest(skill);
        }
        
        return suggestionsBuilder.buildFuture();
    }
}
