package cc.ghosty.kamoof.utils;

import cc.ghosty.kamoof.KamoofSMP;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import static cc.ghosty.kamoof.KamoofSMP.*;

/**
 * FAQ très rapide
 * <p>
 * Q: Pourquoi pas un fichier JSON ?
 * R: Trop lent 💀
 * <p>
 * Q: Pourquoi pas un fichier Properties ?
 * R: Pas de UTF-8, ou des démarches chiantes
 * <p>
 * Q: Pourquoi pas un fichier *insérer type de fichier* ?
 * R: Car là c'est très bien !!
 * <p>
 * Q: Juste... pourquoi faire ?
 * R: Certains pourraient vouloir le plugin en anglais mais n'ont aucune compétence en "taper des commandes", donc c'est + simple !
 */
@RequiredArgsConstructor
public enum Lang {
	
	PLAYER_ONLY(new String[]{
		PREFIX + "§cCette commande est uniquement disponible pour les joueurs.",
		PREFIX + "§cThis command is only usable by players."
	}),
	DATA_FILE_FAILED(new String[]{
		PREFIX + "§cImpossible de créer/utiliser le fichier '%s'",
		PREFIX + "§cCouldn't create/use the file '%s'"
	}),
	INVALID_USERNAME(new String[]{
		PREFIX + "§cPseudonyme invalide '%s'",
		PREFIX + "§cInvalid username '%s'"
	}),
	INVENTORY_FULL(new String[]{
		PREFIX + "§cVotre inventaire est plein",
		PREFIX + "§cYour inventory is full"
	}),
	HEAD_GIVEN(new String[]{
		PREFIX + "§aVous vous êtes donné la tête de §e%s",
		PREFIX + "§aGave yourself the head of §e%s"
	}),
	NEW_RITUAL_LOCATION(new String[]{
		PREFIX + "§aNouveau lieu de Rituel: %s %s %s",
		PREFIX + "§aNew location of Ritual: %s %s %s"
	}),
	UPDATE_CHECKING(new String[]{
		PREFIX + "§aVérification de la dernière version...",
		PREFIX + "§aChecking for the last version available..."
	}),
	UPDATE_CHECKER_FAIL(new String[]{
		PREFIX + "§cImpossible de récupérer/traiter la dernière version",
		PREFIX + "§cCouldn't fetch/process the last version"
	}),
	UPDATE_DOWNLOADING(new String[]{
		PREFIX + "§aTéléchargement de la dernière version...",
		PREFIX + "§aDownloading the last version available..."
	}),
	UPDATE_DOWNLOAD_FAIL(new String[]{
		PREFIX + "§cImpossible de télécharger/sauvegarder la dernière version",
		PREFIX + "§cCouldn't download/save the last version"
	}),
	UPDATE_DOWNLOADED(new String[]{
		PREFIX + "§aNouvelle version téléchargée avec succès. Redémarrage du serveur.",
		PREFIX + "§aNew version downloaded successfully. Restarting the server."
	}),
	NEW_VERSION(new String[]{
		"<hover:show_text:\"%s\"><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ Nouvelle version disponible ! <yellow><bold>%s ➞ %s</hover>",
		"<hover:show_text:\"%s\"><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ New version available! <yellow><bold>%s ➞ %s</hover>"
	}),
	NEW_VERSION_HOVER(new String[]{
		"Dernière version: <bold>Version %s</bold><br><gold>Téléchargée %s fois<br><br><white>%s<br><yellow>Cliquez pour ouvrir la page de la version",
		"Last version: <bold>Version %s</bold><br><gold>Downloaded %s times<br><br><white>%s<br><yellow>Click to open the version page"
	}),
	NEW_VERSION_DOWNLOADED(new String[]{
		"<hover:show_text:\"%s\"><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ Nouvelle version téléchargée ! <yellow><bold>%s ➞ %s</hover><br><gold><bold><hover:show_text:'Clique pour relancer le serveur et avoir la mise à jour'><click:run_command:/restart>[RELANCER LE SERVEUR]",
		"<hover:show_text:\"%s\"><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ New version downloaded ! <yellow><bold>%s ➞ %s</hover><br><gold><bold><hover:show_text:'Click to restart the server and get the update'><click:run_command:/restart>[RESTART THE SERVER]"
	}),
	VERSION_CHANGELOG_REGEX(new String[]{
		"Changements:((?!`).)+",
		"Changes:((?!`).)+"
	}),
	CONFIG_RELOADED(new String[]{
		PREFIX_MM + "<green>Config rechargée. Veuillez relancer le serveur si vous avez activé/désactivé des catégories.<br><b><yellow><click:run_command:'/restart'>Relancer</click><white> | <gold><click:run_command:'/reload confirm'>Recharger</click>",
		PREFIX_MM + "<green>Config reloaded. You should restart the server if you enabled/disabled categories.<br><b><yellow><click:run_command:'/restart'>Restart</click><white> | <gold><click:run_command:'/reload confirm'>Reload</click>"
	}),
	MAIN_ARGUMENTS(new String[]{
		"<green><b>KamoofSMP Version %s</b><br><br><gold>Arguments possibles:<br>- <yellow><click:run_command:'/kamoofsmp info'><b>info:</b></click> Les infos/crédits du plugin</yellow><br>- <yellow><click:run_command:'/kamoofsmp givehead'><b>givehead:</b></click> Se donner n'importe quelle tête</yellow><br>- <yellow><click:run_command:'/kamoofsmp reload'><b>reload:</b></click> Recharger la configuration</yellow><br>- <yellow><click:run_command:'/kamoofsmp setup'><b>setup:</b></click> Définir le lieu du rituel</yellow>",
		"<green><b>KamoofSMP Version %s</b><br><br><gold>Possible arguments:<br>- <yellow><click:run_command:'/kamoofsmp info'><b>info:</b></click> The infos/credits of the plugin</yellow><br>- <yellow><click:run_command:'/kamoofsmp givehead'><b>givehead:</b></click> Give yourself any head</yellow><br>- <yellow><click:run_command:'/kamoofsmp reload'><b>reload:</b></click> Reload the configuration</yellow><br>- <yellow><click:run_command:'/kamoofsmp setup'><b>setup:</b></click> Set where the ritual is</yellow>"
	}),
	CREDITS(new String[]{
		"<green><b>KamoofSMP Version %s</b><br><br><gold>- <yellow>Systèmes & Base par <u>Ghosty</u></yellow><br>- <yellow>Support & MàJ par <u>Solme</u></yellow><br>Contact: <#4444ee><u><click:open_url:'https://discord.gg/akgp49Q76M'>Discord",
		"<green><b>KamoofSMP Version %s</b><br><br><gold>- <yellow>Systems & Base by <u>Ghosty</u></yellow><br>- <yellow>Support & Updates by <u>Solme</u></yellow><br>Contact: <#4444ee><u><click:open_url:'https://discord.gg/akgp49Q76M'>Discord"
	}),
	SETUP_GIVEN(new String[]{
		"<br>Utilise le <red><b>Bloc de verrues du Nether <reset>pour définir l'emplacement des têtes.<br> <gray>- Il suffit de le poser au centre du rituel.</gray><br>Utilise le <yellow><b>Bâton de Blaze <reset>pour construire un rituel au sol.<br>Utilise le <aqua><b>Bâton de Breeze <reset>pour construire un rituel en sous-sol.",
		"<br>Use the <red><b>Nether Wart Block <reset>to set the location of the heads.<br> <gray>- You just need to place it at the center.</gray><br>Use the <yellow><b>Blaze Rod <reset>to paste a ritual on the ground.<br>Use the <aqua><b>Breeze Rod <reset>to paste a ritual underground."
	}),
	FIRST_JOIN(new String[]{
		"<br><yellow>Merci d'avoir installé le KamoofSMP <red>❤<reset><br><click:run_command:'/kamoofsmp setup'>Cliquez ici pour placer le rituel</click><br><click:suggest_command:'/givehead '><hover:show_text:'Rajoutez le pseudo d'un joueur au choix'>Cliquez ici pour vous donner une tête</hover></click><br>",
		"<br><yellow>Thanks for installing the KamoofSMP <red>❤<reset><br><click:run_command:'/kamoofsmp setup'>Click here to place the ritual</click><br><click:suggest_command:'/givehead '><hover:show_text:'Add any player name'>Click here to give you any head</hover></click><br>"
	}),
	NOT_PLACED(new String[]{
		PREFIX_MM + "<red>Vous n'avez pas encore placé le Rituel !<br><yellow><click:run_command:'/kamoofsmp setup'>Cliquez ici pour placer le rituel.</click>",
		PREFIX_MM + "<red>You still haven't placed the Ritual !<br><yellow><click:run_command:'/kamoofsmp setup'>Click here to place the ritual.</click>"
	}),
	RITUAL_DISABLED(new String[]{
		PREFIX + "§cLe rituel est désactivé dans la configuration.",
		PREFIX + "§cThe ritual is disabled in the configuration."
	});
	
	private static int lang = 0;
	private final String[] values;
	
	public static void init() {
		switch (KamoofSMP.config().getString("language").toLowerCase().trim()) {
			case "en":
			case "en_uk":
			case "en_en":
			case "en_us":
			case "england":
			case "uk":
			case "us":
			case "english": {
				lang = 1;
				break;
			}
			case "fr":
			case "fr_fr":
			case "french":
			default: {
				lang = 0;
				break;
			}
		}
	}
	
	public String get() {
		return values[lang];
	}
	
	public void send(CommandSender sender, Object... args) {
		if (args == null || args.length == 0)
			sender.sendMessage(get());
		else
			sender.sendMessage(String.format(get(), args));
	}
	
	public void sendMM(CommandSender sender, Object... args) {
		if (args == null || args.length == 0)
			sender.spigot().sendMessage(Message.toBaseComponent(get()));
		else
			sender.spigot().sendMessage(Message.toBaseComponent(String.format(get(), args)));
	}
	
	
}
