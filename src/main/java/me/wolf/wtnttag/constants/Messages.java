package me.wolf.wtnttag.constants;

import me.wolf.wtnttag.utils.Utils;

public final class Messages {

    public static final String ADMIN_HELP = Utils.colorize(
            "&7[-------&bTNT &cAdmin &bHelp&7-------]\n" +
                    "&b/tnt createarena <arena> &7- Creates a new arena \n" +
                    "&b/tnt deletearena <arena> &7- Deletes an arena\n" +
                    "&b/tnt setworldspawn &7- Sets the world spawn \n" +
                    "&b/tnt setlobby <arena> &7- Sets the lobby for the arena \n" +
                    "&b/tnt setspawn <arena> &7- Sets the spawn point for the arena\n" +
                    "&b/tnt tp <arena> &7- Teleports you to the arena\n" +
                    "&7[-------&bTNT &cAdmin &bHelp&7-------]");

    public static final String HELP = Utils.colorize(
            "&7[------- &bTNT Help &7-------]\n" +
                    "&b/tnt join <arena> &7- Join the arena\n" +
                    "&b/tnt leave &7- Leaves the arena\n" +
                    "&b/tnt help &7- Displays the help command\n" +
                    "&7[------- &bTNT Help &7-------]");

    public static final String ARENA_CREATED = Utils.colorize(
            "&aSuccessfully created the arena {arena}");

    public static final String ARENA_DELETED = Utils.colorize(
            "&cSuccessfully deleted the arena {arena}");

    public static final String SET_LOBBY_SPAWN = Utils.colorize(
            "&aSuccessfully set the lobby spawn");

    public static final String SET_WORLD_SPAWN = Utils.colorize(
            "&aSuccessfully set the world spawn");

    public static final String SET_GAME_SPAWN = Utils.colorize(
            "&aSuccessfully set a game spawn");

    public static final String JOINED_ARENA = Utils.colorize(
            "&aSuccessfully joined the arena &2{arena}");

    public static final String LEFT_ARENA = Utils.colorize(
            "&cSuccessfully left the arena &2{arena}");

    public static final String NOT_IN_ARENA = Utils.colorize(
            "&cYou are not in this arena!");

    public static final String ARENA_NOT_FOUND = Utils.colorize(
            "&cThis arena does not exist!");

    public static final String LOBBY_COUNTDOWN = Utils.colorize(
            "&bThe game will start in &3{countdown}&b seconds!");

    public static final String ARENA_IS_FULL = Utils.colorize(
            "&cThis arena is full!");

    public static final String ALREADY_IN_ARENA = Utils.colorize(
            "&cYou are already in this arena!");

    public static final String CAN_NOT_MODIFY = Utils.colorize(
            "&cThis game is going on, you can not modify the arena");

    public static final String ARENA_EXISTS = Utils.colorize(
            "&cThis arena already exists!");

    public static final String TELEPORTED_TO_ARENA = Utils.colorize(
            "&aTeleported to the arena");

    public static final String GAME_IN_PROGRESS = Utils.colorize(
            "&cThis game is in progress");

    public static final String PLAYER_LEFT_GAME = Utils.colorize(
            "&b{player} &ahas left the game!");

    public static final String PLAYER_JOINED_GAME = Utils.colorize(
            "&b{player} &ahas joined the game!");

    public static final String NO_PERMISSION = Utils.colorize(
            "&cSomething went wrong, you do not have the permissions!");


    public static final String GAME_STARTED = Utils.colorize(
            "  &a===============================================\n" +
                    "&a=             &a&lGame Started! \n" +
                    "&a=    Run away from the tagger and stay alive!\n" +
                    "&a=              &2Good luck!\n" +
                    "&a===============================================");

    public static final String GAME_ENDED = Utils.colorize(
            "&a=========================================\n" +
                    "&a=          &a&lGame Ended!\n" +
                    "&a=         &6Winner: {winner}\n" +
                    "&a=======================================\n" +
                    "&2Players will be teleported out in 10 seconds!");


    public static final String TAGGER = Utils.colorize("&cYou are now the tagger! Tag someone else before you explode!");

    public static final String TAGGED_SOMEONE = Utils.colorize("&aGood job, you lost the TNT!");

    public static final String GOT_TAGGED = Utils.colorize("&cYou got tagged! Tag someone before you explode!");

    private Messages() {

    }

}
