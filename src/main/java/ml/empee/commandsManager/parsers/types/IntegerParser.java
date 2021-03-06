package ml.empee.commandsManager.parsers.types;

import lombok.Getter;
import ml.empee.commandsManager.parsers.ParameterParser;
import ml.empee.commandsManager.parsers.ParserDescription;
import org.bukkit.command.CommandException;

public class IntegerParser extends ParameterParser<Integer> {

    public static final IntegerParser DEFAULT = new IntegerParser("", "", Integer.MIN_VALUE, Integer.MAX_VALUE);

    @Getter private final int min;
    @Getter private final int max;

    public IntegerParser(String label, String defaultValue, Integer min, Integer max) {
        super(label, defaultValue);

        this.min = min;
        this.max = max;

        descriptor = new ParserDescription("integer", "This parameter can only contain an integer", new String[]{
                "Min: ", (min != Integer.MIN_VALUE ? min+"" : "-∞"),
                "Max: ", (max != Integer.MAX_VALUE ? max+"" : "+∞"),
                "Default value: ", (defaultValue.isEmpty() ? "none" : defaultValue)
        });
    }

    @Override
    public Integer parse(int offset, String... args) {
        try {
            int result = Integer.parseInt(args[offset]);

            if(result < min) {
                throw new CommandException("&4&l > &cThe value must be higher then &e" + min + "&c but it's value is &e" + result);
            } else if(result > max) {
                throw new CommandException("&4&l > &cThe value must be lower then &e" + max + "&c but it's value is &e" + result);
            }

            return result;
        } catch (NumberFormatException e) {
            throw new CommandException("&4&l > &cThe value &e" + args[offset] + "&c must be an integer");
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && ((IntegerParser) o).min == min && ((IntegerParser) o).max == max;
    }


}
