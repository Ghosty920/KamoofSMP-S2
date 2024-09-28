package me.ghosty.kamoof.utils;

import lombok.RequiredArgsConstructor;
import me.ghosty.kamoof.KamoofSMP;
import org.bukkit.command.CommandSender;

import static me.ghosty.kamoof.KamoofSMP.PREFIX;

/**
 * FAQ très rapide
 * <p>
 * Q: Pourquoi pas un fichier JSON ?
 * R: Trop lent
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
	UPDATE_CHECKER_FAIL(new String[]{
		PREFIX + "§cImpossible de récupérer/traiter la dernière version",
		PREFIX + "§cCouldn't fetch/process the last version"
	}),
	UPDATE_DOWNLOAD_FAIL(new String[]{
		PREFIX + "§cImpossible de télécharger/sauvegarder la dernière version",
		PREFIX + "§cCouldn't download/save the last version"
	}),
	NEW_VERSION(new String[]{
		"<hover:show_text:'%s'><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ Nouvelle version disponible ! <yellow><bold>%s ➞ %s</hover>",
		"<hover:show_text:'%s'><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ New version available! <yellow><bold>%s ➞ %s</hover>"
	}),
	NEW_VERSION_HOVER(new String[]{
		"Dernière version: <bold>Version %s</bold><br><gold>Téléchargée %s fois<br><br><white>%s<br><yellow>Cliquez pour ouvrir la page de la version",
		"Last version: <bold>Version %s</bold><br><gold>Downloaded %s times<br><br><white>%s<br><yellow>Click to open the version page"
	}),
	NEW_VERSION_DOWNLOADED(new String[]{
		"<hover:show_text:'%s'><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ Nouvelle version téléchargée ! <yellow><bold>%s ➞ %s</hover><br><gold><bold><hover:show_text:'Clique pour relancer le serveur et avoir la mise à jour'><click:run_command:/restart>[RELANCER LE SERVEUR]",
		"<hover:show_text:'%s'><click:open_url:'%s'><green><bold>[KamoofSMP]</bold> ➤ New version downloaded ! <yellow><bold>%s ➞ %s</hover><br><gold><bold><hover:show_text:'Click to restart the server and get the update'><click:run_command:/restart>[RESTART THE SERVER]"
	}),
	VERSION_CHANGELOG_REGEX(new String[]{
		"Changements:((?!`).)+",
		"Changes:((?!`).)+"
	}),
	CONFIG_RELOADED(new String[]{
		PREFIX + "§aConfig rechargée. Veuillez relancer le serveur si vous avez activé/désactivé des catégories.",
		PREFIX + "§aConfig reloaded. You should restart the server if you enabled/disabled categories."
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
		"§aVous avez été donné les objets pour placer le rituel.",
		"§aYou have been given the items to place the ritual."
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
