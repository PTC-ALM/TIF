/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter.parser;

import com.ptc.tifworkbench.exporter.DeployableCommand;
import com.ptc.tifworkbench.exporter.IntegrityCommandImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author schamaillard
 */
public class SimpleIntegrityCommandParserImpl implements DeployableCommandLineParser {

    protected static final String PREFIX_REGEX = "[a-z]+";
    protected static final String CMD_REGEX = "[a-z]+";
    protected static final String QUOTED_VALUE_OPT_REGEX = "--[a-zA-Z]+=([a-zA-Z]+=)?(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\")";
    protected static final String UNQUOTED_VALUE_OPT_REGEX = "--[a-zA-Z]+=[.[^ \"]]+";
    protected static final String WITHOUT_VALUE_OPT_REGEX = "--[a-zA-Z]+( |$)";
    protected static final String SELECTION_REGEX = "(([.[^ $\"]]+)|(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"))";

    protected final List<String> tokensToRemove;

    public SimpleIntegrityCommandParserImpl() {
        this.tokensToRemove = new ArrayList<String>();
    }

    protected String getFirstMatch(final String regex, final String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    protected Map<String,String> parseValuedOptions(final String parsedLine, final String regex) {
        Map<String,String> options = new HashMap<String,String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parsedLine);
        while(matcher.find()){
            String optionStr = matcher.group();
            this.tokensToRemove.add(optionStr);
            String optionName = optionStr.substring(2, optionStr.indexOf('='));
            String optionValue = optionStr.substring(optionStr.indexOf('=') + 1,
                    optionStr.length());
            if (optionValue.startsWith("\"")) {
                optionValue = optionValue
                        .substring(1, optionValue.length() - 1);
            }
            options.put(optionName, optionValue);
        }
        return options;
    }

    protected Map<String,String> parseQuotedValuedOptions(final String parsedLine) {
        return parseValuedOptions(parsedLine,QUOTED_VALUE_OPT_REGEX);
        
    }

    protected Map<String,String> parseUnquotedValuedOptions(final String parsedLine) {
        return parseValuedOptions(parsedLine,UNQUOTED_VALUE_OPT_REGEX);
    }

    protected Map<String,String> parseUnvaluedOption(final String parsedLine) {
        Map<String,String> options = new HashMap<String,String>();
        Pattern pattern = Pattern.compile(WITHOUT_VALUE_OPT_REGEX);
        Matcher matcher = pattern.matcher(parsedLine);
        while(matcher.find()){
            String optionStr = matcher.group();
            this.tokensToRemove.add(optionStr);
            String optionName = optionStr.substring(2, optionStr.length());
            options.put(optionName, null);
        }
        return options;
    }

    protected String removeParsedToken(String line) {
        String clearedLine = new String(line);
        for(String token : this.tokensToRemove){
            clearedLine = clearedLine.replace(token, "");
        }
        this.tokensToRemove.clear();
        return clearedLine;
    }

    @Override
    public DeployableCommand parse(final String line) {
        String parsedLine = new String(line);
        String prefix = getFirstMatch(PREFIX_REGEX, parsedLine);
        parsedLine = parsedLine.replaceFirst(
                (new StringBuilder(String.valueOf(prefix))).append(" ")
                .toString(), "");
        String commandName = getFirstMatch(CMD_REGEX, parsedLine);
        parsedLine = parsedLine.replaceFirst(
                (new StringBuilder(String.valueOf(commandName))).append(" ")
                .toString(), "");
        Map allOptions = new HashMap();
        Map quotedValuedOptions = parseQuotedValuedOptions(parsedLine);
        allOptions.putAll(quotedValuedOptions);
        parsedLine = removeParsedToken(parsedLine);
        Map unquotedValuedOptions = parseUnquotedValuedOptions(parsedLine);
        allOptions.putAll(unquotedValuedOptions);
        parsedLine = removeParsedToken(parsedLine);
        Map unvaluedOptions = parseUnvaluedOption(parsedLine);
        allOptions.putAll(unvaluedOptions);
        parsedLine = removeParsedToken(parsedLine);
        String selection = getFirstMatch(SELECTION_REGEX,parsedLine);
        if (selection != null && selection.startsWith("\"")) {
            selection = selection.substring(1, selection.length() - 1);
        }
        IntegrityCommandImpl integrityCommand = new IntegrityCommandImpl(prefix, commandName,
                    allOptions, selection);
        return integrityCommand;
    }

}
