package qsided.rpmechanics;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class StateManager extends PersistentState {
    
    public HashMap<UUID, PlayerData> players = new HashMap<>();
    
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            
            playerNbt.putBoolean("hasJoinedBefore", playerData.hasJoinedBefore);
            
            NbtCompound skillLevelsNbt = new NbtCompound();
            playerData.skillLevels.forEach(skillLevelsNbt::putInt);
            playerNbt.put("skillLevels", skillLevelsNbt);
            
            NbtCompound skillExpNbt = new NbtCompound();
            playerData.skillExperience.forEach(skillExpNbt::putFloat);
            playerNbt.put("skillExp", skillExpNbt);
            
            NbtCompound expModifiersNbt = new NbtCompound();
            playerData.expModifiers.forEach((id, skillPercentHashmap) -> {
                NbtCompound skillsAndPercentsNbt = new NbtCompound();
                skillPercentHashmap.forEach(skillsAndPercentsNbt::putInt);
                expModifiersNbt.put(id, skillsAndPercentsNbt);
            });
            playerNbt.put("expModifiers", expModifiersNbt);
            
            NbtCompound rpClassNbt = new NbtCompound();
            rpClassNbt.putString("rpClass", playerData.rpClass);
            playerNbt.put("rpClass", rpClassNbt);
            
            //Puts each player's data into nbt
            playersNbt.put(uuid.toString(), playerNbt);
        });
        //Writes all players' saved class and rpClass nbt into a save file
        nbt.put("players", playersNbt);
        
        return nbt;
    }
    
    public static StateManager createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        StateManager state = new StateManager();
        
        NbtCompound playersNbt = nbt.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
            
            playerData.hasJoinedBefore = playersNbt.getCompound(key).getBoolean("hasJoinedBefore");
            
            NbtCompound skillLevelsNbt = playersNbt.getCompound(key).getCompound("skillLevels");
            skillLevelsNbt.getKeys().forEach(s -> {
                String skill = String.valueOf(s);
                int level = skillLevelsNbt.getInt(s);
                playerData.skillLevels.put(skill, level);
            });
            NbtCompound skillExpNbt = playersNbt.getCompound(key).getCompound("skillExp");
            skillExpNbt.getKeys().forEach(s -> {
                String skill = String.valueOf(s);
                float experience = skillExpNbt.getInt(s);
                playerData.skillExperience.put(skill, experience);
            });
            
            NbtCompound expModifiersNbt = playersNbt.getCompound(key).getCompound("expModifiers");
            expModifiersNbt.getKeys().forEach(s -> {
                NbtCompound modifiersNbt = expModifiersNbt.getCompound(s);
                String id = String.valueOf(s);
                HashMap<String, Integer> modifiers = new HashMap<>();
                modifiersNbt.getKeys().forEach(skill -> modifiers.put(skill, modifiersNbt.getInt(skill)));
                playerData.expModifiers.put(id, modifiers);
            });
            
            playerData.rpClass = playersNbt.getCompound(key).getString("rpClass");
            
            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });
        
        return state;
    }
    
    private static Type<StateManager> type = new Type<>(
            StateManager::new,
            StateManager::createFromNbt,
            null
    );
    
    public static StateManager getServerState(MinecraftServer server) {
        
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        StateManager state = persistentStateManager.getOrCreate(type, RoleplayMechanicsCommon.MOD_ID);
        
        state.markDirty();
        
        return state;
    }
    
    public static PlayerData getPlayerState(LivingEntity player) {
        StateManager serverState = getServerState(player.getWorld().getServer());
        
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
    }
}
