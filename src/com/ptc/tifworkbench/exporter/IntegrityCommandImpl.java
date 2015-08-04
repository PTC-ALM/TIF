/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter;

import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.response.APIException;
import com.ptc.tifworkbench.integrity.IntegrityApi;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author schamaillard
 */
public class IntegrityCommandImpl implements DeployableCommand {

    protected final Command command;

    public IntegrityCommandImpl(final String prefix, final String cmd, final Map<String, String> options,
            final String selection) {
        this.command = new Command(prefix, cmd);
        if (options != null) {
            for (Entry<String, String> option : options.entrySet()) {
                this.command.addOption(new Option(option.getKey(), option.getValue()));
            }
        }
        if ((selection != null) && !selection.isEmpty()) {
            this.command.addSelection(selection);
        }
    }

    @Override
    public void execute() throws DeployableCommandException {
        try {
            IntegrityApi.getInstance().execute(command);
        } catch (APIException apie) {
            throw new DeployableCommandException(apie.getExceptionId());
        } catch (IntegrityExceptionEx ex) {
            throw new DeployableCommandException(ex.getMessage());
        }
    }

    // For testing purpose
    public boolean isEqualsToCommandLine(String prefix, String command,
            Map options, String selection) {
        if (!prefix.equals(this.command.getApp())) {
            return false;
        }
        if (!command.equals(this.command.getCommandName())) {
            return false;
        }
        if (options.keySet().size() != this.command.getOptionList().size()) {
            return false;
        }
        OptionList optionList = this.command.getOptionList();
        for (Iterator<?> it = optionList.getOptions(); it.hasNext();) {
            Option opt = (Option) it.next();
            if (!options.containsKey(opt.getName())) {
                return false;
            }
            if (!((String) options.get(opt.getName())).equals(opt.getValue())) {
                return false;
            }
        }
        if (selection != null) {
            if (this.command.getSelectionList().size() == 0) {
                return false;
            }
            if (!selection.equals(this.command.getSelectionList().getSelection(
                    0))) {
                return false;
            }
        }
        return true;
    }

}
