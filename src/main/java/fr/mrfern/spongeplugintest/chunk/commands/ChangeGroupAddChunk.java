package fr.mrfern.spongeplugintest.chunk.commands;

import java.util.List;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import fr.mrfern.spongeplugintest.config.ChunkConfig;
import fr.mrfern.spongeplugintest.config.ChunkNode;

public class ChangeGroupAddChunk implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			
			Player player;
			if(args.<Player>getOne("player").isPresent()) {
				player = args.<Player>getOne("player").get();
			}else {
				src.sendMessage(Text.of("Le joueur entré n'est pas valide"));
			    return CommandResult.empty();
			}
			
            String groupName;
            if(args.<String>getOne("group-name").isPresent()) {
				groupName = args.<String>getOne("group-name").get();
			}else {
				src.sendMessage(Text.of("Mauvais argument"));
			    return CommandResult.empty();
			}
            
            Player ply = (Player) src;
			int posX = (int) ply.getLocation().getChunkPosition().getX(), posZ = (int) ply.getLocation().getChunkPosition().getZ();
		    String worldName = ply.getWorld().getName();		
				    
		    ChunkNode chunkNode = ChunkConfig.getInstance().getChunkConfigNode(worldName,posX,posZ);
		    
		    Text textPosX = Text.builder("X:"+ posX ).color(TextColors.LIGHT_PURPLE).build();
	    	Text textSlasher = Text.builder("/").color(TextColors.DARK_BLUE).build();
	    	Text textPosZ = Text.builder("Z:"+ posZ ).color(TextColors.GREEN).build();
	    	Text textEnd = Text.builder(" ] ").color(TextColors.DARK_BLUE).build();
	    	Text textPlayerNameCible = Text.builder(player.getName()).color(TextColors.GOLD).build();
	    	//Text textPlayerNameYou = Text.builder(ply.getName()).color(TextColors.GOLD).build();
	    	Text textGroupName = Text.builder(groupName).color(TextColors.GOLD).build();
				    
		    if(chunkNode != null) {
		    	
		    	/*if(chunkNode.getClaimedBy().equals(player.getName())){
		    		// vous ne pouvez pas vou ajouté vous meme
		    		Text textClaimed = Text.builder("Vous ne pouvez pas vous ajoutez vous même, ce claim vous appartient.").color(TextColors.RED).build();
			    	Text textEnTete = Text.builder("[PumpMyChunk -- ").color(TextColors.DARK_BLUE).append(textPosX,textSlasher,textPosZ,textEnd,textClaimed).build();
		    		ply.sendMessage(textEnTete);
			    	return CommandResult.empty();
		    	}*/
		    	
		    	// ajout
		    	if(chunkNode.getClaimedBy().equals(ply.getName())){
		    		// alors autorisé
		    		List<String> listGroupCoOwner = getNodeGroupList("co-owner", chunkNode);
				    List<String> listGroupUser = getNodeGroupList("user", chunkNode);
				    						    		
				    if((listGroupCoOwner.contains(player.getName()) & groupName.equals("co-owner")) | (listGroupUser.contains(player.getName()) & groupName.equals("user"))) {
				    	
				    	Text textClaimed = Text.builder(" est déjà dans la liste du groupes ").color(TextColors.RED).build();
				    	Text textEnTete = Text.builder("[PumpMyChunk -- ").color(TextColors.DARK_BLUE).append(textPosX,textSlasher,textPosZ,textEnd,textPlayerNameCible,textClaimed,textGroupName).build();
			    		ply.sendMessage(textEnTete);
				    	// joueur déjà dans la liste
					    return CommandResult.success();
					    
				    }else if(listGroupCoOwner.contains(player.getName()) & groupName.equals("user")){
				    	
				    	// suppression de la liste co-owner et ajout à list user
				    	listGroupCoOwner.remove(player.getName());
				    	listGroupUser.add(player.getName());
				    	
				    	chunkNode.setCoOwnerList(listGroupCoOwner);
				    	chunkNode.setUserList(listGroupUser);
				    	chunkNode.save();
				    	
				    	Text textClaimed = Text.builder(" a été supprimé de sa liste d'origine et ajouté à la liste ").color(TextColors.BLUE).build();
				    	Text textEnTete = Text.builder("[PumpMyChunk -- ").color(TextColors.DARK_BLUE).append(textPosX,textSlasher,textPosZ,textEnd,textPlayerNameCible,textClaimed,textGroupName).build();
			    		ply.sendMessage(textEnTete);

					    return CommandResult.success();
					    
				    }else if(listGroupUser.contains(player.getName()) & groupName.equals("co-owner")) {
				    	
				    	// suppression de la list user et ajout à la list co-owner
				    	listGroupUser.remove(player.getName());
				    	listGroupCoOwner.add(player.getName());
				    	
				    	chunkNode.setCoOwnerList(listGroupCoOwner);
				    	chunkNode.setUserList(listGroupUser);
				    	chunkNode.save();
				    	
				    	Text textClaimed = Text.builder(" a été supprimé de sa liste d'origine et ajouté à la liste ").color(TextColors.BLUE).build();
				    	Text textEnTete = Text.builder("[PumpMyChunk -- ").color(TextColors.DARK_BLUE).append(textPosX,textSlasher,textPosZ,textEnd,textPlayerNameCible,textClaimed,textGroupName).build();
			    		ply.sendMessage(textEnTete);
				    	
					    return CommandResult.success();
					    
				    }else {
				    	
				    	// ajout à la liste demandé
				    	
				    	List<String> listGroup = getNodeGroupList(groupName, chunkNode);
				    	listGroup.add(player.getName());
				    	
				    	setNodeGroupList(listGroup, groupName, chunkNode);
				    	chunkNode.save();
				    	Text textClaimed = Text.builder(" a été ajouté à la liste ").color(TextColors.BLUE).build();
				    	Text textEnTete = Text.builder("[PumpMyChunk -- ").color(TextColors.DARK_BLUE).append(textPosX,textSlasher,textPosZ,textEnd,textPlayerNameCible,textClaimed,textGroupName).build();
			    		ply.sendMessage(textEnTete);
				    	
					    return CommandResult.success();
				    }

				    		
		    	}else {
				    // verification si autorisation
				    ply.sendMessage(Text.of("verification de l'autorisation"));
				}

		    }
		    return CommandResult.empty();
				    
		}else {
			// return erreur
			src.sendMessage(Text.of("Joueur inconnu"));
			return CommandResult.empty();
		}
		
	}
	
	private List<String> getNodeGroupList(String groupName, ChunkNode chunkNode) {
		switch (groupName) {
		case "co-owner":
			return chunkNode.getCoOwnerList();
			
		case "user":
			return chunkNode.getUserList();
		}
		return null;
	}
	
	private void setNodeGroupList(List<String> list ,String groupName, ChunkNode chunkNode) {
		switch (groupName) {
		case "co-owner":
			chunkNode.setCoOwnerList(list);
			break;	
			
		case "user":
			chunkNode.setUserList(list);
			break;	
		}
	}
	
}
