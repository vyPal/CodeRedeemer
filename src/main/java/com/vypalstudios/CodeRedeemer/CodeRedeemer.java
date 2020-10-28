package com.vypalstudios.CodeRedeemer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CodeRedeemer extends JavaPlugin {
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		int pluginId = 9221; // <-- Replace with the id of your plugin!
        @SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, pluginId);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("coderedeemer")) {
			if (args.length == 0) {
				sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bThis is the CodeRedeemer main command. Type '/coderedeemer help' in chat to show a list of commands."));
			}else if(args.length > 0) {
				if (args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bHELP MENU"));
					sender.sendMessage(colorize("&r&b/coderedeemer - main command"));
					sender.sendMessage(colorize("&r&b/coderedeemer help - show this help menu"));
					sender.sendMessage(colorize("&r&b/coderedeemer create <code> <max uses> <delete after (in hours, write 0 for unlimited)> - creates a code"));
					sender.sendMessage(colorize("&r&b/coderedeemer remove <code> - removes a code"));
					sender.sendMessage(colorize("&r&b/coderedeemer list - shows all the codes"));
					sender.sendMessage(colorize("&r&b/coderedeemer setperm <code> <perm (write null for any)> - makes code available only for players with this permission"));
					sender.sendMessage(colorize("&r&b/coderedeemer addcmd <code> <cmd (without /)> - adds a command to a code"));
					sender.sendMessage(colorize("&r&b/coderedeemer remcmd <code> <cmd> - main command"));
					sender.sendMessage(colorize("&r&b/coderedeemer listcmd <code> - lists all command for the code"));
				}
				if (args[0].equalsIgnoreCase("create")) {
					FileConfiguration cfg = this.getConfig();
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat("YYYYddMMHHmm");
					cfg.set(args[1] + ".maxuses", args[2]);
					Calendar calendar = Calendar.getInstance();
				    calendar.setTime(now);
				    calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(args[3]));
				    cfg.set(args[1] + ".delon", format.format(calendar.getTime()));
				    if(Integer.parseInt(args[3]) == 0) {
				    	cfg.set(args[1] + ".delon", 0);
				    }
				    cfg.set(args[1] + ".perm", 0);
				    cfg.createSection(args[1] + ".cmds");
				    File file = new File(this.getDataFolder().getAbsolutePath()+File.separator+"config.yml");
				    try {
						cfg.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				    sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bCode " + args[1] + " created!"));
				}
				if (args[0].equalsIgnoreCase("remove")) {
					FileConfiguration cfg = this.getConfig();
					cfg.set(args[1], null);
					File file = new File(this.getDataFolder().getAbsolutePath()+File.separator+"config.yml");
				    try {
						cfg.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				    sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bCode " + args[1] + " removed!"));
				}
				if (args[0].equalsIgnoreCase("list")) {
					sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bList of all codes:"));
					FileConfiguration cfg = this.getConfig();
					Set<String> list = cfg.getKeys(false);
					String[] stringArray = list.toArray(new String[0]);
					sender.sendMessage(stringArray);
				}
				if (args[0].equalsIgnoreCase("setperm")) {
					sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bSet permission requirement for code " + args[1]));
					FileConfiguration cfg = this.getConfig();
					cfg.set(args[1] + ".perm", args[2]);
				}
				if (args[0].equalsIgnoreCase("addcmd")) {
					FileConfiguration cfg = this.getConfig();
					List<String> list = cfg.getStringList(args[1] + ".cmds");
					String ats = String.join(" ", Arrays.asList(args).subList(2, args.length).toArray(new String[]{}));
					list.add(ats);
					this.getConfig().set(args[1] + ".cmds", list);
					File file = new File(this.getDataFolder().getAbsolutePath()+File.separator+"config.yml");
				    try {
						cfg.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				    sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bCommand added to code " + args[1]));
				}
				if (args[0].equalsIgnoreCase("rmcmd")) {
					FileConfiguration cfg = this.getConfig();
					List<String> list = cfg.getStringList(args[1] + ".cmds");
					list.remove(list.indexOf(args[2]));
					this.getConfig().set("path", list);
					File file = new File(this.getDataFolder().getAbsolutePath()+File.separator+"config.yml");
				    try {
						cfg.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				    sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bCommand removed from code " + args[1]) + ": /" + args[2]);
				}
				if (args[0].equalsIgnoreCase("listcmd")) {
					sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bList of all cmds:"));
					FileConfiguration cfg = this.getConfig();
					Set<String> list = cfg.getConfigurationSection(args[1] + ".cmds").getKeys(false);
					String[] stringArray = list.toArray(new String[0]);
					sender.sendMessage(stringArray);
				}
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("redeem")) {
			FileConfiguration cfg = this.getConfig();
			Set<String> list = cfg.getKeys(false);
			if(list.contains(args[0])) {
				Date now = new Date();
				SimpleDateFormat format = new SimpleDateFormat("YYYYddMMHHmm");
				if(cfg.getInt(args[0] + ".delon") != 0) {
					if(Integer.parseInt(format.format(now)) < cfg.getInt(args[0] + ".delon")) {
						if(cfg.getString(args[0] + ".perm") != String.valueOf(0)){
							if(sender.hasPermission(cfg.getString(args[0] + ".perm"))){
								if(cfg.getInt(args[0] + ".maxuses") == 0) {
									for(String command : cfg.getStringList(args[0] + ".cmds")) {
										command = command.replace("{player}", sender.getName());
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
									}
								}
								if(cfg.getInt(args[0] + ".maxuses") >= 1) {
									for(String command : cfg.getStringList(args[0] + ".cmds")) {
										command = command.replace("{player}", sender.getName());
										Bukkit.dispatchCommand(sender, command);
									}
									cfg.set(args[0] + ".maxuses", cfg.getInt(args[0] + ".maxuses") - 1);
									if(cfg.getInt(args[0] + ".maxuses") == 0) {
										cfg.set(args[0], null);
									}
								}
							}
						}else{
							if(cfg.getInt(args[0] + ".maxuses") == 0) {
								for(String command : cfg.getStringList(args[0] + ".cmds")) {
									command = command.replace("{player}", sender.getName());
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								}
							}
							if(cfg.getInt(args[0] + ".maxuses") >= 1) {
								for(String command : cfg.getStringList(args[0] + ".cmds")) {
									command = command.replace("{player}", sender.getName());
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								}
								cfg.set(args[0] + ".maxuses", cfg.getInt(args[0] + ".maxuses") - 1);
								if(cfg.getInt(args[0] + ".maxuses") == 0) {
									cfg.set(args[0], null);
								}
							}
						}
					}
				}else{
					if(cfg.getString(args[0] + ".perm") != String.valueOf(0)){
						if(sender.hasPermission(cfg.getString(args[0] + ".perm"))){
							if(cfg.getInt(args[0] + ".maxuses") == 0) {
								for(String command : cfg.getStringList(args[0] + ".cmds")) {
									command = command.replace("{player}", sender.getName());
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								}
							}
							if(cfg.getInt(args[0] + ".maxuses") >= 1) {
								for(String command : cfg.getStringList(args[0] + ".cmds")) {
									command = command.replace("{player}", sender.getName());
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								}
								cfg.set(args[0] + ".maxuses", cfg.getInt(args[0] + ".maxuses") - 1);
								if(cfg.getInt(args[0] + ".maxuses") == 0) {
									cfg.set(args[0], null);
								}
							}
						}
					}else{
						if(cfg.getInt(args[0] + ".maxuses") == 0) {
							for(String command : cfg.getStringList(args[0] + ".cmds")) {
								command = command.replace("{player}", sender.getName());
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
							}
						}
						if(cfg.getInt(args[0] + ".maxuses") >= 1) {
							for(String command : cfg.getStringList(args[0] + ".cmds")) {
								command = command.replace("{player}", sender.getName());
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
							}
							cfg.set(args[0] + ".maxuses", cfg.getInt(args[0] + ".maxuses") - 1);
							if(cfg.getInt(args[0] + ".maxuses") == 0) {
								cfg.set(args[0], null);
							}
						}
					}
				}
			}
			sender.sendMessage(colorize("&6&l[CodeRedeemer] &f>> &r&bCode redeemed!"));
			return true;
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(command.getName().equalsIgnoreCase("coderedeemer")) {
			if(args.length == 1) {
				List<String> otc = new ArrayList<>();
				otc.add("help");
				otc.add("create");
				otc.add("list");
				otc.add("remove");
				otc.add("setperm");
				otc.add("addcmd");
				otc.add("listcmd");
				otc.add("remcmd");
				return otc;
			}
		}
		return null;
	}
	
	public String colorize(String msg)
    {
        String coloredMsg = "";
        for(int i = 0; i < msg.length(); i++)
        {
            if(msg.charAt(i) == '&')
                coloredMsg += '§';
            else
                coloredMsg += msg.charAt(i);
        }
        return coloredMsg;
    }
	
}