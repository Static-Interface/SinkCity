package de.static_interface.sinkcity.commands;

import org.apache.commons.cli.ParseException;
import org.bukkit.command.CommandSender;

import de.static_interface.sinkcity.SinkCity;
import de.static_interface.sinklibrary.api.command.SinkCommand;

public class ResidentCommand extends SinkCommand {

    public ResidentCommand(SinkCity sinkCity) {
        super(sinkCity);
    }

    @Override
    protected boolean onExecute(CommandSender sender, String label, String[] args) throws ParseException {
        sender.sendMessage("Not yet implemented.");
        return true;
    }

}
