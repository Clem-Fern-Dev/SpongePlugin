package fr.mrfern.spongeplugintest.chunk.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import fr.mrfern.spongeplugintest.config.ChunkConfig;
import fr.mrfern.spongeplugintest.config.ChunkNode;

@Deprecated
public class CreateChunkConfigCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
		    
			Player player = (Player) src;
		    int posX = (int) player.getLocation().getX(), posZ = (int) player.getLocation().getZ();
		    String worldName = player.getWorld().getName();
		    
		    ChunkConfig.getInstance().createChunkConfig(worldName, posX, posZ);
		 
		    
		    ChunkNode chunkNode = ChunkConfig.getInstance().getChunkConfigNode(worldName,posX,posZ);
		    chunkNode.setNameDiscoverer(player.getName());
		    chunkNode.save();
			return CommandResult.success(); 
		}
		else if(src instanceof ConsoleSource) {
		    src.sendMessage(Text.of("Impossible d'exécuter cette commands ici"));
		}
		else if(src instanceof CommandBlockSource) {
		    src.sendMessage(Text.of("Impossible d'exécuter cette commands ici"));
		}
		return CommandResult.empty();
	}

}
