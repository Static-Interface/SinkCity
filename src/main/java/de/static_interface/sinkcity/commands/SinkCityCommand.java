package de.static_interface.sinkcity.commands;

import org.apache.commons.cli.ParseException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.static_interface.sinklibrary.api.command.SinkCommand;

public class SinkCityCommand extends SinkCommand {

    public SinkCityCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean onExecute(CommandSender commandSender, String label, String[] args) throws ParseException {
        return false;
    }

}
